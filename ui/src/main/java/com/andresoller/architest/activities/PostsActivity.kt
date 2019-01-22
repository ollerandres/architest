package com.andresoller.architest.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andresoller.architest.ArchitestApplication
import com.andresoller.architest.EXTRA_POST_ID
import com.andresoller.architest.R
import com.andresoller.architest.adapters.PostsAdapter
import com.andresoller.architest.adapters.PostsAdapter.PostNavigationListener
import com.andresoller.domain.entities.PostInfo
import com.andresoller.presentation.posts.PostsPresenter
import com.andresoller.presentation.posts.PostsView
import com.andresoller.presentation.posts.viewstates.PostsViewState
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_posts.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PostsActivity : AppCompatActivity(), PostsView, PostNavigationListener, CoroutineScope {

    @Inject
    lateinit var presenter: PostsPresenter
    @Inject
    lateinit var adapter: PostsAdapter
    private val loadDataChannel: Channel<Boolean> = Channel()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as ArchitestApplication).getUIComponent().inject(this)
        setContentView(R.layout.activity_posts)

        swipe_to_refresh_layout.setColorSchemeColors(resources.getColor(R.color.colorAccent))
        swipe_to_refresh_layout.setOnRefreshListener {
            loadData(true)
        }

        recycler_posts.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        recycler_posts.adapter = adapter

        adapter.navigationListener = this
    }

    override fun onResume() {
        super.onResume()
        presenter.bindIntents(this)
        loadData(true)
    }

    override fun onStop() {
        if (isFinishing) {
            presenter.unbind()
        }
        super.onStop()
    }

    private fun loadPosts(posts: ArrayList<PostInfo>) {
        adapter.updatePosts(posts)
    }

    private fun showError(display: Boolean, message: String) {
        if (display) {
            Snackbar.make(posts_constraint, message, Snackbar.LENGTH_LONG)
                    .show()
        }
    }

    private fun showProgressBar(refresh: Boolean) {
        swipe_to_refresh_layout.isRefreshing = refresh
    }

    override fun onPostTapped(postId: Int) {
        startActivity(Intent(this, PostDetailsActivity::class.java)
                .putExtra(EXTRA_POST_ID, postId))
    }

    override fun loadDataIntent(): Channel<Boolean> {
        return loadDataChannel
    }

    private fun loadData(loadData: Boolean) {
        launch {
            if (loadData) {
                loadDataChannel.send(loadData)
            }
        }
    }

    override fun render(state: PostsViewState) {
        with(state) {
            showProgressBar(progress)
            showError(error, errorMessage)
            loadPosts(posts)
        }
    }
}

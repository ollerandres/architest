package com.andresoller.babylonhealthtechtest.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andresoller.babylonhealthtechtest.BHApplication
import com.andresoller.babylonhealthtechtest.EXTRA_POST_ID
import com.andresoller.babylonhealthtechtest.R
import com.andresoller.babylonhealthtechtest.adapters.PostsAdapter
import com.andresoller.babylonhealthtechtest.adapters.PostsAdapter.PostNavigationListener
import com.andresoller.domain.entities.PostInfo
import com.andresoller.presentation.posts.PostsPresenter
import com.andresoller.presentation.posts.PostsView
import com.andresoller.presentation.posts.viewstates.PostsViewState
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_posts.*
import javax.inject.Inject
import kotlin.coroutines.Continuation

class PostsActivity : AppCompatActivity(), PostsView, PostNavigationListener {

    @Inject
    lateinit var presenter: PostsPresenter
    @Inject
    lateinit var adapter: PostsAdapter
    lateinit var result: Continuation<Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as BHApplication).getUIComponent().inject(this)
        setContentView(R.layout.activity_posts)

        swipe_to_refresh_layout.setColorSchemeColors(resources.getColor(R.color.colorAccent))
        swipe_to_refresh_layout.setOnRefreshListener {
            presenter.retry()
        }

        recycler_posts.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        recycler_posts.adapter = adapter

        adapter.navigationListener = this
    }

    override fun onResume() {
        super.onResume()
        presenter.bind(this)
    }

    override fun onPause() {
        presenter.unbind()
        super.onPause()
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

    override fun render(state: PostsViewState) {
        with(state) {
            showProgressBar(progress)
            showError(error, errorMessage)
            loadPosts(posts)
        }
    }
}

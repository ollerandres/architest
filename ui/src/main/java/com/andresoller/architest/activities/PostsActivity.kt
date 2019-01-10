package com.andresoller.architest.activities

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.jakewharton.rxbinding3.swiperefreshlayout.refreshes
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_posts.*
import javax.inject.Inject

class PostsActivity : AppCompatActivity(), PostsView, PostNavigationListener {

    @Inject
    lateinit var presenter: PostsPresenter
    @Inject
    lateinit var adapter: PostsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as ArchitestApplication).getUIComponent().inject(this)
        setContentView(R.layout.activity_posts)

        swipe_to_refresh_layout.setColorSchemeColors(resources.getColor(R.color.colorAccent))

        recycler_posts.layoutManager = LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)
        recycler_posts.adapter = adapter

        adapter.navigationListener = this
    }

    override fun onResume() {
        super.onResume()
        presenter.bindIntents(this)
    }

    override fun onPause() {
        presenter.onPause()
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

    override fun pullToRefreshIntent(): Observable<Boolean> {
        return swipe_to_refresh_layout.refreshes().map { true }
    }

    override fun loadPostsIntent(): Observable<Boolean> {
        return Observable.just(true)
    }

    override fun render(state: PostsViewState) {
        with(state) {
            showProgressBar(progress)
            showError(error, errorMessage)
            loadPosts(posts)
        }
    }
}

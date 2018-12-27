package com.andresoller.babylonhealthtechtest.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.andresoller.babylonhealthtechtest.BHApplication
import com.andresoller.babylonhealthtechtest.EXTRA_POST_ID
import com.andresoller.babylonhealthtechtest.R
import com.andresoller.babylonhealthtechtest.adapters.PostsAdapter
import com.andresoller.babylonhealthtechtest.adapters.PostsAdapter.PostNavigationListener
import com.andresoller.domain.entities.PostInfo
import com.andresoller.presentation.posts.PostsPresenter
import com.andresoller.presentation.posts.PostsView
import kotlinx.android.synthetic.main.activity_posts.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.longToast
import javax.inject.Inject

class PostsActivity : AppCompatActivity(), PostsView, PostNavigationListener {

    @Inject
    lateinit var presenter: PostsPresenter
    @Inject
    lateinit var adapter: PostsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as BHApplication).getUIComponent().inject(this)
        setContentView(R.layout.activity_posts)

        swipe_to_refresh_layout.setOnRefreshListener { presenter.onRetry() }
        swipe_to_refresh_layout.setColorSchemeColors(resources.getColor(R.color.colorAccent))

        recycler_posts.layoutManager = LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)
        recycler_posts.adapter = adapter

        adapter.navigationListener = this
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
        presenter.detachView()
    }

    override fun loadPosts(posts: ArrayList<PostInfo>) {
        adapter.updatePosts(posts)
    }

    override fun displayErrorBanner(message: String) {
        longToast(message)
    }

    override fun startLoading() {
        swipe_to_refresh_layout.isRefreshing = true
    }

    override fun stopLoading() {
        swipe_to_refresh_layout.isRefreshing = false
    }

    override fun displayNoResultsError(noResultMessage: String) {
        snackbar(posts_constraint, noResultMessage)
                .setAction(R.string.retry) { presenter.onRetry() }
    }

    override fun clearPosts() {
        adapter.updatePosts(ArrayList())
    }

    override fun onPostTapped(postId: Int) {
        startActivity(Intent(this, PostDetailActivity::class.java)
                .putExtra(EXTRA_POST_ID, postId))
    }

    override fun noResultMessage(): String {
        return getString(R.string.error_no_results)
    }
}

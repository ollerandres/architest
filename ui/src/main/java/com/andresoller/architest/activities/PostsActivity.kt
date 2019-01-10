package com.andresoller.architest.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.andresoller.architest.ArchitestApplication
import com.andresoller.architest.EXTRA_POST_ID
import com.andresoller.architest.R
import com.andresoller.architest.adapters.PostsAdapter
import com.andresoller.architest.adapters.PostsAdapter.PostNavigationListener
import com.andresoller.domain.entities.PostInfo
import com.andresoller.presentation.posts.PostsViewModel
import com.andresoller.presentation.posts.PostsViewModelFactory
import com.andresoller.presentation.posts.viewstates.PostsViewState
import kotlinx.android.synthetic.main.activity_posts.*
import javax.inject.Inject

class PostsActivity : AppCompatActivity(), PostNavigationListener {

    @Inject
    lateinit var viewModelFactory: PostsViewModelFactory
    @Inject
    lateinit var adapter: PostsAdapter
    lateinit var viewModel: PostsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as ArchitestApplication).getUIComponent().inject(this)
        setContentView(R.layout.activity_posts)

        swipe_to_refresh_layout.setOnRefreshListener { viewModel.loadPosts() }
        swipe_to_refresh_layout.setColorSchemeColors(resources.getColor(R.color.colorAccent))

        recycler_posts.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        recycler_posts.adapter = adapter

        adapter.navigationListener = this

        viewPostsObserver()
    }

    override fun onStart() {
        super.onStart()
        viewPostsObserver()
        viewModel.loadPosts()
    }

    private fun viewPostsObserver() {
        viewModel = ViewModelProviders.of(this, viewModelFactory)[PostsViewModel::class.java]
        viewModel.viewStateMutable.apply {
            if (hasObservers()) {
                removeObservers(this@PostsActivity)
            }
            observe(this@PostsActivity, Observer {
                render(it!!)
            })
        }
    }

    private fun render(state: PostsViewState) {
        with(state) {
            showProgressBar(progress)
            showError(error, errorMessage)
            loadPosts(posts)
        }
    }

    private fun loadPosts(posts: ArrayList<PostInfo>) {
        adapter.updatePosts(posts)
    }

    private fun showError(display: Boolean, message: String) {
        if (display) {
            com.google.android.material.snackbar.Snackbar.make(posts_constraint, message, com.google.android.material.snackbar.Snackbar.LENGTH_LONG)
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
}

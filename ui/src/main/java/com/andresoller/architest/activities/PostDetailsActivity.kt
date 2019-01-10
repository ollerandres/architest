package com.andresoller.architest.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.andresoller.architest.ArchitestApplication
import com.andresoller.architest.EXTRA_POST_ID
import com.andresoller.architest.R
import com.andresoller.architest.adapters.CommentsAdapter
import com.andresoller.domain.entities.PostDetailsInfo
import com.andresoller.presentation.postdetail.PostDetailsViewModel
import com.andresoller.presentation.postdetail.PostDetailsViewModelFactory
import com.andresoller.presentation.postdetail.viewstates.PostDetailsViewState
import kotlinx.android.synthetic.main.activity_post_detail.*
import javax.inject.Inject


class PostDetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: PostDetailsViewModelFactory
    @Inject
    lateinit var adapter: CommentsAdapter
    @Inject
    lateinit var viewModel: PostDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
        (application as ArchitestApplication).getUIComponent().inject(this)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recycler_comments.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)
        recycler_comments.adapter = adapter
        recycler_comments.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                viewModel.collapseDetails((recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findFirstCompletelyVisibleItemPosition() > 0)
            }
        })

        swipe_to_refresh_layout.setOnRefreshListener { viewModel.loadPostDetails(intent.getIntExtra(EXTRA_POST_ID, -1).toString()) }
        swipe_to_refresh_layout.setColorSchemeColors(resources.getColor(R.color.colorAccent))
    }

    override fun onStart() {
        super.onStart()
        viewPostsObserver()
        viewModel.loadPostDetails(intent.getIntExtra(EXTRA_POST_ID, -1).toString())
    }

    private fun viewPostsObserver() {
        viewModel = ViewModelProviders.of(this, viewModelFactory)[PostDetailsViewModel::class.java]
        viewModel.collapseDetailsLiveData.apply {
            if (hasObservers()) {
                removeObservers(this@PostDetailsActivity)
            }
            observe(this@PostDetailsActivity, Observer {
                render(it!!)
            })
        }

        viewModel.viewState.apply {
            if (hasObservers()) {
                removeObservers(this@PostDetailsActivity)
            }
            observe(this@PostDetailsActivity, Observer {
                render(it!!)
            })
        }
    }

    private fun showError(display: Boolean, message: String) {
        if (display) {
            com.google.android.material.snackbar.Snackbar.make(post_details_constraint_layout, message, com.google.android.material.snackbar.Snackbar.LENGTH_LONG)
                    .show()
        }
    }

    private fun showProgressBar(refresh: Boolean) {
        swipe_to_refresh_layout.isRefreshing = refresh
    }

    private fun loadPostDetails(postDetailsInfo: PostDetailsInfo) {
        cv_post.visibility = if (postDetailsInfo.body.isNotEmpty()) VISIBLE else GONE
        tv_post_comments_count.visibility = if (postDetailsInfo.commentsCount > 0) VISIBLE else GONE
        tv_post_title.text = postDetailsInfo.title
        tv_post_user.text = postDetailsInfo.name
        tv_post_body.text = postDetailsInfo.body
        tv_post_comments_count.text = getString(R.string.comments_count, postDetailsInfo.commentsCount.toString())
        adapter.updateComments(postDetailsInfo.comments)
    }

    private fun collapsePost(collapse: Boolean) {
        if (collapse) {
            full_post.animate()
                    .alpha(0f)
                    .setDuration(500L)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            super.onAnimationEnd(animation)
                            tv_post_body.visibility = View.GONE
                        }
                    })
        } else {
            full_post.animate()
                    .alpha(1f)
                    .setDuration(500L)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationStart(animation: Animator?) {
                            super.onAnimationStart(animation)
                            tv_post_body.visibility = View.VISIBLE
                        }
                    })
        }
    }

    fun render(state: PostDetailsViewState) {
        with(state) {
            showProgressBar(progress)
            showError(error, errorMessage)
            loadPostDetails(postDetails)
            collapsePost(collapsePost)
        }
    }
}

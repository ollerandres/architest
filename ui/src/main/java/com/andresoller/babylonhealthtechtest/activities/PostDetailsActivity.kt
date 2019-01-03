package com.andresoller.babylonhealthtechtest.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.andresoller.babylonhealthtechtest.BHApplication
import com.andresoller.babylonhealthtechtest.EXTRA_POST_ID
import com.andresoller.babylonhealthtechtest.R
import com.andresoller.babylonhealthtechtest.adapters.CommentsAdapter
import com.andresoller.domain.entities.PostDetailsInfo
import com.andresoller.presentation.postdetails.PostDetailsPresenter
import com.andresoller.presentation.postdetails.PostDetailsView
import com.andresoller.presentation.postdetails.viewstates.PostDetailsViewState
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding3.recyclerview.scrollEvents
import com.jakewharton.rxbinding3.swiperefreshlayout.refreshes
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_post_details.*
import javax.inject.Inject


class PostDetailsActivity : AppCompatActivity(), PostDetailsView {

    @Inject
    lateinit var presenter: PostDetailsPresenter
    @Inject
    lateinit var adapter: CommentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)
        (application as BHApplication).getUIComponent().inject(this)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recycler_comments.layoutManager = LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)
        recycler_comments.adapter = adapter

        swipe_to_refresh_layout.setColorSchemeColors(resources.getColor(R.color.colorAccent))
    }

    override fun onResume() {
        super.onResume()
        presenter.bindIntents(this)
    }

    override fun onPause() {
        presenter.onPause()
        super.onPause()
    }

    private fun showError(display: Boolean, message: String) {
        if (display) {
            Snackbar.make(post_details_constraint_layout, message, Snackbar.LENGTH_LONG)
                    .show()
        }
    }

    private fun showProgressBar(refresh: Boolean) {
        swipe_to_refresh_layout.isRefreshing = refresh
    }

    private fun loadPostDetails(postDetailsInfo: PostDetailsInfo) {
        cv_post.visibility = if (postDetailsInfo.body.isNotEmpty()) VISIBLE else GONE
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

    override fun postIdExtraIntent(): Observable<Int> {
        return Observable.just(intent.getIntExtra(EXTRA_POST_ID, -1))
    }

    override fun scrolledRecyclerViewIntent(): Observable<Boolean> {
        return recycler_comments.scrollEvents().map { (recycler_comments.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() > 0 }
    }

    override fun pullToRefreshIntent(): Observable<Int> {
        return swipe_to_refresh_layout.refreshes().map { intent.getIntExtra(EXTRA_POST_ID, -1) }
    }

    override fun render(state: PostDetailsViewState) {
        with(state) {
            showProgressBar(progress)
            showError(error, errorMessage)
            loadPostDetails(postDetails)
            collapsePost(collapsePost)
        }
    }
}

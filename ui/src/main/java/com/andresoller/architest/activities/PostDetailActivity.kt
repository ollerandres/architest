package com.andresoller.architest.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.andresoller.architest.EXTRA_POST_ID
import com.andresoller.architest.R
import com.andresoller.architest.adapters.CommentsAdapter
import com.andresoller.domain.entities.PostDetailsInfo
import com.andresoller.presentation.postdetail.PostDetailPresenter
import com.andresoller.presentation.postdetail.PostDetailView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_post_detail.*
import org.jetbrains.anko.design.snackbar
import javax.inject.Inject

@AndroidEntryPoint
class PostDetailActivity : AppCompatActivity(), PostDetailView {

    @Inject
    lateinit var presenter: PostDetailPresenter
    @Inject
    lateinit var adapter: CommentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recycler_comments.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        recycler_comments.adapter = adapter
        recycler_comments.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                presenter.onCommentsScrolled((recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findFirstCompletelyVisibleItemPosition())
            }
        })

        swipe_to_refresh_layout.setOnRefreshListener { presenter.onRetry(intent.getIntExtra(EXTRA_POST_ID, -1).toString()) }
        swipe_to_refresh_layout.setColorSchemeColors(resources.getColor(R.color.colorAccent))
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
        presenter.onResume(intent.getIntExtra(EXTRA_POST_ID, -1).toString())
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

    override fun startLoading() {
        swipe_to_refresh_layout.isRefreshing = true
    }

    override fun stopLoading() {
        swipe_to_refresh_layout.isRefreshing = false
    }

    override fun displayError(message: String) {
        snackbar(recycler_comments, message)
                .setAction(R.string.retry) { presenter.onRetry(intent.getStringExtra(EXTRA_POST_ID)) }
    }

    override fun loadPostDetails(postDetailsInfo: PostDetailsInfo) {
        tv_post_title.text = postDetailsInfo.title
        tv_post_user.text = postDetailsInfo.name
        tv_post_body.text = postDetailsInfo.body
        tv_post_comments_count.text = getString(R.string.comments_count, postDetailsInfo.commentsCount.toString())
        adapter.updateComments(postDetailsInfo.comments)
    }

    override fun noResultMessage(): String {
        return getString(R.string.error_no_results)
    }

    override fun collapsePost() {
        full_post.animate()
                .alpha(0f)
                .setDuration(500L)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        tv_post_body.visibility = View.GONE
                    }
                })
    }

    override fun expandPost() {
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

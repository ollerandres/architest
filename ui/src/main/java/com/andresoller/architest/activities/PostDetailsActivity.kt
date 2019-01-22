package com.andresoller.architest.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andresoller.architest.ArchitestApplication
import com.andresoller.architest.EXTRA_POST_ID
import com.andresoller.architest.R
import com.andresoller.architest.adapters.CommentsAdapter
import com.andresoller.domain.entities.PostDetailsInfo
import com.andresoller.presentation.postdetails.PostDetailsPresenter
import com.andresoller.presentation.postdetails.PostDetailsView
import com.andresoller.presentation.postdetails.viewstates.PostDetailsViewState
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_post_details.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PostDetailsActivity : AppCompatActivity(), PostDetailsView, CoroutineScope {

    @Inject
    lateinit var presenter: PostDetailsPresenter
    @Inject
    lateinit var adapter: CommentsAdapter
    private val loadDataChannel: Channel<Boolean> = Channel()
    private val onScrollChannel: Channel<Boolean> = Channel()
    private val postIdChannel: Channel<Int> = Channel()
    private val collapseStateChannel: Channel<Boolean> = Channel()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)
        (application as ArchitestApplication).getUIComponent().inject(this)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recycler_comments.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        recycler_comments.adapter = adapter

        swipe_to_refresh_layout.setColorSchemeColors(resources.getColor(R.color.colorAccent))
        swipe_to_refresh_layout.setOnRefreshListener { loadData(true) }

        recycler_comments.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                onScrolled((recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() > 0)
            }
        })
    }

    fun onScrolled(firstItemVisible: Boolean) {
        launch {
            onScrollChannel.send(true)
            collapseStateChannel.send(firstItemVisible)
        }
    }

    override fun onScrollIntent(): Channel<Boolean> {
        return onScrollChannel
    }

    override fun loadDataIntent(): Channel<Boolean> {
        return loadDataChannel
    }

    override fun postIdChannel(): Channel<Int> {
        return postIdChannel
    }

    override fun collapseStateChannel(): Channel<Boolean> {
        return collapseStateChannel
    }

    private fun loadData(loadData: Boolean) {
        launch {
            if (loadData) {
                loadDataChannel.send(loadData)
                postIdChannel.send(intent.getIntExtra(EXTRA_POST_ID, -1))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.bindIntents(this)
        loadData(true)
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
        tv_post_comments_count.visibility = if (postDetailsInfo.comments.isNotEmpty()) VISIBLE else GONE
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

    override fun render(state: PostDetailsViewState) {
        with(state) {
            showProgressBar(progress)
            showError(error, errorMessage)
            loadPostDetails(postDetails)
            collapsePost(collapsePost)
        }
    }
}

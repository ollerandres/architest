package com.andresoller.architest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation.RELATIVE_TO_SELF
import android.view.animation.TranslateAnimation
import androidx.recyclerview.widget.RecyclerView
import com.andresoller.architest.R
import com.andresoller.domain.entities.PostInfo
import kotlinx.android.synthetic.main.item_post.view.*
import javax.inject.Inject


class PostsAdapter @Inject constructor() : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    private var posts: ArrayList<PostInfo> = ArrayList()
    public var navigationListener: PostNavigationListener? = null

    interface PostNavigationListener {
        fun onPostTapped(postId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false))
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bindItems(posts[position], navigationListener)
        setScaleAnimation(holder.itemView)
    }

    fun updatePosts(posts: ArrayList<PostInfo>) {
        this.posts = posts
        notifyDataSetChanged()
    }

    private fun setScaleAnimation(view: View) {
        val anim = TranslateAnimation(RELATIVE_TO_SELF, 1f, RELATIVE_TO_SELF, 0f, RELATIVE_TO_SELF, 0f, RELATIVE_TO_SELF, 0f)
        anim.duration = 500L
        view.startAnimation(anim)
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(postInfo: PostInfo, navigationListener: PostNavigationListener?) {
            itemView.tv_title.text = postInfo.title
            itemView.tv_username.text = postInfo.username
            itemView.tv_address.text = postInfo.address

            itemView.setOnClickListener { view ->
                navigationListener?.onPostTapped(postInfo.id)
            }
        }
    }
}
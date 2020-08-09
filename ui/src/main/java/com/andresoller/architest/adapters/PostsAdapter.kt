package com.andresoller.architest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.recyclerview.widget.RecyclerView
import com.andresoller.architest.R
import com.andresoller.domain.entities.PostInfo
import kotlinx.android.synthetic.main.item_post.view.*
import javax.inject.Inject


class PostsAdapter @Inject constructor() : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    private var posts: List<PostInfo> = ArrayList()
    var navigationListener: PostNavigationListener? = null

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

    fun updatePosts(posts: List<PostInfo>) {
        this.posts = posts
        notifyDataSetChanged()
    }

    private fun setScaleAnimation(view: View) {
        val anim = ScaleAnimation(0.8f, 1.0f, 0.8f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        anim.duration = 1000L
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
package com.andresoller.architest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andresoller.architest.R
import com.andresoller.domain.entities.CommentInfo
import kotlinx.android.synthetic.main.item_comments.view.*
import javax.inject.Inject

class CommentsAdapter @Inject constructor() : androidx.recyclerview.widget.RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {

    private var comments: ArrayList<CommentInfo> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        return CommentsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_comments, parent, false))
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.bindItems(comments[position])
    }

    fun updateComments(comments: ArrayList<CommentInfo>) {
        this.comments = comments
        notifyDataSetChanged()
    }

    class CommentsViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        fun bindItems(commentsInfo: CommentInfo) {
            itemView.tv_title.text = commentsInfo.title
            itemView.tv_body.text = commentsInfo.body
            itemView.tv_username.text = commentsInfo.email
        }
    }
}
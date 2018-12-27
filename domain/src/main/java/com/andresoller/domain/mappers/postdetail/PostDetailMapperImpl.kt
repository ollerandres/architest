package com.andresoller.domain.mappers.postdetail

import com.andresoller.data.model.Comment
import com.andresoller.data.model.Post
import com.andresoller.data.model.User
import com.andresoller.domain.entities.CommentInfo
import com.andresoller.domain.entities.PostDetailsInfo
import javax.inject.Inject

class PostDetailMapperImpl @Inject constructor() : PostDetailMapper {

    override fun mapToEntity(post: Post, users: List<User>, comments: List<Comment>): PostDetailsInfo {
        return PostDetailsInfo(post.title.capitalize(), post.body.capitalize(), mapUserName(post.userId, users), comments.size, mapComments(comments))
    }

    private fun mapUserName(userId: Int, users: List<User>): String {
        return users.find { it.id == userId }?.username.toString()
    }

    private fun mapComments(comments: List<Comment>): ArrayList<CommentInfo> {
        val commentsInfo = ArrayList<CommentInfo>()
        for (comment in comments) {
            commentsInfo.add(CommentInfo(comment.id, comment.email.toLowerCase(), comment.name.capitalize(), comment.body.capitalize()))
        }
        return commentsInfo
    }
}
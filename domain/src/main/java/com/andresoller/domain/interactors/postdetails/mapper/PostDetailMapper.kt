package com.andresoller.domain.interactors.postdetails.mapper

import com.andresoller.data.model.Comment
import com.andresoller.data.model.Post
import com.andresoller.data.model.User
import com.andresoller.domain.entities.PostDetailsInfo

interface PostDetailMapper {

    fun mapToEntity(post: Post, users: List<User>, comments: List<Comment>): PostDetailsInfo
}
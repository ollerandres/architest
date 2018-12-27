package com.andresoller.domain.mappers.postdetail

import com.andresoller.data.model.Comment
import com.andresoller.data.model.Post
import com.andresoller.data.model.User
import com.andresoller.domain.entities.PostDetailsInfo

interface PostDetailMapper {

    fun mapToEntity(post: Post, users: List<User>, comments: List<Comment>): PostDetailsInfo
}
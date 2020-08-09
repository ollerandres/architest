package com.andresoller.data.mappers.posts

import com.andresoller.data.model.Post
import com.andresoller.data.model.User
import com.andresoller.domain.entities.PostInfo

interface PostMapper {

    fun mapToEntity(posts: List<Post>, users: List<User>): List<PostInfo>
}
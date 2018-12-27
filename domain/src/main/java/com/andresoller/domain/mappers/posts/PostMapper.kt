package com.andresoller.domain.mappers.posts

import com.andresoller.data.model.Post
import com.andresoller.data.model.User
import com.andresoller.domain.entities.PostInfo

interface PostMapper {

    fun mapToEntity(posts: List<Post>, users: List<User>): ArrayList<PostInfo>
}
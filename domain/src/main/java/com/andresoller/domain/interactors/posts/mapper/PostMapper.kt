package com.andresoller.domain.interactors.posts.mapper

import com.andresoller.data.model.Post
import com.andresoller.data.model.User
import com.andresoller.domain.entities.PostInfo

interface PostMapper {

    fun mapToEntity(posts: List<Post>, users: List<User>): ArrayList<PostInfo>
}
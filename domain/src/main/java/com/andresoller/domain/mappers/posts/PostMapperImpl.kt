package com.andresoller.domain.mappers.posts

import com.andresoller.data.model.Post
import com.andresoller.data.model.User
import com.andresoller.domain.entities.PostInfo
import javax.inject.Inject

class PostMapperImpl @Inject constructor() : PostMapper {

    override fun mapToEntity(posts: List<Post>, users: List<User>): ArrayList<PostInfo> {
        val postsInfo = ArrayList<PostInfo>()
        for (post in posts) {
            val user = users.find { it.id == post.userId }
            user?.let {
                postsInfo.add(PostInfo(post.id,
                        post.title.capitalize(),
                        user.username,
                        user.address.street.capitalize() + ", " + user.address.city.capitalize() + ", " + user.address.zipcode.capitalize()))
            }
        }
        return postsInfo
    }

}
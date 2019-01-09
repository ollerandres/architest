package com.andresoller.domain.interactors.posts

import com.andresoller.domain.entities.PostInfo

interface PostsInteractor {

    suspend fun getPosts(): ArrayList<PostInfo>
}
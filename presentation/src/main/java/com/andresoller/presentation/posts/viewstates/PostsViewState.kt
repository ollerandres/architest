package com.andresoller.presentation.posts.viewstates

import com.andresoller.domain.entities.PostInfo

data class PostsViewState(val progress: Boolean = false,
                          val error: Boolean = false,
                          val errorMessage: String = "",
                          val posts: ArrayList<PostInfo> = arrayListOf())
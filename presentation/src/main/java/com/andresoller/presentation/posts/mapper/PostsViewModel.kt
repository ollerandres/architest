package com.andresoller.presentation.posts.mapper

import com.andresoller.domain.entities.PostInfo
import com.andresoller.presentation.posts.viewstates.PartialPostsViewState

interface PostsViewStateMapper {

    fun mapToViewState(posts: ArrayList<PostInfo>): PartialPostsViewState
}
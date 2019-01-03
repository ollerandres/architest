package com.andresoller.presentation.posts.mapper

import com.andresoller.domain.entities.PostInfo
import com.andresoller.presentation.posts.viewstates.PartialPostsViewState
import javax.inject.Inject

class PostsViewStateMapperImpl @Inject constructor() : PostsViewStateMapper {

    override fun mapToViewState(posts: ArrayList<PostInfo>): PartialPostsViewState {
        return PartialPostsViewState.PostsFetchedState(posts)
    }

}
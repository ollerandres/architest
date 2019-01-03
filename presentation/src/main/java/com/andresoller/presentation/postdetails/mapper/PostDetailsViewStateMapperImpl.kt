package com.andresoller.presentation.postdetails.mapper

import com.andresoller.domain.entities.PostDetailsInfo
import com.andresoller.presentation.postdetails.viewstates.PartialPostDetailsViewState

class PostDetailsViewStateMapperImpl : PostDetailsViewStateMapper {

    override fun mapToViewState(postDetailsInfo: PostDetailsInfo): PartialPostDetailsViewState {
        return PartialPostDetailsViewState.PostDetailsFetchedState(postDetailsInfo)
    }

}
package com.andresoller.presentation.postdetail.mapper

import com.andresoller.domain.entities.PostDetailsInfo
import com.andresoller.presentation.postdetail.viewstates.PartialPostDetailsViewState

class PostDetailsViewStateMapperImpl : PostDetailsViewStateMapper {

    override fun mapToViewState(postDetailsInfo: PostDetailsInfo): PartialPostDetailsViewState {
        return PartialPostDetailsViewState.PostDetailsFetchedState(postDetailsInfo)
    }

}
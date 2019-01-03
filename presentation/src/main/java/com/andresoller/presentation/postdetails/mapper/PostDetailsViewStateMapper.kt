package com.andresoller.presentation.postdetails.mapper

import com.andresoller.domain.entities.PostDetailsInfo
import com.andresoller.presentation.postdetails.viewstates.PartialPostDetailsViewState

interface PostDetailsViewStateMapper {
    fun mapToViewState(postDetailsInfo: PostDetailsInfo): PartialPostDetailsViewState
}

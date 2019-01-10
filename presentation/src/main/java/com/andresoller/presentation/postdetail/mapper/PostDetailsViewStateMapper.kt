package com.andresoller.presentation.postdetail.mapper

import com.andresoller.domain.entities.PostDetailsInfo
import com.andresoller.presentation.postdetail.viewstates.PartialPostDetailsViewState

interface PostDetailsViewStateMapper {
    fun mapToViewState(postDetailsInfo: PostDetailsInfo): PartialPostDetailsViewState
}
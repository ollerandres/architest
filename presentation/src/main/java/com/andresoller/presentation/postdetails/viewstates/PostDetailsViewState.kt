package com.andresoller.presentation.postdetails.viewstates

import com.andresoller.domain.entities.PostDetailsInfo

data class PostDetailsViewState(val progress: Boolean = false,
                                val error: Boolean = false,
                                val errorMessage: String = "",
                                val collapsePost: Boolean = false,
                                val postDetails: PostDetailsInfo = PostDetailsInfo())
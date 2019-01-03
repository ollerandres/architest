package com.andresoller.presentation.postdetails;

import com.andresoller.presentation.postdetails.viewstates.PostDetailsViewState
import io.reactivex.Observable

interface PostDetailsView {

    fun postIdExtraIntent(): Observable<Int>

    fun scrolledRecyclerViewIntent(): Observable<Boolean>

    fun pullToRefreshIntent(): Observable<Int>

    fun render(state: PostDetailsViewState)
}
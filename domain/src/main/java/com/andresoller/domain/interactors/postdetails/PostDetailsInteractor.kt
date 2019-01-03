package com.andresoller.domain.interactors.postdetails

import com.andresoller.domain.entities.PostDetailsInfo
import io.reactivex.Observable

interface PostDetailsInteractor {

    fun getPostDetail(postId: Int): Observable<PostDetailsInfo>
}
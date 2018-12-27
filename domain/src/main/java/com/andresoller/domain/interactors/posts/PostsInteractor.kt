package com.andresoller.domain.interactors.posts

import com.andresoller.domain.entities.PostDetailsInfo
import com.andresoller.domain.entities.PostInfo
import io.reactivex.Observable

interface PostsInteractor {

    fun getPosts(): Observable<ArrayList<PostInfo>>

    fun getPostDetail(postId: String): Observable<PostDetailsInfo>
}
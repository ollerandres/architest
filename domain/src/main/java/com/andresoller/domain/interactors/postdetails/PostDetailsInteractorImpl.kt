package com.andresoller.domain.interactors.postdetails

import com.andresoller.data.remote.RemoteRepository
import com.andresoller.domain.entities.PostDetailsInfo
import com.andresoller.domain.interactors.postdetails.mapper.PostDetailMapper
import io.reactivex.Observable
import io.reactivex.functions.Function3
import javax.inject.Inject

class PostDetailsInteractorImpl @Inject constructor(private val repository: RemoteRepository,
                                                    private val mapper: PostDetailMapper) : PostDetailsInteractor {

    override fun getPostDetail(postId: Int): Observable<PostDetailsInfo> {
        return Observable.zip(repository.getPostDetail(postId),
                repository.getUsers(),
                repository.getPostComments(postId),
                Function3 { post, users, comments ->
                    return@Function3 mapper.mapToEntity(post, users, comments)
                })
    }
}
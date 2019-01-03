package com.andresoller.domain.interactors.posts;

import com.andresoller.data.model.Post
import com.andresoller.data.model.User
import com.andresoller.data.remote.RemoteRepository
import com.andresoller.domain.entities.PostInfo
import com.andresoller.domain.interactors.posts.mapper.PostMapper
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class PostsInteractorImpl @Inject constructor(private val repository: RemoteRepository,
                                              private val postMapper: PostMapper) : PostsInteractor {

    override fun getPosts(): Observable<ArrayList<PostInfo>> {
        return Observable.zip(repository.getPosts(), repository.getUsers(), BiFunction<List<Post>, List<User>, ArrayList<PostInfo>> { posts, users ->
            postMapper.mapToEntity(posts, users)
        })
    }
}

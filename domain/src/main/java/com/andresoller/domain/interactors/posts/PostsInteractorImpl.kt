package com.andresoller.domain.interactors.posts;

import com.andresoller.data.model.Post
import com.andresoller.data.model.User
import com.andresoller.data.remote.RemoteRepository
import com.andresoller.domain.entities.PostDetailsInfo
import com.andresoller.domain.entities.PostInfo
import com.andresoller.domain.mappers.postdetail.PostDetailMapper
import com.andresoller.domain.mappers.posts.PostMapper
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import javax.inject.Inject

class PostsInteractorImpl @Inject constructor(val repository: RemoteRepository,
                                              val postMapper: PostMapper,
                                              val postDetailMapper: PostDetailMapper) : PostsInteractor {

    override fun getPosts(): Observable<ArrayList<PostInfo>> {
        return Observable.zip(repository.getPosts(), repository.getUsers(), BiFunction<List<Post>, List<User>, ArrayList<PostInfo>> { posts, users ->
            return@BiFunction postMapper.mapToEntity(posts, users)
        })
    }

    override fun getPostDetail(postId: String): Observable<PostDetailsInfo> {
        return Observable.zip(repository.getPostDetail(postId),
                repository.getUsers(),
                repository.getPostComments(postId),
                Function3 { post, users, comments ->
                    return@Function3 postDetailMapper.mapToEntity(post, users, comments)
                })
    }
}

package com.andresoller.data.repositories

import com.andresoller.data.Repository
import com.andresoller.data.model.Comment
import com.andresoller.data.model.Post
import com.andresoller.data.model.User
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PostsRepository @Inject constructor(private val remoteRepository: Repository,
                                          private val localRepository: Repository) : Repository {

    override fun getPosts(): Observable<ArrayList<Post>> {
        return Observable.concat(localRepository.getPosts(),
                remoteRepository.getPosts()
                        .doAfterNext {
                            savePosts(it)
                        }.materialize()
                        .filter { !it.isOnError }
                        .dematerialize<ArrayList<Post>>()
                        .debounce(200, TimeUnit.MILLISECONDS))
    }

    override fun getPostDetail(postId: String): Observable<Post> {
        return Observable.concat(localRepository.getPostDetail(postId),
                remoteRepository.getPostDetail(postId)
                        .doAfterNext {
                            savePosts(arrayListOf(it))
                        }.materialize()
                        .filter { !it.isOnError }
                        .dematerialize<Post>()
                        .debounce(200, TimeUnit.MILLISECONDS))
    }

    override fun getUsers(): Observable<ArrayList<User>> {
        return Observable.concat(localRepository.getUsers(),
                remoteRepository.getUsers()
                        .doAfterNext {
                            saveUsers(it)
                        }
                        .materialize()
                        .filter { !it.isOnError }
                        .dematerialize<ArrayList<User>>()
                        .debounce(200, TimeUnit.MILLISECONDS))
    }

    override fun getPostComments(postId: String): Observable<ArrayList<Comment>> {
        return Observable.concat(localRepository.getPostComments(postId),
                remoteRepository.getPostComments(postId)
                        .doAfterNext {
                            saveComments(it)
                        }
                        .materialize()
                        .filter { !it.isOnError }
                        .dematerialize<ArrayList<Comment>>()
                        .debounce(200, TimeUnit.MILLISECONDS))
    }

    override fun savePosts(posts: ArrayList<Post>) {
        localRepository.savePosts(posts)
    }

    override fun saveUsers(users: ArrayList<User>) {
        localRepository.saveUsers(users)
    }

    override fun saveComments(comments: ArrayList<Comment>) {
        localRepository.saveComments(comments)
    }
}
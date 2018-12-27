package com.andresoller.data.remote

import com.andresoller.data.model.Comment
import com.andresoller.data.model.Post
import com.andresoller.data.model.User
import com.andresoller.mlsearch.data.remote.ApiClient
import io.reactivex.Observable
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class RemoteRepositoryImplTest {

    @Mock
    lateinit var apiClient: ApiClient
    @InjectMocks
    lateinit var repository: RemoteRepositoryImpl

    @Before
    fun testSetup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getPosts_shouldCallApiClientGetPostsAndReturnObservable() {
        val posts = Observable.just(List(1) { Post() })
        `when`(repository.getPosts()).thenReturn(posts)

        repository.getPosts()

        verify(apiClient).getPosts()
        assertEquals(posts, apiClient.getPosts())
    }

    @Test
    fun getPostDetail_shouldCallApiClientGetPostDetailAndReturnObservable() {
        val post = Observable.just(Post())
        `when`(apiClient.getPostDetail("1")).thenReturn(post)

        repository.getPostDetail("1")

        verify(apiClient).getPostDetail("1")
        assertEquals(post, apiClient.getPostDetail("1"))
    }

    @Test
    fun getUsers_shouldCallApiClientGetUsersAndReturnObservable() {
        val users = Observable.just(List(1) { User() })
        `when`(apiClient.getUsers()).thenReturn(users)

        repository.getUsers()

        verify(apiClient).getUsers()
        assertEquals(users, apiClient.getUsers())
    }

    @Test
    fun getPostComments_shouldCallApiClientGetCommentsAndReturnObservable() {
        val posts = Observable.just(List(1) { Comment() })
        `when`(apiClient.getPostComments("1")).thenReturn(posts)

        repository.getPostComments("1")

        verify(apiClient).getPostComments("1")
        assertEquals(posts, apiClient.getPostComments("1"))
    }
}
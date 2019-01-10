package com.andresoller.data.local

import com.andresoller.data.model.*
import io.reactivex.Maybe
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class LocalRepositoryImplTest {

    @Mock
    lateinit var database: AppDatabase
    @InjectMocks
    lateinit var repository: LocalRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getPosts_shouldCallDatabaseGetPostsAndReturnObservable() {
        val posts = Maybe.just(listOf(Post()))
        `when`(database.postDao()).thenReturn(mock(PostDao::class.java))
        `when`(database.postDao().getPosts()).thenReturn(posts)

        repository.getPosts()

        verify(database.postDao()).getPosts()
        assertEquals(posts, database.postDao().getPosts())
    }

    @Test
    fun getPostDetail_shouldCallDatabaseGetPostDetailAndReturnObservable() {
        val post = Maybe.just(Post())
        `when`(database.postDao()).thenReturn(mock(PostDao::class.java))
        `when`(database.postDao().getPost("1")).thenReturn(post)

        repository.getPostDetail("1")

        verify(database.postDao()).getPost("1")
        assertEquals(post, database.postDao().getPost("1"))
    }

    @Test
    fun getUsers_shouldCallDatabaseGetUsersAndReturnObservable() {
        val users = Maybe.just(listOf(User()))
        `when`(database.userDao()).thenReturn(mock(UserDao::class.java))
        `when`(database.userDao().getUsers()).thenReturn(users)

        repository.getUsers()

        verify(database.userDao()).getUsers()
        assertEquals(users, database.userDao().getUsers())
    }

    @Test
    fun getPostComments_shouldCallDatabaseGetCommentsAndReturnObservable() {
        val posts = Maybe.just(listOf(Comment()))
        `when`(database.commentDao()).thenReturn(mock(CommentDao::class.java))
        `when`(database.commentDao().getPostComments("1")).thenReturn(posts)

        repository.getPostComments("1")

        verify(database.commentDao()).getPostComments("1")
        assertEquals(posts, database.commentDao().getPostComments("1"))
    }
}
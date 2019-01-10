package com.andresoller.domain.mappers.posts

import com.andresoller.data.model.*
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations

class PostMapperImplTest {

    @InjectMocks
    lateinit var postMapper: PostMapperImpl

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun mapToEntity_emptyPosts_returnEmptyArray() {
        val posts = postMapper.mapToEntity(getPostsList(), getUserList())
        assertEquals(1, posts.size)
        assertEquals(1, posts[0].id)
        assertEquals("Street, City, Zipcode", posts[0].address)
        assertEquals("Title", posts[0].title)
        assertEquals("username", posts[0].username)
    }

    private fun getUserList(): List<User> {
        return listOf(User(1, "website", Address("zipcode", Geo(), "suite", "city", "street"), "phone", "name", Company(), "Email", "username"))
    }

    private fun getPostsList(): List<Post> {
        return listOf(Post(1, "title", "body", 1))
    }
}
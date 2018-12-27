package com.andresoller.domain.mappers.postdetail

import com.andresoller.data.model.*
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations


class PostDetailMapperImplTest {

    @InjectMocks
    lateinit var postDetailMapper: PostDetailMapperImpl

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun mapToEntity_emptyPosts_returnEmptyArray() {
        val postInfo = postDetailMapper.mapToEntity(getPostInfo(), getUserList(), getCommentsList())
        assertEquals(1, postInfo.commentsCount)
        assertEquals("Body", postInfo.body)
        assertEquals("username", postInfo.name)
        assertEquals(1, postInfo.comments.size)
        assertEquals("Body", postInfo.comments[0].body)
        assertEquals("email", postInfo.comments[0].email)
        assertEquals(1, postInfo.comments[0].id)
        assertEquals("Title", postInfo.comments[0].title)
    }

    private fun getPostInfo(): Post {
        return Post(1, "title", "body", 1)
    }

    private fun getUserList(): List<User> {
        return listOf(User("website", Address("zipcode", Geo(), "suite", "city", "street"), "phone", "name", Company(), 1, "Email", "username"))
    }

    private fun getCommentsList(): List<Comment> {
        return listOf(Comment("title", 1, 1, "body", "Email"))
    }
}
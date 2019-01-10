package com.andresoller.domain.interactors.posts

import com.andresoller.data.model.*
import com.andresoller.data.repositories.PostsRepository
import com.andresoller.domain.entities.PostDetailsInfo
import com.andresoller.domain.entities.PostInfo
import com.andresoller.domain.mappers.postdetail.PostDetailMapper
import com.andresoller.domain.mappers.posts.PostMapper
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


class PostsInteractorImplTest {

    @Mock
    lateinit var postMapper: PostMapper
    @Mock
    lateinit var postDetailMapper: PostDetailMapper
    @Mock
    lateinit var repository: PostsRepository
    @InjectMocks
    lateinit var interactor: PostsInteractorImpl

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getPosts_successfulZip_ReturnPostInfoArrayList() {
        val posts = getPostsList()
        val users = getUserList()
        val postsInfo = getPostInfoList()
        `when`(repository.getPosts()).thenReturn(Observable.just(posts))
        `when`(repository.getUsers()).thenReturn(Observable.just(users))
        `when`(postMapper.mapToEntity(posts, users)).thenReturn(postsInfo)

        interactor.getPosts()
                .test()
                .assertResult(postsInfo)

        verify(repository).getPosts()
        verify(repository).getUsers()
        verify(postMapper).mapToEntity(posts, users)
    }

    @Test
    fun getPostDetail_emptyPosts_ReturnEmptyPostInfoArrayList() {
        val post = Post()
        val users = getUserList()
        val comments = getCommentsList()
        val postDetailsInfo = PostDetailsInfo()
        `when`(repository.getPostDetail("1")).thenReturn(Observable.just(post))
        `when`(repository.getPostComments("1")).thenReturn(Observable.just(comments))
        `when`(repository.getUsers()).thenReturn(Observable.just(users))
        `when`(postDetailMapper.mapToEntity(post, users, comments)).thenReturn(postDetailsInfo)

        interactor.getPostDetail("1")
                .test()
                .assertResult(postDetailsInfo)

        verify(repository).getPostComments("1")
        verify(repository).getPostDetail("1")
        verify(repository).getUsers()
        verify(postDetailMapper).mapToEntity(post, users, comments)
    }

    private fun getPostsList(): ArrayList<Post> {
        return arrayListOf(Post(1, "title", "body", 1))
    }

    private fun getPostInfoList(): ArrayList<PostInfo>? {
        return arrayListOf(PostInfo(1, "title", "body", "address"))
    }

    private fun getPostInfo(): Post {
        return Post(1, "title", "body", 1)
    }

    private fun getUserList(): ArrayList<User> {
        return arrayListOf(User(1, "website", Address("zipcode", Geo(), "suite", "city", "street"), "phone", "name", Company(), "Email", "username"))
    }

    private fun getCommentsList(): ArrayList<Comment> {
        return arrayListOf(Comment(1, "name", 1, "body", "Email"))
    }
}
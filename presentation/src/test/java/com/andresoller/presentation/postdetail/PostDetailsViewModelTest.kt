package com.andresoller.presentation.postdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.andresoller.domain.entities.PostDetailsInfo
import com.andresoller.domain.interactors.posts.PostsInteractor
import com.andresoller.presentation.postdetail.mapper.PostDetailsViewStateMapper
import com.andresoller.presentation.postdetail.viewstates.PartialPostDetailsViewState
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit


class PostDetailsViewModelTest {

    @Mock
    lateinit var interactor: PostsInteractor
    @Mock
    lateinit var mapper: PostDetailsViewStateMapper
    @Mock
    lateinit var disposable: CompositeDisposable

    lateinit var viewModel: PostDetailsViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        val immediate = object : Scheduler() {
            override fun scheduleDirect(@NonNull run: Runnable, delay: Long, @NonNull unit: TimeUnit): Disposable {
                return super.scheduleDirect(run, 0, unit)
            }

            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
            }
        }

        RxJavaPlugins.setInitIoSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { scheduler -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }

        viewModel = PostDetailsViewModel(interactor, mapper, disposable)
    }

    @Test
    fun loadPostDetails_PostIdNotNullNorEmptyAndSuccessfulResponse_displayPostDetailsFetchedState() {
        val postDetailsInfo = PostDetailsInfo("title", "body", "name", 0, arrayListOf())
        `when`(interactor.getPostDetail("1")).thenReturn(Observable.just(postDetailsInfo))
        `when`(mapper.mapToViewState(postDetailsInfo)).thenReturn(PartialPostDetailsViewState.PostDetailsFetchedState(postDetailsInfo))

        viewModel.loadPostDetails("1")

        verify(disposable).add(any(Disposable::class.java))
        verify(interactor).getPostDetail("1")
        assertEquals("body", viewModel.viewState.value?.postDetails?.body)
        assertEquals("title", viewModel.viewState.value?.postDetails?.title)
        assertEquals(postDetailsInfo.comments.size, viewModel.viewState.value?.postDetails?.comments?.size)
        assertEquals(0, viewModel.viewState.value?.postDetails?.commentsCount)
        assertEquals("", viewModel.viewState.value?.errorMessage)
        assertFalse(viewModel.viewState.value?.error!!)
        assertFalse(viewModel.viewState.value?.progress!!)
    }

    @Test
    fun loadPostDetails_PostIdNotNullNorEmptyAndErrorResponse_displayErrorState() {
        `when`(interactor.getPostDetail("1")).thenReturn(Observable.error(Throwable("Error")))

        viewModel.loadPostDetails("1")

        verify(disposable).add(any(Disposable::class.java))
        verify(interactor).getPostDetail("1")
        assertEquals("Error", viewModel.viewState.value?.errorMessage)
        assertTrue(viewModel.viewState.value?.error!!)
        assertFalse(viewModel.viewState.value?.progress!!)
    }

    @Test
    fun loadPostDetails_PostIdNull_displayErrorState() {
        viewModel.loadPostDetails(null)

        verifyZeroInteractions(interactor)
        assertEquals("No results", viewModel.viewState.value?.errorMessage)
        assertTrue(viewModel.viewState.value?.error!!)
        assertFalse(viewModel.viewState.value?.progress!!)
    }

    @Test
    fun collapseDetails_setCollapseDetailsTrue_getCollapseDetailsTrue() {
        viewModel.collapseDetails(true)

        assertTrue(viewModel.collapseDetails.value!!)
    }

    @Test
    fun collapseDetails_setCollapseDetailsFalse_getCollapseDetailsFalse() {
        viewModel.collapseDetails(false)

        assertFalse(viewModel.collapseDetails.value!!)
    }
}
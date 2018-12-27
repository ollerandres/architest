package com.andresoller.presentation.postdetail

import com.andresoller.domain.entities.PostDetailsInfo
import com.andresoller.domain.interactors.posts.PostsInteractor
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit


class PostDetailPresenterTest {

    @Mock
    lateinit var interactor: PostsInteractor
    @Mock
    lateinit var view: PostDetailView
    @Mock
    lateinit var disposable: CompositeDisposable
    @InjectMocks
    lateinit var presenter: PostDetailPresenter

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
    }

    @Test
    fun attachView_viewNull_setView() {
        presenter.attachView(view)

        assertEquals(view, presenter.view)
    }

    @Test
    fun attachView_viewNotNull_noViewInteraction() {
        presenter.attachView(view)

        Mockito.verifyZeroInteractions(view)
    }

    @Test
    fun detachView_setViewNull() {
        presenter.attachView(view)
        assertEquals(view, presenter.view)

        presenter.detachView()
        assertEquals(null, presenter.view)
    }

    @Test
    fun onResume_loadPostDetail() {
        val spiedPresenter = spy(presenter)
        `when`(interactor.getPostDetail("1")).thenReturn(Observable.just(PostDetailsInfo()))

        spiedPresenter.onResume("1")

        verify(spiedPresenter).loadPostDetails("1")
    }

    @Test
    fun onRetry_viewNotNull_clearPostsAndLoadAgain() {
        presenter.attachView(view)
        val spiedPresenter = spy(presenter)
        `when`(interactor.getPostDetail("1")).thenReturn(Observable.just(PostDetailsInfo()))

        spiedPresenter.onRetry("1")

        verify(spiedPresenter).loadPostDetails("1")
    }

    @Test
    fun loadPostDetails_viewNotNullPostIdNotNullNorEmptyAndSuccessfulResponse_startLoadingAndFetchPosts() {
        val postDetailsInfo = PostDetailsInfo()
        presenter.attachView(view)
        `when`(interactor.getPostDetail("1")).thenReturn(Observable.just(postDetailsInfo))

        presenter.loadPostDetails("1")

        verify(disposable).add(any(Disposable::class.java))
        verify(interactor).getPostDetail("1")
        verify(view).stopLoading()
        verify(view).loadPostDetails(postDetailsInfo)
    }

    @Test
    fun loadPostDetails_viewNotNullPostIdNotNullNorEmptyAndErrorResponse_displayErrorResponse() {
        presenter.attachView(view)
        `when`(view.noResultMessage()).thenReturn("No results")

        `when`(interactor.getPostDetail("1")).thenReturn(Observable.error(Throwable("Error")))

        presenter.loadPostDetails("1")

        verify(disposable).add(any(Disposable::class.java))
        verify(interactor).getPostDetail("1")
        verify(view).stopLoading()
        verify(view).displayError("Error")
    }

    @Test
    fun loadPostDetails_viewNullPostIdNotNullNorEmptyAndSuccessfulResponse_noViewInteraction() {
        val postDetailsInfo = PostDetailsInfo()
        `when`(interactor.getPostDetail("1")).thenReturn(Observable.just(postDetailsInfo))

        presenter.loadPostDetails("1")

        verifyZeroInteractions(view)
    }

    @Test
    fun loadPostDetails_viewNotNullAndPostIdNull_displayErrorBanner() {
        presenter.attachView(view)
        `when`(view.noResultMessage()).thenReturn("No results")

        presenter.loadPostDetails(null)

        verify(view).noResultMessage()
        verify(view).displayError("No results")
    }

    @Test
    fun onCommentsScrolled_viewNotNullFirstCompletelyVisibleItemPositionIsZero_expandPost() {
        presenter.attachView(view)

        presenter.onCommentsScrolled(0)

        verify(view).expandPost()
    }

    @Test
    fun onCommentsScrolled_viewNotNullFirstCompletelyVisibleItemPositionIsBiggerThanZero_collapse() {
        presenter.attachView(view)

        presenter.onCommentsScrolled(1)

        verify(view).collapsePost()
    }

    @Test
    fun onCommentsScrolled_viewNotNullFirstCompletelyVisibleItemPositionIsSmallerThanZero_expandPost() {
        presenter.attachView(view)

        presenter.onCommentsScrolled(-1)

        verify(view).expandPost()
    }

    @Test
    fun onPause_dispose() {
        presenter.onPause()

        verify(disposable).clear()
    }
}
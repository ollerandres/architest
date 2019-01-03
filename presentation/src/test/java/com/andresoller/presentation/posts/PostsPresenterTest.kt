package com.andresoller.presentation.posts

import com.andresoller.domain.entities.PostInfo
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

internal class PostsPresenterTest {

    @Mock
    lateinit var interactor: PostsInteractor
    @Mock
    lateinit var view: PostsView
    @Mock
    lateinit var disposable: CompositeDisposable
    @InjectMocks
    lateinit var presenter: PostsPresenter

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
    fun onResume_loadPosts() {
        val spiedPresenter = spy(presenter)
        `when`(interactor.getPosts()).thenReturn(Observable.just(arrayListOf()))

        spiedPresenter.onResume()

        verify(spiedPresenter).loadPosts()
    }

    @Test
    fun onRetry_viewNotNull_clearPostsAndLoadAgain() {
        presenter.attachView(view)
        val spiedPresenter = spy(presenter)
        `when`(interactor.getPosts()).thenReturn(Observable.just(arrayListOf()))

        spiedPresenter.onRetry()

        verify(spiedPresenter).loadPosts()
    }

    @Test
    fun loadPosts_viewNotNullAndEmptySuccessfulResponse_startLoadingStopLoadingAndDisplayErrorBanner() {
        val posts = arrayListOf<PostInfo>()
        presenter.attachView(view)
        `when`(interactor.getPosts()).thenReturn(Observable.just(posts))
        `when`(view.noResultMessage()).thenReturn("No Results")

        presenter.loadPosts()

        verify(disposable).add(any(Disposable::class.java))
        verify(view).startLoading()
        verify(interactor).getPosts()
        verify(view).stopLoading()
        verify(view).noResultMessage()
        verify(view).displayNoResultsError("No Results")
    }

    @Test
    fun loadPosts_viewNotNullAndSuccessfulResponse_startLoadingStopLoadingAndDisplayErrorBanner() {
        val posts = arrayListOf(PostInfo(1, "title", "username", "address"))
        presenter.attachView(view)
        `when`(interactor.getPosts()).thenReturn(Observable.just(posts))

        presenter.loadPosts()

        verify(disposable).add(any(Disposable::class.java))
        verify(view).startLoading()
        verify(interactor).getPosts()
        verify(view).stopLoading()
        verify(view).loadPostsIntent(posts)
    }

    @Test
    fun loadPosts_viewNotNullAndErrorResponse_displayErrorResponse() {
        presenter.attachView(view)
        `when`(view.noResultMessage()).thenReturn("No results")
        `when`(interactor.getPosts()).thenReturn(Observable.error(Throwable("Error")))

        presenter.loadPosts()

        verify(disposable).add(any(Disposable::class.java))
        verify(interactor).getPosts()
        verify(view).stopLoading()
        verify(view).displayErrorBanner("Error")
    }

    @Test
    fun loadPosts_viewNullPostIdNotNullNorEmptyAndSuccessfulResponse_noViewInteraction() {
        `when`(interactor.getPosts()).thenReturn(Observable.just(arrayListOf()))

        presenter.loadPosts()

        verifyZeroInteractions(view)
    }

    @Test
    fun onPause_dispose() {
        presenter.onPause()

        verify(disposable).clear()
    }
}
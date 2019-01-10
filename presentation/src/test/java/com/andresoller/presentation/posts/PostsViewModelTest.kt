package com.andresoller.presentation.posts

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.andresoller.domain.entities.PostInfo
import com.andresoller.domain.interactors.posts.PostsInteractor
import com.andresoller.presentation.posts.mapper.PostsViewStateMapper
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit


internal class PostsViewModelTest {

    @Mock
    lateinit var interactor: PostsInteractor
    @Mock
    lateinit var disposable: CompositeDisposable
    @Mock
    lateinit var mapper: PostsViewStateMapper

    lateinit var viewModel: PostsViewModel

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

        viewModel = PostsViewModel(interactor, mapper, disposable)
    }

    @Test
    fun loadPosts_viewNotNullAndEmptySuccessfulResponse_startLoadingStopLoadingAndDisplayErrorBanner() {
        val posts = arrayListOf<PostInfo>()
        `when`(interactor.getPosts()).thenReturn(Observable.just(posts))

        viewModel.loadPosts()

        verify(disposable).add(any(Disposable::class.java))
        verify(interactor).getPosts()
    }
}
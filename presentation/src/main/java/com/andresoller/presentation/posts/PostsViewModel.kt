package com.andresoller.presentation.posts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.andresoller.domain.interactors.posts.PostsInteractor
import com.andresoller.presentation.posts.mapper.PostsViewStateMapper
import com.andresoller.presentation.posts.viewstates.PartialPostsViewState
import com.andresoller.presentation.posts.viewstates.PostsViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PostsViewModel constructor(private val postsInteractor: PostsInteractor,
                                 private val mapper: PostsViewStateMapper,
                                 private val disposable: CompositeDisposable) : ViewModel() {

    val viewStateMutable = MutableLiveData<PostsViewState>(reduce(PartialPostsViewState.ProgressState))

    fun loadPosts() {
        disposable.add(
                postsInteractor.getPosts()
                        .map { reduce(mapper.mapToViewState(it)) }
                        .startWith(reduce(PartialPostsViewState.ProgressState))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            viewStateMutable.value = it
                        }, {
                            viewStateMutable.value = reduce(PartialPostsViewState.ErrorState(it.message.toString()))
                        }))
    }

    private fun reduce(partialState: PartialPostsViewState): PostsViewState {
        return when (partialState) {
            is PartialPostsViewState.ProgressState -> PostsViewState(progress = true)
            is PartialPostsViewState.ErrorState -> PostsViewState(error = true, errorMessage = partialState.errorMessage)
            is PartialPostsViewState.PostsFetchedState -> PostsViewState(posts = partialState.posts)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}

class PostsViewModelFactory @Inject constructor(private val postsViewModel: PostsViewModel) :
        ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return postsViewModel as T
    }
}
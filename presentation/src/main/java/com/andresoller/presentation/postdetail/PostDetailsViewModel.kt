package com.andresoller.presentation.postdetail;

import androidx.lifecycle.*
import com.andresoller.domain.interactors.posts.PostsInteractor
import com.andresoller.presentation.postdetail.mapper.PostDetailsViewStateMapper
import com.andresoller.presentation.postdetail.viewstates.PartialPostDetailsViewState
import com.andresoller.presentation.postdetail.viewstates.PostDetailsViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PostDetailsViewModel @Inject constructor(private val interactor: PostsInteractor,
                                               private val mapper: PostDetailsViewStateMapper,
                                               private val disposable: CompositeDisposable) : ViewModel() {

    val viewState = MutableLiveData<PostDetailsViewState>(PostDetailsViewState(true))
    val collapseDetails = MutableLiveData<Boolean>()
    var collapseDetailsLiveData: LiveData<PostDetailsViewState> = Transformations.map(collapseDetails) {
        reduce(PartialPostDetailsViewState.PostCollapsingState(it!!))
    }

    fun loadPostDetails(postId: String?) {
        if (postId.isNullOrBlank()) {
            viewState.value = reduce(PartialPostDetailsViewState.ErrorState("No results"))
            return
        }

        disposable.add(
                interactor.getPostDetail(postId)
                        .map { reduce(mapper.mapToViewState(it)) }
                        .startWith(reduce(PartialPostDetailsViewState.ProgressState))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            viewState.value = it
                        }, {
                            viewState.value = reduce(PartialPostDetailsViewState.ErrorState(it.message.toString()))
                        }))
    }

    fun collapseDetails(collapse: Boolean) {
        collapseDetails.value = collapse
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    private fun reduce(partialState: PartialPostDetailsViewState): PostDetailsViewState {
        if (viewState.value == null) {
            return PostDetailsViewState(error = true, errorMessage = "Error")
        }
        return when (partialState) {
            is PartialPostDetailsViewState.ProgressState -> PostDetailsViewState(progress = true)
            is PartialPostDetailsViewState.ErrorState -> PostDetailsViewState(error = true, errorMessage = partialState.errorMessage)
            is PartialPostDetailsViewState.PostDetailsFetchedState -> PostDetailsViewState(collapsePost = viewState.value!!.collapsePost, postDetails = partialState.postdetails)
            is PartialPostDetailsViewState.PostCollapsingState -> PostDetailsViewState(collapsePost = partialState.collapsePost, postDetails = viewState.value!!.postDetails)
        }
    }
}

class PostDetailsViewModelFactory @Inject constructor(private val postDetailsViewModel: PostDetailsViewModel) :
        ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return postDetailsViewModel as T
    }
}
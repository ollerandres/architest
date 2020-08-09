package com.andresoller.presentation.posts

import androidx.paging.PagingSource
import com.andresoller.domain.entities.PostInfo
import com.andresoller.domain.interactors.posts.PostsInteractor
import com.andresoller.domain.result.ArchitestResult

class PostsPagingSource(private val postsInteractor: PostsInteractor) : PagingSource<Int, PostInfo>() {
    override suspend fun load(
            params: LoadParams<Int>
    ): LoadResult<Int, PostInfo> {
        val nextPageNumber = params.key ?: 1

        return when (val response = postsInteractor.getPosts()) {
            is ArchitestResult.Success -> LoadResult.Page(
                    data = response.result,
                    prevKey = null, // Only paging forward.
                    nextKey = 1
            )
            is ArchitestResult.Error -> TODO()
        }
    }
}
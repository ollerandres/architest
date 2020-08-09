package com.andresoller.domain.result

import com.andresoller.domain.error.ErrorEntity

sealed class ArchitestResult<T> {
    class Success<T>(val result: T) : ArchitestResult<T>()
    class Error<T>(val error: ErrorEntity) : ArchitestResult<T>()
}

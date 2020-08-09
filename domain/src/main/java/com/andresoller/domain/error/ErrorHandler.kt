package com.andresoller.domain.error

interface ErrorHandler {

    fun mapError(throwable: Throwable): ErrorEntity

    fun trackError(throwable: Throwable)
}

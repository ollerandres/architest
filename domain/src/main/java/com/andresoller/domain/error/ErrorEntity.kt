package com.andresoller.domain.error

sealed class ErrorEntity {
    object NetworkError : ErrorEntity()
    data class HttpError(val code: Int) : ErrorEntity()
    object ParsingError : ErrorEntity()
    object UnknownError : ErrorEntity()
}

package com.andresoller.data.remote.errors

import com.andresoller.domain.error.ErrorEntity
import com.andresoller.domain.error.ErrorEntity.*
import com.andresoller.domain.error.ErrorHandler
import com.google.gson.JsonParseException
import retrofit2.HttpException
import java.io.IOException

internal class ErrorHandlerImpl : ErrorHandler {
    override fun mapError(throwable: Throwable): ErrorEntity {
        return when (throwable) {
            is IOException -> NetworkError
            is HttpException -> HttpError(throwable.code())
            is JsonParseException -> ParsingError
            else -> UnknownError
        }
    }

    /**
     * Tracks error to the different tracking systems.
     */
    override fun trackError(throwable: Throwable) {
        // NO OP.

        // TODO Write tracking logic for different kinds of throwable here of what and when you want to track.
        //  If more than one tracking system is needed (Google analytics, firebase, etc...), depending on the business,
        //  then this function can be refactored into a better solution
    }
}

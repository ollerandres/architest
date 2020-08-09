package com.andresoller.data.remote.errors

import com.andresoller.domain.error.ErrorHandler
import com.andresoller.domain.result.ArchitestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class SafeFunctionExecutorImpl(private val errorHandler: ErrorHandler) :
        SafeFunctionExecutor {

    /**
     * Method that ensures that a suspend function that returns T is done
     * in the IO Dispatcher and that any possible error is handled correctly
     *
     * @param function service call that also is mapped to <T>
     *
     * @return OVResult<T> result of type T where T is the return type of the function to execute
     */
    @Suppress("TooGenericExceptionCaught")
    override suspend fun <T> executeSafeFunction(function: suspend () -> T): ArchitestResult<T> {
        return try {
            withContext(Dispatchers.IO) {
                ArchitestResult.Success(function())
            }
        } catch (throwable: Throwable) {
            errorHandler.trackError(throwable)
            ArchitestResult.Error(errorHandler.mapError(throwable))
        }
    }

    /**
     * Method that ensures that a suspend function that returns T is done
     * in the IO Dispatcher and that any possible error is handled correctly by using the fallback
     * if not null otherwise returning a correctly mapped error.
     *
     * @param function service call that also is mapped to <T>
     * @param fallback Optional T to fallback to when an error occurs while executing the function
     *
     * @return OVResult<T> result of type T where T is the return type of the function to execute
     */
    override suspend fun <T> executeSafeFunctionWithFallback(
            fallback: T?,
            function: suspend () -> T
    ): ArchitestResult<T> {
        return try {
            withContext(Dispatchers.IO) {
                ArchitestResult.Success(function())
            }
        } catch (throwable: Throwable) {
            errorHandler.trackError(throwable)
            fallback?.let { ArchitestResult.Success(it) }
                    ?: ArchitestResult.Error(errorHandler.mapError(throwable))
        }
    }
}
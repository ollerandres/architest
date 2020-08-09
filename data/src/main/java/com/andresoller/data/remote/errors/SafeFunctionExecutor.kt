package com.andresoller.data.remote.errors

import com.andresoller.domain.result.ArchitestResult

interface SafeFunctionExecutor {
    suspend fun <T> executeSafeFunction(function: suspend () -> T): ArchitestResult<T>

    suspend fun <T> executeSafeFunctionWithFallback(
            fallback: T?,
            function: suspend () -> T
    ): ArchitestResult<T>
}
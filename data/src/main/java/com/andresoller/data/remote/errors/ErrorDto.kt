package com.andresoller.data.remote.errors

internal data class ErrorDto(
        val code: Int,
        val message: String,
        val timestamp: Long)
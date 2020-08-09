package com.andresoller.data.remote.errors

internal data class ArchitestDto<T>(val error: ErrorDto?, val result: ArchitestResultDto<T>?)

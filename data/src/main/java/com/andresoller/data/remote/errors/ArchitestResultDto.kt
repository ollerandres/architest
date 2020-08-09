package com.andresoller.data.remote.errors

interface ArchitestResultDto<T> {
    fun mapToDomain(): T
}

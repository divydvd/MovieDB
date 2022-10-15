package com.divy.moviedb

import androidx.annotation.NonNull

class ApiResponseGeneric<T> private constructor(
    val status: Status,
    val data: T?,
    val error: Throwable?
) {

    companion object {
        fun loading(): ApiResponseGeneric<Any> {
            return ApiResponseGeneric(Status.LOADING, null, null)
        }

        fun success(data: Any?): ApiResponseGeneric<Any> {
            return ApiResponseGeneric(Status.SUCCESS, data, null)
        }

        fun error(@NonNull error: Throwable): ApiResponseGeneric<Any> {
            return ApiResponseGeneric(Status.ERROR, null, error)
        }
    }
}
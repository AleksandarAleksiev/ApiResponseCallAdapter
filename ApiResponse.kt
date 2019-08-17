package com.aaleksiev.starling.network.response

import retrofit2.Response

@Suppress("unused") // T is used in extending classes
sealed class ApiResponse<T> {
    companion object {
        @JvmStatic
        val TAG: String = ApiResponse::class.java.simpleName
        @JvmStatic
        val HTTP_OK: Int = 200
        @JvmStatic
        val HTTP_NO_CONTENT: Int = 204
        @JvmStatic
        val HTTP_REDIRECTION: Int = 300
        @JvmStatic
        val HTTP_SERVER_ERROR: Int = 500
        @JvmStatic
        val NOT_FOUND_ERROR_MESSAGE = "Not found"

        @JvmStatic
        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            return ApiErrorResponse(error.message ?: "unknown error")
        }

        @JvmStatic
        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == HTTP_NO_CONTENT) {
                    ApiEmptyResponse()
                } else {
                    ApiSuccessResponse(body = body)
                }
            } else {
                val msg = response.errorBody()?.string()
                val errorMsg = if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                ApiErrorResponse(errorMsg ?: "unknown error", response.code())
            }
        }
    }
}

class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiSuccessResponse<T>(val body: T) : ApiResponse<T>()

data class ApiErrorResponse<T>(val errorMessage: String, val responseCode: Int = HTTP_SERVER_ERROR) : ApiResponse<T>()
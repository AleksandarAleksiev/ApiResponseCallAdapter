package com.aaleksiev.starling.network.response

import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ApiResponseCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        val returnObjectType = getRawType(returnType)
        if (returnObjectType != ApiResponse::class.java) {
            return null
        }
        if (returnType !is ParameterizedType) {
            throw IllegalArgumentException("resource must be parameterized")
        }
        if (returnType.actualTypeArguments.size < 0) {
            throw IllegalArgumentException("resource must have at least one parameter")
        }
        val observableType = getParameterUpperBound(0, returnType)
        val rawObservableType = getRawType(observableType)

        val unexpectedType = when (val argumentType = returnType.actualTypeArguments[0]) {
            is ParameterizedType -> rawObservableType != argumentType.rawType
            else -> rawObservableType != argumentType
        }
        if (unexpectedType) {
            throw IllegalArgumentException("type must be a resource")
        }
        return ApiResponseCallAdapter<Any>(observableType)
    }
}
package com.aaleksiev.starling.network.response

import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class ApiResponseCallAdapter<R>(private val responseType: Type) : CallAdapter<R, ApiResponse<R>> {

    override fun responseType() = responseType

    override fun adapt(call: Call<R>): ApiResponse<R> {
        return try {
            ApiResponse.create<R>(call.execute())
        } catch (throwable: Throwable){
            ApiResponse.create(throwable)
        }
    }
}
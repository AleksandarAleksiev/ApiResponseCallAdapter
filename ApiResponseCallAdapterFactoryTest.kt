package com.aaleksiev.starling.network.response

import com.aaleksiev.starling.core.TypeReference
import com.aaleksiev.starling.network.TestBase
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Retrofit

class ApiResponseCallAdapterFactoryTest : TestBase() {

    private val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://test.com/").build()
    private val underTest = ApiResponseCallAdapterFactory()

    @Test(expected = IllegalArgumentException::class)
    fun willThrowExceptionWhenReturnTypeIsNotGeneric() {
        underTest.get(
            returnType = ApiResponse::class.java,
            annotations = emptyArray(),
            retrofit = retrofit
        )
    }

    @Test
    fun willReturnNullWhenReturnTypeIsNotApiResponse() {
        val result = underTest.get(
            returnType = String::class.java,
            annotations = emptyArray(),
            retrofit = retrofit
        )

        assertNull(result)
    }

    @Test
    fun willCreateCallAdapter() {
        val expectedType = ApiResponseCallAdapter::class.java
        val apiResponse = object : TypeReference<ApiResponse<String>>(){}
        val result = underTest.get(
            returnType = apiResponse.type,
            annotations = emptyArray(),
            retrofit = retrofit
        )

        assertNotNull(result)
        assertEquals(expectedType, result!!::class.java)
    }
}
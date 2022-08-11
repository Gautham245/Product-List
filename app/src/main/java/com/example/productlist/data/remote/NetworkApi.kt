package com.example.productlist.data.remote

import com.example.productlist.data.remote.dto.ProductResponse
import retrofit2.Response
import retrofit2.http.GET

interface NetworkApi {

    @GET("api/v2/Products")
    suspend fun getProducts(): Response<ProductResponse>
}
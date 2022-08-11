package com.example.productlist.repo

import com.example.productlist.data.local.entity.ProductEntity
import com.example.productlist.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProduct(): Flow<Resource<List<ProductEntity>>>
    suspend fun updateFav(id: Int, isFav: Boolean)
    suspend fun getProductFromDB(): Flow<Resource<List<ProductEntity>>>
}
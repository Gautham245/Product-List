package com.example.productlist.repo

import com.example.productlist.data.local.ProductDao
import com.example.productlist.data.local.entity.ProductEntity
import com.example.productlist.data.remote.NetworkApi
import com.example.productlist.utils.AppConstant
import com.example.productlist.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class ProductRepositoryImpl @Inject constructor(
    private val api: NetworkApi,
    private val dao: ProductDao,
) : ProductRepository {

    var products: List<ProductEntity>? = mutableListOf()

    override suspend fun getProduct(): Flow<Resource<List<ProductEntity>>> {
        return flow {
            try {
                products = dao.getAllProduct()

                if (!products.isNullOrEmpty())
                    emit(Resource.Success(products!!))

                val response = api.getProducts()
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    val products = result.products.map { it.toProduct() }
                    products.forEach { product ->
                        dao.insertProduct(product)
                    }
                    val productList = dao.getAllProduct()
                    productList?.let {
                        emit(Resource.Success(productList))
                    }
                } else
                    emit(Resource.Error(response.message() + " code : " + response.code()
                        .toString()))

            } catch (e: HttpException) {
                if (products.isNullOrEmpty())
                    emit(Resource.Error(AppConstant.OFFLINE))
                else
                    emit(Resource.Success(products!!))

            } catch (e: IOException) {
                if (products.isNullOrEmpty())
                    emit(Resource.Error(AppConstant.OFFLINE))
                else
                    emit(Resource.Success(products!!))
            }
        }
    }


    override suspend fun updateFav(id: Int, isFav: Boolean) {
        dao.updateFavourite(id, isFav)
    }

    override suspend fun getProductFromDB(): Flow<Resource<List<ProductEntity>>> {
        return flow {
            try {
                val products = dao.getAllProduct()
                products?.let {
                    if (products.size > 0)
                        emit(Resource.Success(products))
                    else
                        emit(Resource.Error(AppConstant.OFFLINE))
                }

            } catch (e: IOException) {
                emit(Resource.Error(AppConstant.OFFLINE))
            }
        }
    }
}
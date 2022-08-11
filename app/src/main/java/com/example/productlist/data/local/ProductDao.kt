package com.example.productlist.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.productlist.data.local.entity.ProductEntity

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertProduct(productEntity: ProductEntity)

    @Query("SELECT * FROM ProductEntity")
    suspend fun getAllProduct(): List<ProductEntity>?

    @Query("UPDATE ProductEntity SET favourite = :favourite WHERE id = :id")
    suspend fun updateFavourite(id: Int, favourite: Boolean)

}
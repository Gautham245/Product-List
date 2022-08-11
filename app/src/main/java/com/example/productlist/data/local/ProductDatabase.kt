package com.example.productlist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.productlist.data.local.entity.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 1
)
abstract class ProductDatabase : RoomDatabase() {
    abstract val productDao: ProductDao
}
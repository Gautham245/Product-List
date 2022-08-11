package com.example.productlist.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.productlist.utils.model.Product


@Entity
data class ProductEntity(
    val brand: String,
    val category: String,
    val description: String,
    @PrimaryKey
    val id: Int,
    val images: String,
    val price: Int,
    val rating: Double,
    val title: String,
    val favourite: Boolean = false,
) {
    fun toProduct(): Product {
        return Product(
            brand = brand,
            category = category,
            description = description,
            id = id,
            images = images,
            price = price,
            rating = rating,
            title = title
        )
    }
}

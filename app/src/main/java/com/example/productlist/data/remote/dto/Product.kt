package com.example.productlist.data.remote.dto

import com.example.productlist.data.local.entity.ProductEntity

data class Product(
    val brand: String,
    val category: String,
    val description: String,
    val discountPercentage: Double,
    val id: Int,
    val images: List<String>,
    val price: Int,
    val rating: Double,
    val stock: Int,
    val thumbnail: String,
    val title: String,
) {
    fun toProduct(): ProductEntity {
        return ProductEntity(
            brand = brand,
            category = category,
            description = description,
            id = id,
            images = images.get(0),
            price = price,
            rating = rating,
            title = title
        )
    }
}
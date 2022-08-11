package com.example.productlist.utils.model

data class Product(
    val brand: String,
    val category: String,
    val description: String,
    val id: Int,
    val images: String,
    val price: Int,
    val rating: Double,
    val title: String,
    val favourite : Boolean = false
)
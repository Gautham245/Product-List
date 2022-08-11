package com.example.productlist.utils

import com.example.productlist.data.local.entity.ProductEntity

sealed class UiEvent{
    class Success(val result: List<ProductEntity>?) : UiEvent()
    class Failure(val errorText: String) : UiEvent()
    object Loading : UiEvent()
    object Empty : UiEvent()
}

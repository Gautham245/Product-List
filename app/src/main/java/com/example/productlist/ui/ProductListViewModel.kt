package com.example.productlist.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productlist.repo.ProductRepository
import com.example.productlist.utils.AppConstant
import com.example.productlist.utils.Resource
import com.example.productlist.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(private val repository: ProductRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow<UiEvent>(UiEvent.Empty)
    val uiState: StateFlow<UiEvent> = _uiState

    fun getProductList() {
        _uiState.value = UiEvent.Loading
        viewModelScope.launch(Dispatchers.IO) {
            repository.getProduct().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let {
                            _uiState.value = UiEvent.Success(result.data)
                        }
                    }
                    is Resource.Error -> {
                        if (result.message.isNullOrEmpty()) {
                            _uiState.value = UiEvent.Failure(AppConstant.SOME_THING_WENT_WRONG)
                        } else {
                            _uiState.value = UiEvent.Failure(result.message.toString())
                        }
                    }
                }
            }
        }
    }

    fun updateFavouriteStatus(id: Int, isFavourite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateFav(id, isFavourite)
            repository.getProductFromDB().collect{ result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let {
                            _uiState.value = UiEvent.Success(result.data)
                        }
                    }
                    is Resource.Error -> {
                        if (result.message.isNullOrEmpty()) {
                            _uiState.value = UiEvent.Failure(AppConstant.SOME_THING_WENT_WRONG)
                        } else {
                            _uiState.value = UiEvent.Failure(result.message.toString())
                        }
                    }
                }
            }
        }
    }
}
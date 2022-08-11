package com.example.productlist.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.productlist.R
import com.example.productlist.data.local.entity.ProductEntity
import com.example.productlist.databinding.ActivityMainBinding
import com.example.productlist.utils.UiEvent
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: ProductListViewModel by viewModels()
    lateinit var activityMainBinding: ActivityMainBinding
    lateinit var productAdapter: ProductAdapter
    lateinit var productList: List<ProductEntity>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        setUpRecyclerView(this)

        //Get Product list
        viewModel.getProductList()

        lifecycleScope.launch {
            viewModel.uiState.collect { state ->

                when (state) {
                    is UiEvent.Empty -> {
                        stopLoading()
                    }

                    is UiEvent.Loading -> {
                        showLoading()
                    }

                    is UiEvent.Success -> {
                        state.result?.let { result ->
                            productList = result
                            setAdapter(productList)
                        }
                        stopLoading()
                    }
                    is UiEvent.Failure -> {
                        stopLoading()
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            state.errorText,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun setAdapter(products: List<ProductEntity>?) {
        if (products.isNullOrEmpty() && this::productList.isInitialized) {
            productAdapter.submitList(productList)
        } else {
            productAdapter.submitList(products)
        }

    }

    private fun showLoading() {
        activityMainBinding.progressBar.isVisible = true
    }

    private fun stopLoading() {
        activityMainBinding.progressBar.isVisible = false
    }

    private fun setUpRecyclerView(context: Context) {
        productAdapter = ProductAdapter(viewModel, context)
        activityMainBinding.rvProductList.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        menu?.let {
            val item = menu.findItem(R.id.action_search)
            val searchView: SearchView = item.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filter(newText)
                    return true
                }

            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    fun filter(query: String?) {
        lifecycleScope.launch(Dispatchers.Default) {
            val list = mutableListOf<ProductEntity>()

            if (!query.isNullOrEmpty() && this@MainActivity::productList.isInitialized) {
                list.addAll(
                    productList.filter {
                        it.title.lowercase().contains(query.toString().lowercase())
                    }
                )
            }
            withContext(Dispatchers.Main) {
                setAdapter(list)
            }
        }

    }
}
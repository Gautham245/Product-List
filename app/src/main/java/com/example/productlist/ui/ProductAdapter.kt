package com.example.productlist.ui


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.productlist.R
import com.example.productlist.data.local.entity.ProductEntity
import com.example.productlist.databinding.ProductItemBinding

class ProductAdapter(
    private val viewModel: ProductListViewModel,
    private val context: Context,
) :
    ListAdapter<ProductEntity, ProductAdapter.ProductViewHolder>(Companion) {


    companion object : DiffUtil.ItemCallback<ProductEntity>() {
        override fun areItemsTheSame(
            oldItem: ProductEntity,
            newItem: ProductEntity,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProductEntity,
            newItem: ProductEntity,
        ): Boolean {
            return oldItem == newItem
        }
    }


    inner class ProductViewHolder(val productItemBinding: ProductItemBinding) :
        RecyclerView.ViewHolder(productItemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProductViewHolder(ProductItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val productItem = getItem(position)
        holder.productItemBinding.apply {
            product = productItem
        }
        val favourite = holder.productItemBinding.favourite
        favourite.apply {
            if (productItem.favourite)
                setImageResource(R.drawable.ic_baseline_favorite_24)
            else
                setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }

        Glide.with(context)
            .load(productItem.images)
            .into(holder.productItemBinding.productImage)

        favourite.setOnClickListener {
            if (productItem.favourite) {
                favourite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                viewModel.updateFavouriteStatus(productItem.id, false)
            } else {
                favourite.setImageResource(R.drawable.ic_baseline_favorite_24)
                viewModel.updateFavouriteStatus(productItem.id, true)
            }
        }

    }
}
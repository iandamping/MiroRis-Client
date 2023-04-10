package com.spe.miroris.feature.addProduct.exclude

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.spe.miroris.databinding.ItemMiniImageAddProductBinding

class AdapterAddProductImage(private val listener: AdapterAddProductImageListener) :
    ListAdapter<MiniImage, ViewHolderAddProductImage>(miniImageAdapterCallback) {

    companion object {
        val miniImageAdapterCallback = object : DiffUtil.ItemCallback<MiniImage>() {
            override fun areItemsTheSame(oldItem: MiniImage, newItem: MiniImage): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: MiniImage,
                newItem: MiniImage
            ): Boolean {
                return oldItem.image == newItem.image
            }
        }
    }

    interface AdapterAddProductImageListener {
        fun onClicked(data: MiniImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAddProductImage {
        return ViewHolderAddProductImage(
            binding = ItemMiniImageAddProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolderAddProductImage, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
            holder.itemView.setOnClickListener {
                listener.onClicked(data)
            }
        }
    }
}
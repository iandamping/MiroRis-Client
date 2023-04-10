package com.spe.miroris.feature.addProduct.exclude

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.spe.miroris.databinding.ItemMiniImageAddProductBinding

class ViewHolderAddProductImage(private val binding: ItemMiniImageAddProductBinding) :
    ViewHolder(binding.root) {

    fun bind(data: MiniImage) {
        binding.ivMiniAddProduct.load(data.image)
    }
}
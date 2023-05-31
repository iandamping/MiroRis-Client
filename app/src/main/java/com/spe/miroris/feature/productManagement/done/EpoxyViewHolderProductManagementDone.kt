package com.spe.miroris.feature.productManagement.done

import coil.load
import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant
import com.spe.miroris.databinding.ItemProductDoneBinding
import com.spe.miroris.util.ViewBindingEpoxyModelWithHolder

class EpoxyViewHolderProductManagementDone(
    private val data: MultiAdapterProductManagementDoneData.ProductDone,
    private val rootOnClick: (String) -> Unit,
) :
    ViewBindingEpoxyModelWithHolder<ItemProductDoneBinding>() {
    override fun ItemProductDoneBinding.bind() {
        tvItemProductTitle.text = data.productName
        tvItemProductDesc.text = data.productDetail
        ivProductActive.load("${NetworkConstant.BASE_URL}${data.productImage}")


        root.setOnClickListener {
            rootOnClick.invoke(data.productId)
        }
    }

    override fun getDefaultLayout(): Int {
        return R.layout.item_product_done
    }
}
package com.spe.miroris.feature.productManagement.ongoing

import coil.load
import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant
import com.spe.miroris.databinding.ItemProductOngoingBinding
import com.spe.miroris.util.ViewBindingEpoxyModelWithHolder

class EpoxyViewHolderProductManagementOnGoing(
    private val data: MultiAdapterProductManagementOnGoingData.ProductOnGoing,
    private val rootOnClick: (String) -> Unit,
) :
    ViewBindingEpoxyModelWithHolder<ItemProductOngoingBinding>() {
    override fun ItemProductOngoingBinding.bind() {
        tvItemProductTitle.text = data.productName
        tvItemProductDesc.text = data.productDetail
        ivProductActive.load("${NetworkConstant.BASE_URL}${data.productImage}")



        root.setOnClickListener {
            rootOnClick.invoke(data.productId)
        }
    }

    override fun getDefaultLayout(): Int {
        return R.layout.item_product_ongoing
    }
}
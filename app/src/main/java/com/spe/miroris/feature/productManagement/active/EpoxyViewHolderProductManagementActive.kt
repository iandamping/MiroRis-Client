package com.spe.miroris.feature.productManagement.active

import android.view.View
import coil.load
import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant
import com.spe.miroris.databinding.ItemProductActiveBinding
import com.spe.miroris.util.ViewBindingEpoxyModelWithHolder

class EpoxyViewHolderProductManagementActive(
    private val data: MultiAdapterProductManagementActiveData.ProductActive,
    private val rootOnClick: (String) -> Unit,
    private val deleteOnClick: (String) -> Unit,
    private val editOnClick: (MultiAdapterProductManagementActiveData.ProductActive) -> Unit,
    private val qrOnClick: (String) -> Unit,
) :
    ViewBindingEpoxyModelWithHolder<ItemProductActiveBinding>() {
    override fun ItemProductActiveBinding.bind() {
        if (data.productType == "2") {
            btnProductQr.visibility = View.VISIBLE
        } else {
            btnProductQr.visibility = View.INVISIBLE
        }
        tvItemProductTitle.text = data.productName
        tvItemProductDesc.text = data.productDetail

        ivProductActive.load("${NetworkConstant.BASE_URL}${data.productImage}")

        btnProductDelete.setOnClickListener {
            deleteOnClick.invoke(data.productId)
        }
        btnProductEdit.setOnClickListener {
            editOnClick.invoke(data)
        }

        btnProductQr.setOnClickListener {
            qrOnClick.invoke(data.productId)
        }

        root.setOnClickListener {
            rootOnClick.invoke(data.productId)
        }
    }

    override fun getDefaultLayout(): Int {
        return R.layout.item_product_active
    }
}
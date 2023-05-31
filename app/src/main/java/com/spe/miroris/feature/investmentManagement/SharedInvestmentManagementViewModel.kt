package com.spe.miroris.feature.investmentManagement

import androidx.lifecycle.ViewModel
import com.spe.miroris.core.data.dataSource.remote.model.response.InvestmentResponse
import com.spe.miroris.feature.investmentManagement.ongoing.InvestmentOnGoingProduct
import com.spe.miroris.feature.investmentManagement.ongoing.MultiAdapterInvestmentData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class SharedInvestmentManagementViewModel : ViewModel() {

    private val _listInvestmentData: MutableStateFlow<MutableList<MultiAdapterInvestmentData>> =
        MutableStateFlow(
            mutableListOf(
                MultiAdapterInvestmentData.Shimmer(0),
                MultiAdapterInvestmentData.Shimmer(1),
                MultiAdapterInvestmentData.Shimmer(2),
            )
        )

    val listInvestmentData: StateFlow<List<MultiAdapterInvestmentData>> =
        _listInvestmentData.asStateFlow()

//    private val _sharedInvestmentData: MutableStateFlow<List<InvestmentResponse>> =
//        MutableStateFlow(emptyList())
//    val sharedInvestmentData: StateFlow<List<InvestmentResponse>> =
//        _sharedInvestmentData.asStateFlow()

    suspend fun setInvestmentData(data: List<InvestmentResponse>) {
        if (data.isEmpty()) {
            _listInvestmentData.value = mutableListOf(MultiAdapterInvestmentData.Error)
        } else {
            val listInvestment = withContext(Dispatchers.Default) {
                val listOfId = List(data.size) { it + 1 }
                data.zip(listOfId).map {
                    InvestmentOnGoingProduct(
                        epoxyId = it.second,
                        productName = it.first.productName ?: "",
                        productDetail = it.first.productDetail ?: "",
                        productQris = it.first.productQris ?: "",
                        productPaid = it.first.productPaid ?: "",
                        productStatus = it.first.productStatus ?: "",
                        paymentAmount = it.first.paymentAmount ?: ""
                    )
                }.toMutableList()
            }

            _listInvestmentData.update {
                _listInvestmentData.value.toMutableList().apply {
                    this.clear()
                    listInvestment.forEach {
                        this.add(
                            MultiAdapterInvestmentData.Investment(
                                epoxyId = it.epoxyId,
                                productName = it.productName,
                                productDetail = it.productDetail,
                                productQris = it.productQris,
                                productPaid = it.productPaid,
                                productStatus = it.productStatus,
                                paymentAmount = it.paymentAmount
                            )
                        )
                    }
                }
            }
        }


    }
}
package com.spe.miroris.feature.editProduct

import androidx.lifecycle.ViewModel
import com.spe.miroris.feature.editProduct.adapter.MultiAdapterEditProductData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SharedEditProductViewModel : ViewModel() {

    private val _listMiniImageData: MutableStateFlow<MutableList<MultiAdapterEditProductData>> =
        MutableStateFlow(
            mutableListOf(
                MultiAdapterEditProductData.Footer,
            )
        )

    val listMiniImageData: StateFlow<MutableList<MultiAdapterEditProductData>> =
        _listMiniImageData.asStateFlow()


    fun setMiniImageData(data: MultiAdapterEditProductData) {
        _listMiniImageData.update {
            _listMiniImageData.value.toMutableList().apply {
                when {
                    this[0] == MultiAdapterEditProductData.Footer -> {
                        this.removeFirst()
                        this.add(data)
                        this.add(MultiAdapterEditProductData.Footer)
                    }

                    this[1] == MultiAdapterEditProductData.Footer -> {
                        this.removeAt(1)
                        this.add(data)
                        this.add(MultiAdapterEditProductData.Footer)
                    }

                    this[2] == MultiAdapterEditProductData.Footer -> {
                        this.removeAt(2)
                        this.add(data)
                        this.add(MultiAdapterEditProductData.Footer)
                    }

                    this[3] == MultiAdapterEditProductData.Footer -> {
                        this.removeAt(3)
                        this.add(data)
                        this.add(MultiAdapterEditProductData.Footer)
                    }

                    this[4] == MultiAdapterEditProductData.Footer -> {
                        this.removeAt(4)
                        this.add(data)
                    }

                    this[5] == MultiAdapterEditProductData.Footer -> {
                        this.removeAt(5)
                    }
                }

            }
        }
    }

    fun deleteMiniImageData(position: Int) {
        _listMiniImageData.update {
            _listMiniImageData.value.toMutableList().apply {
                removeAt(position)
                if (this.size == 4) {
                    if (this[3] != MultiAdapterEditProductData.Footer) {
                        add(4, MultiAdapterEditProductData.Footer)
                    }
                }
            }
        }

    }
}
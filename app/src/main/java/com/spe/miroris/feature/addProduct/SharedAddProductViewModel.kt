package com.spe.miroris.feature.addProduct

import androidx.lifecycle.ViewModel
import com.spe.miroris.feature.addProduct.adapter.MultiAdapterData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SharedAddProductViewModel : ViewModel() {

    private val _listMiniImageData: MutableStateFlow<MutableList<MultiAdapterData>> =
        MutableStateFlow(
            mutableListOf(
                MultiAdapterData.Footer,
            )
        )

    val listMiniImageData: StateFlow<MutableList<MultiAdapterData>> =
        _listMiniImageData.asStateFlow()


    fun setMiniImageData(data: MultiAdapterData) {
        _listMiniImageData.update {
            _listMiniImageData.value.toMutableList().apply {
                when {
                    this[0] == MultiAdapterData.Footer -> {
                        this.removeFirst()
                        this.add(data)
                        this.add(MultiAdapterData.Footer)
                    }
                    this[1] == MultiAdapterData.Footer -> {
                        this.removeAt(1)
                        this.add(data)
                        this.add(MultiAdapterData.Footer)
                    }
                    this[2] == MultiAdapterData.Footer -> {
                        this.removeAt(2)
                        this.add(data)
                        this.add(MultiAdapterData.Footer)
                    }
                    this[3] == MultiAdapterData.Footer -> {
                        this.removeAt(3)
                        this.add(data)
                        this.add(MultiAdapterData.Footer)
                    }
                    this[4] == MultiAdapterData.Footer -> {
                        this.removeAt(4)
                        this.add(data)
                    }
                    this[5] == MultiAdapterData.Footer -> {
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
                    if (this[3] != MultiAdapterData.Footer) {
                        add(4, MultiAdapterData.Footer)
                    }
                }
            }
        }

    }


}
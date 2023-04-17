package com.spe.miroris.feature.home

import androidx.lifecycle.ViewModel
import com.spe.miroris.feature.home.banner.DummyBanner
import com.spe.miroris.feature.home.banner.MultiAdapterBannerData
import com.spe.miroris.feature.home.state.BannerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber

class HomeViewModel : ViewModel() {

    //service
    val listImage: MutableList<Int> = mutableListOf(1, 1, 1, 1, 1, 1, 1, 1)


    init {
        //local buat sendiri
        val listOfId = List(listImage.size) { it + 1 }
        //mapping it
        val listOf: MutableList<DummyBanner> = listImage.zip(listOfId).map {
            DummyBanner(imageId = it.first, id = it.second)
        }.toMutableList()

        Timber.e("map : $listOf")

    }

    private val _bannerUiState: MutableStateFlow<BannerUiState> =
        MutableStateFlow(BannerUiState.initialize())
    val bannerUiState: StateFlow<BannerUiState> = _bannerUiState.asStateFlow()


//    fun setBannerData(dummyBannerData: List<DummyBanner>) {
//        _bannerUiState.update { currentUiState ->
//            currentUiState.copy(isLoading = false, data = dummyBannerData)
//        }
//    }

    fun setBannerData(listOfImage: List<Int>) {
        //local buat sendiri
        val listOfId = List(listOfImage.size) { it + 1 }
        //mapping image
        val listOfData: MutableList<DummyBanner> = listOfImage.zip(listOfId).map {
            DummyBanner(imageId = it.first, id = it.second)
        }.toMutableList()
        _bannerUiState.update { currentUiState ->
            currentUiState.copy(isLoading = false, data = listOfData)
        }
    }

    fun setBannerLoading() {
        _bannerUiState.update { currentUiState ->
            currentUiState.copy(isLoading = true)
        }
    }

    fun setBannerError() {
        _bannerUiState.update { currentUiState ->
            currentUiState.copy(isLoading = false, errorMessage = "error kaka")
        }
    }


    private val _listBannerData: MutableStateFlow<MutableList<DummyBanner>> =
        MutableStateFlow(emptyList<DummyBanner>().toMutableList())

    val listBannerData: StateFlow<List<DummyBanner>> =
        _listBannerData.asStateFlow()

    private val _listBannerData2: MutableStateFlow<MutableList<MultiAdapterBannerData>> =
        MutableStateFlow(
            mutableListOf(
                MultiAdapterBannerData.Shimmer(0),
                MultiAdapterBannerData.Shimmer(1),
                MultiAdapterBannerData.Shimmer(2),
            )
        )

    val listBannerData2: StateFlow<List<MultiAdapterBannerData>> =
        _listBannerData2.asStateFlow()


    fun setDummyBanner(dummyBannerData: List<DummyBanner>) {
        _listBannerData.update {
            _listBannerData.value.toMutableList().apply {
                this.addAll(dummyBannerData)
            }
        }
    }

    fun setDummyBanner2(dummyBannerData: List<DummyBanner>) {
        _listBannerData2.update {
            _listBannerData2.value.toMutableList().apply {
                this.clear()
                dummyBannerData.forEach {
                    this.add(MultiAdapterBannerData.Banner(it.id, it.imageId))
                }
            }
        }
    }
}
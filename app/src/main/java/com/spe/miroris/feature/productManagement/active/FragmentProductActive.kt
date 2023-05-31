package com.spe.miroris.feature.productManagement.active

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.spe.miroris.R
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.core.data.dataSource.remote.model.response.ProductUserResponse
import com.spe.miroris.core.domain.model.mapToDomain
import com.spe.miroris.databinding.FragmentProductActiveBinding
import com.spe.miroris.feature.productManagement.controller.FragmentProductManagementDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentProductActive : BaseFragmentViewBinding<FragmentProductActiveBinding>(),
    EpoxyMultiProductManagementActiveController.EpoxyMultiProductManagementActiveControllerClickListener {

    private var listProductCatalog: List<ProductUserResponse> = mutableListOf()

    private val productManagementActiveViewModel: ProductManagementActiveViewModel by viewModels()

    private val epoxyMultiProductManagementActiveController: EpoxyMultiProductManagementActiveController by lazy {
        EpoxyMultiProductManagementActiveController(this)
    }
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProductActiveBinding
        get() = FragmentProductActiveBinding::inflate

    override fun initView() {
        with(binding.rvProductActive) {
            setController(epoxyMultiProductManagementActiveController)
            LinearSnapHelper().attachToRecyclerView(this)
        }
    }

    override fun viewCreated() {
        consumeSuspend {
            productManagementActiveViewModel.activeProductUser.collect { data ->
                epoxyMultiProductManagementActiveController.setData(data)
            }
        }

        consumeSuspend {
            productManagementActiveViewModel.productManagementUiState.collect {
                when (it.successState) {
                    ProductManagementActiveUiState.SuccessState.Initialize -> {

                    }

                    ProductManagementActiveUiState.SuccessState.Success -> {
                        if (it.data.isNotEmpty()) {
                            listProductCatalog = it.data
                            productManagementActiveViewModel.setupSuccessProductActive(it.data)
                        } else {
                            productManagementActiveViewModel.setupFailedProductActive()
                        }
                    }

                    ProductManagementActiveUiState.SuccessState.Failed -> {
                        productManagementActiveViewModel.setupFailedProductActive()
                    }

                    ProductManagementActiveUiState.SuccessState.RefreshToken -> {
                        productManagementActiveViewModel.refreshToken(
                            productManagementActiveViewModel.getUserEmail()
                        )
                    }
                }


                if (it.errorMessage.isNotEmpty()) {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.error_token_dialog_title))
                        .setMessage(it.errorMessage)
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                            dialog.dismiss()
                        }.show()
                }
            }
        }

        consumeSuspend {
            productManagementActiveViewModel.refreshTokenUiState.collect {
                when (it.successState) {
                    RefreshTokenProductManagementActiveUiState.SuccessState.Initialize -> {

                    }

                    RefreshTokenProductManagementActiveUiState.SuccessState.Success -> {
                        productManagementActiveViewModel.getProductData()
                    }

                    RefreshTokenProductManagementActiveUiState.SuccessState.Failed -> {
                        productManagementActiveViewModel.setupFailedProductActive()
                    }
                }
                if (it.errorMessage.isNotEmpty()) {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.error_token_dialog_title))
                        .setMessage(it.errorMessage)
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                            dialog.dismiss()
                        }.show()
                }
            }
        }

    }

    override fun onDeleteClick(selectedId: String) {
    }

    override fun onEditClick(selectedData: MultiAdapterProductManagementActiveData.ProductActive) {
        findNavController().navigate(FragmentProductManagementDirections.actionFragmentProductManagementToFragmentEditProductFirstStep())

    }

    override fun onQrClick(selectedId: String) {

    }

    override fun onRootClick(selectedId: String) {
        val selectedPosition = listProductCatalog.indexOfFirst {
            it.productId == selectedId
        }
        val data = listProductCatalog[selectedPosition].mapToDomain()
        findNavController().navigate(
            FragmentProductManagementDirections.actionFragmentProductManagementToFragmentDetailProduct(
                data
            )
        )

    }

}
package com.spe.miroris.feature.productManagement.done

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearSnapHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.spe.miroris.R
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.databinding.FragmentProductDoneBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentProductDone : BaseFragmentViewBinding<FragmentProductDoneBinding>(),
    EpoxyMultiProductManagementDoneController.EpoxyMultiProductManagementDoneControllerClickListener {

    private val productManagementDoneViewModel: ProductManagementDoneViewModel by viewModels()


    private val epoxyMultiProductManagementDoneController: EpoxyMultiProductManagementDoneController by lazy {
        EpoxyMultiProductManagementDoneController(this)
    }
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProductDoneBinding
        get() = FragmentProductDoneBinding::inflate

    override fun initView() {
        with(binding.rvProductDone) {
            setController(epoxyMultiProductManagementDoneController)
            LinearSnapHelper().attachToRecyclerView(this)
        }
    }

    override fun viewCreated() {
        consumeSuspend {
            productManagementDoneViewModel.doneProductUser.collect { data ->
                epoxyMultiProductManagementDoneController.setData(data)
            }
        }
        consumeSuspend {
            productManagementDoneViewModel.productManagementUiState.collect {
                when (it.successState) {
                    ProductManagementDoneUiState.SuccessState.Initialize -> {

                    }

                    ProductManagementDoneUiState.SuccessState.Success -> {
                        if (it.data.isNotEmpty()) {
                            productManagementDoneViewModel.setupSuccessProductDone(it.data)
                        } else{
                            productManagementDoneViewModel.setupFailedProductDone()
                        }
                    }

                    ProductManagementDoneUiState.SuccessState.Failed -> {
                        productManagementDoneViewModel.setupFailedProductDone()
                    }

                    ProductManagementDoneUiState.SuccessState.RefreshToken -> {
                        productManagementDoneViewModel.refreshToken(
                            productManagementDoneViewModel.getUserEmail()
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
            productManagementDoneViewModel.refreshTokenUiState.collect {
                when (it.successState) {
                    RefreshTokenProductManagementDoneUiState.SuccessState.Initialize -> {

                    }

                    RefreshTokenProductManagementDoneUiState.SuccessState.Success -> {
                        productManagementDoneViewModel.getProductData()
                    }

                    RefreshTokenProductManagementDoneUiState.SuccessState.Failed -> {
                        productManagementDoneViewModel.setupFailedProductDone()
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

    override fun onRootClick(selectedId: String) {

    }

}
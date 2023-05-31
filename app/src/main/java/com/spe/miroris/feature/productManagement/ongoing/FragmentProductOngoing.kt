package com.spe.miroris.feature.productManagement.ongoing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearSnapHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.spe.miroris.R
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.databinding.FragmentProductOngoingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentProductOngoing : BaseFragmentViewBinding<FragmentProductOngoingBinding>(),
    EpoxyMultiProductManagementOnGoingController.EpoxyMultiProductManagementOnGoingControllerClickListener {

    private val productManagementOnGoingViewModel: ProductManagementOnGoingViewModel by viewModels()


    private val epoxyMultiProductManagementOnGoingController: EpoxyMultiProductManagementOnGoingController by lazy {
        EpoxyMultiProductManagementOnGoingController(this)
    }
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProductOngoingBinding
        get() = FragmentProductOngoingBinding::inflate

    override fun initView() {
        with(binding.rvProductOnGoing) {
            setController(epoxyMultiProductManagementOnGoingController)
            LinearSnapHelper().attachToRecyclerView(this)
        }
    }

    override fun viewCreated() {
        consumeSuspend {
            productManagementOnGoingViewModel.onGoingProductUser.collect { data ->
                epoxyMultiProductManagementOnGoingController.setData(data)
            }
        }
        consumeSuspend {
            productManagementOnGoingViewModel.productManagementUiState.collect {
                when (it.successState) {
                    ProductManagementOnGoingUiState.SuccessState.Initialize -> {

                    }

                    ProductManagementOnGoingUiState.SuccessState.Success -> {
                        if (it.data.isNotEmpty()) {
                            productManagementOnGoingViewModel.setupSuccessProductOnGoing(it.data)
                        } else{
                            productManagementOnGoingViewModel.setupFailedProductOnGoing()
                        }
                    }

                    ProductManagementOnGoingUiState.SuccessState.Failed -> {
                        productManagementOnGoingViewModel.setupFailedProductOnGoing()
                    }

                    ProductManagementOnGoingUiState.SuccessState.RefreshToken -> {
                        productManagementOnGoingViewModel.refreshToken(
                            productManagementOnGoingViewModel.getUserEmail()
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
            productManagementOnGoingViewModel.refreshTokenUiState.collect {
                when (it.successState) {
                    RefreshTokenProductManagementOnGoingUiState.SuccessState.Initialize -> {

                    }

                    RefreshTokenProductManagementOnGoingUiState.SuccessState.Success -> {
                        productManagementOnGoingViewModel.getProductData()
                    }

                    RefreshTokenProductManagementOnGoingUiState.SuccessState.Failed -> {
                        productManagementOnGoingViewModel.setupFailedProductOnGoing()
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
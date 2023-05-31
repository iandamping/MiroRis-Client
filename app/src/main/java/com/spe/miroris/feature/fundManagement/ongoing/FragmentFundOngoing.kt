package com.spe.miroris.feature.fundManagement.ongoing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearSnapHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.spe.miroris.R
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.databinding.FragmentFundOngoingBinding
import com.spe.miroris.feature.fundManagement.FundManagementUiState
import com.spe.miroris.feature.fundManagement.FundManagementViewModel
import com.spe.miroris.feature.fundManagement.RefreshTokenFundManagementUiState
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class FragmentFundOngoing : BaseFragmentViewBinding<FragmentFundOngoingBinding>(),
    EpoxyMultiFundController.EpoxyMultiFundControllerClickListener {

    private val fundManagementViewModel: FundManagementViewModel by viewModels()

    private val epoxyMultiFundController: EpoxyMultiFundController by lazy {
        EpoxyMultiFundController(this)
    }
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFundOngoingBinding
        get() = FragmentFundOngoingBinding::inflate

    override fun initView() {
        with(binding.rvFundOngoing) {
            setController(epoxyMultiFundController)
            LinearSnapHelper().attachToRecyclerView(this)
        }
    }

    override fun viewCreated() {
        consumeSuspend {
            fundManagementViewModel.fundManagementUiState.collect {
                when (it.successState) {
                    FundManagementUiState.SuccessState.Initialize -> {

                    }

                    FundManagementUiState.SuccessState.Success -> {
                        if (it.data.isNotEmpty()) {
                            fundManagementViewModel.setupSuccessInvestmentOnGoing(it.data)
                        } else {
                            fundManagementViewModel.setupFailedInvestmentOnGoing()
                        }
                    }

                    FundManagementUiState.SuccessState.Failed -> {
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

                    FundManagementUiState.SuccessState.RefreshToken -> {
                        fundManagementViewModel.refreshToken(fundManagementViewModel.getUserEmail())
                    }
                }


            }
        }

        consumeSuspend {
            fundManagementViewModel.refreshTokenUiState.collect {
                when (it.successState) {
                    RefreshTokenFundManagementUiState.SuccessState.Initialize -> {

                    }

                    RefreshTokenFundManagementUiState.SuccessState.Success -> {
                        fundManagementViewModel.getFund()
                    }

                    RefreshTokenFundManagementUiState.SuccessState.Failed -> {
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
        }

        consumeSuspend {
            fundManagementViewModel.fundOnGoing.collect() {
                epoxyMultiFundController.setData(it)
            }
        }
    }

    override fun onInvestmentClick(selectedInvestmentId: String) {
        Timber.e(selectedInvestmentId)
    }

}
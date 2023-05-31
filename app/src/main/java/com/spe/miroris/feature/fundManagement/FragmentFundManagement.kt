package com.spe.miroris.feature.fundManagement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.spe.miroris.R
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.databinding.FragmentFundManagementBinding
import com.spe.miroris.util.rupiahFormatter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentFundManagement : BaseFragmentViewBinding<FragmentFundManagementBinding>() {

    private val fundManagementViewModel: FundManagementViewModel by viewModels()
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFundManagementBinding
        get() = FragmentFundManagementBinding::inflate

    override fun initView() {
        val adapter = FundManagementAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        binding.viewPagerFundManagement.adapter = adapter

        TabLayoutMediator(
            binding.tabLayoutFundManagement,
            binding.viewPagerFundManagement
        ) { tab, position ->
            tab.text = FundManagementEnum.values()[position].name
        }.attach()
    }

    override fun viewCreated() {
        consumeSuspend {
            fundManagementViewModel.fundManagementUiState.collect {
                when (it.successState) {
                    FundManagementUiState.SuccessState.Initialize -> {

                    }

                    FundManagementUiState.SuccessState.Success -> {

                    }

                    FundManagementUiState.SuccessState.Failed -> {

                    }

                    FundManagementUiState.SuccessState.RefreshToken -> {

                    }
                }

                binding.tvTotalFund.text =
                    if (it.totalAmount.isEmpty()) "Rp ${rupiahFormatter("0")}" else "Rp ${
                        rupiahFormatter(it.totalAmount)
                    }"
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
            fundManagementViewModel.refreshTokenUiState.collect {
                when (it.successState) {
                    RefreshTokenFundManagementUiState.SuccessState.Initialize -> {

                    }

                    RefreshTokenFundManagementUiState.SuccessState.Success -> {

                    }

                    RefreshTokenFundManagementUiState.SuccessState.Failed -> {

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
}
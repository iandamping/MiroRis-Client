package com.spe.miroris.feature.investmentManagement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.spe.miroris.R
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.databinding.FragmentInvestmentManagementBinding
import com.spe.miroris.util.rupiahFormatter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentInvestmentManagement :
    BaseFragmentViewBinding<FragmentInvestmentManagementBinding>() {
    private val investmentManagementViewModel: InvestmentManagementViewModel by viewModels()
    private val sharedInvestmentManagementViewModel:SharedInvestmentManagementViewModel by activityViewModels()
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentInvestmentManagementBinding
        get() = FragmentInvestmentManagementBinding::inflate

    override fun initView() {
        val adapter =
            InvestmentManagementAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        binding.viewPagerInvestmentManagement.adapter = adapter

        TabLayoutMediator(
            binding.tabLayoutInvestmentManagement,
            binding.viewPagerInvestmentManagement
        ) { tab, position ->
            tab.text = InvestmentManagementEnum.values()[position].name
        }.attach()
    }

    override fun viewCreated() {
        consumeSuspend {
            investmentManagementViewModel.investmentManagementUiState.collect {
                when (it.successState) {
                    InvestmentManagementUiState.SuccessState.Initialize -> {

                    }

                    InvestmentManagementUiState.SuccessState.Success -> {

                    }

                    InvestmentManagementUiState.SuccessState.Failed -> {

                    }

                    InvestmentManagementUiState.SuccessState.RefreshToken -> {

                    }
                }

                sharedInvestmentManagementViewModel.setInvestmentData(it.data)

                binding.tvTotalFund.text = if (it.totalAmount.isEmpty()) "Rp ${rupiahFormatter("0")}" else "Rp ${rupiahFormatter(it.totalAmount)}"

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
            investmentManagementViewModel.refreshTokenUiState.collect {
                when (it.successState) {
                    RefreshTokenInvestmentManagementUiState.SuccessState.Initialize -> {

                    }

                    RefreshTokenInvestmentManagementUiState.SuccessState.Success -> {

                    }

                    RefreshTokenInvestmentManagementUiState.SuccessState.Failed -> {

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
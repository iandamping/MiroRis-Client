package com.spe.miroris.feature.investmentManagement

import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.databinding.FragmentInvestmentManagementBinding

class FragmentInvestmentManagement :
    BaseFragmentViewBinding<FragmentInvestmentManagementBinding>() {
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
    }
}
package com.spe.miroris.feature.fundManagement

import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.databinding.FragmentFundManagementBinding

class FragmentFundManagement : BaseFragmentViewBinding<FragmentFundManagementBinding>() {
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
    }
}
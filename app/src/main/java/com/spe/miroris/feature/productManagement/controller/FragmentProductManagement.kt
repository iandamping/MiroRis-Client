package com.spe.miroris.feature.productManagement.controller

import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.databinding.FragmentProductManagementBinding

class FragmentProductManagement : BaseFragmentViewBinding<FragmentProductManagementBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProductManagementBinding
        get() = FragmentProductManagementBinding::inflate

    override fun initView() {
        val adapter = ProductManagementAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        binding.viewPagerProductManagement.adapter = adapter

        TabLayoutMediator(
            binding.tabLayoutProductManagement,
            binding.viewPagerProductManagement
        ) { tab, position ->
            tab.text = ProductManagementEnum.values()[position].name
        }.attach()

    }

    override fun viewCreated() {
    }

}
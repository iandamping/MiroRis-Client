package com.spe.miroris.feature.investmentManagement.ongoing

import android.view.LayoutInflater
import android.view.ViewGroup
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.databinding.FragmentInvestmentDoneBinding

class FragmentInvestmentOngoing : BaseFragmentViewBinding<FragmentInvestmentDoneBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentInvestmentDoneBinding
        get() = FragmentInvestmentDoneBinding::inflate

    override fun initView() {
    }

    override fun viewCreated() {
    }

}
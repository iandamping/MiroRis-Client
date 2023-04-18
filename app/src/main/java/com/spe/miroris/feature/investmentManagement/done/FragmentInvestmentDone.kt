package com.spe.miroris.feature.investmentManagement.done

import android.view.LayoutInflater
import android.view.ViewGroup
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.databinding.FragmentInvestmentOngoingBinding

class FragmentInvestmentDone:BaseFragmentViewBinding<FragmentInvestmentOngoingBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentInvestmentOngoingBinding
        get() = FragmentInvestmentOngoingBinding::inflate

    override fun initView() {
    }

    override fun viewCreated() {
    }
}
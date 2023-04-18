package com.spe.miroris.feature.fundManagement.ongoing

import android.view.LayoutInflater
import android.view.ViewGroup
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.databinding.FragmentFundOngoingBinding

class FragmentFundOngoing : BaseFragmentViewBinding<FragmentFundOngoingBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFundOngoingBinding
        get() = FragmentFundOngoingBinding::inflate

    override fun initView() {
    }

    override fun viewCreated() {
    }

}
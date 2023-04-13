package com.spe.miroris.feature.productManagement.ongoing

import android.view.LayoutInflater
import android.view.ViewGroup
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.databinding.FragmentProductOngoingBinding

class FragmentProductOngoing : BaseFragmentViewBinding<FragmentProductOngoingBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProductOngoingBinding
        get() = FragmentProductOngoingBinding::inflate

    override fun initView() {
    }

    override fun viewCreated() {
    }

}
package com.spe.miroris.feature.productManagement.active

import android.view.LayoutInflater
import android.view.ViewGroup
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.databinding.FragmentProductActiveBinding

class FragmentProductActive : BaseFragmentViewBinding<FragmentProductActiveBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProductActiveBinding
        get() = FragmentProductActiveBinding::inflate

    override fun initView() {
    }

    override fun viewCreated() {
    }

}
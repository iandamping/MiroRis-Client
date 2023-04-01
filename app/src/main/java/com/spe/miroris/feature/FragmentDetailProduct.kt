package com.spe.miroris.feature

import android.view.LayoutInflater
import android.view.ViewGroup
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.databinding.FragmentDetailProductBinding

class FragmentDetailProduct : BaseFragmentViewBinding<FragmentDetailProductBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDetailProductBinding
        get() = FragmentDetailProductBinding::inflate

    override fun initView() {
    }

    override fun viewCreated() {
    }
}
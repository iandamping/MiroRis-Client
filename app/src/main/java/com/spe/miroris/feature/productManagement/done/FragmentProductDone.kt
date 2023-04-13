package com.spe.miroris.feature.productManagement.done

import android.view.LayoutInflater
import android.view.ViewGroup
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.databinding.FragmentProductDoneBinding

class FragmentProductDone : BaseFragmentViewBinding<FragmentProductDoneBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProductDoneBinding
        get() = FragmentProductDoneBinding::inflate

    override fun initView() {
    }

    override fun viewCreated() {
    }

}
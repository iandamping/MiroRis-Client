package com.spe.miroris.feature.fundManagement.done

import android.view.LayoutInflater
import android.view.ViewGroup
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.databinding.FragmentFundDoneBinding

class FragmentFundDone: BaseFragmentViewBinding<FragmentFundDoneBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFundDoneBinding
        get() = FragmentFundDoneBinding::inflate

    override fun initView() {
    }

    override fun viewCreated() {
    }

}
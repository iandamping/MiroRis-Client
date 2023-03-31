package com.spe.miroris.feature

import android.view.LayoutInflater
import android.view.ViewGroup
import com.spe.miroris.base.BaseFragmentDataBinding
import com.spe.miroris.databinding.FragmentChangePasswordBinding

class FragmentChangePassword : BaseFragmentDataBinding<FragmentChangePasswordBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentChangePasswordBinding
        get() = FragmentChangePasswordBinding::inflate

    override fun initView() {
    }

    override fun viewCreated() {
    }
}
package com.spe.miroris.feature

import android.view.LayoutInflater
import android.view.ViewGroup
import com.spe.miroris.base.BaseFragmentDataBinding
import com.spe.miroris.databinding.FragmentLoginBinding

class FragmentLogin : BaseFragmentDataBinding<FragmentLoginBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLoginBinding
        get() = FragmentLoginBinding::inflate

    override fun initView() {
    }

    override fun viewCreated() {
    }
}
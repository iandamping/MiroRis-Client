package com.spe.miroris.feature

import android.view.LayoutInflater
import android.view.ViewGroup
import com.spe.miroris.base.BaseFragmentDataBinding
import com.spe.miroris.databinding.FragmentRegistrationBinding

class FragmentRegistration : BaseFragmentDataBinding<FragmentRegistrationBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentRegistrationBinding
        get() = FragmentRegistrationBinding::inflate

    override fun initView() {
    }

    override fun viewCreated() {
    }
}
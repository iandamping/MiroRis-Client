package com.spe.miroris.feature

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.databinding.FragmentProfileBinding

class FragmentProfile : BaseFragmentViewBinding<FragmentProfileBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProfileBinding
        get() = FragmentProfileBinding::inflate

    override fun initView() {
    }

    override fun viewCreated() {
        binding.ivEditUserProfile.setOnClickListener {
            findNavController().navigate(FragmentProfileDirections.actionFragmentProfileToFragmentEditProfile())
        }
    }
}
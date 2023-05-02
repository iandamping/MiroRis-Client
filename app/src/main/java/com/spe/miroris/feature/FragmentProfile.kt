package com.spe.miroris.feature

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.databinding.FragmentProfileBinding

class FragmentProfile : BaseFragmentViewBinding<FragmentProfileBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProfileBinding
        get() = FragmentProfileBinding::inflate

    override fun initView() {
    }

    override fun viewCreated() {
        binding.layerLogout.setOnClickListener {
            Toast.makeText(requireContext(),"logout",Toast.LENGTH_SHORT).show()
        }
        binding.layerChangePassword.setOnClickListener {
            Toast.makeText(requireContext(),"change password",Toast.LENGTH_SHORT).show()
        }
        binding.layerEditProfile.setOnClickListener {
            findNavController().navigate(FragmentProfileDirections.actionFragmentProfileToFragmentEditProfile())
        }
    }
}
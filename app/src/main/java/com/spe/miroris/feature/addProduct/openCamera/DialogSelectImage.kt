package com.spe.miroris.feature.addProduct.openCamera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import coil.load
import com.spe.miroris.R
import com.spe.miroris.databinding.DialogSelectImageBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DialogSelectImage : DialogFragment() {
    private val cameraVm: CameraViewModel by viewModels()

    private var _binding: DialogSelectImageBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isCancelable = false
        _binding = DialogSelectImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.Dialog_App
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivImage.load(cameraVm.provideImageUri())

        binding.ivImageSelect.setOnClickListener {
            setFragmentResult(
                "requestPictureKey",
                bundleOf("bundlePictureKey" to cameraVm.provideImageUri().toString())
            )
            dismiss()
        }
        binding.ivImageDelete.setOnClickListener {
            cameraVm.deletePhoto()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
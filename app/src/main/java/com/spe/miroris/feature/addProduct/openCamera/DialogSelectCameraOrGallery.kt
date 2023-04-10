package com.spe.miroris.feature.addProduct.openCamera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.spe.miroris.R
import com.spe.miroris.databinding.DialogSelectCameraOrGalleryBinding

class DialogSelectCameraOrGallery : DialogFragment() {

    private var _binding: DialogSelectCameraOrGalleryBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isCancelable = false
        _binding = DialogSelectCameraOrGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.Dialog_App
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.llSelectCamera.setOnClickListener {
            setFragmentResult("requestKey", bundleOf("bundleKey" to CameraGalleryEnum.CAMERA.name))
            dismiss()
        }
        binding.llSelectGallery.setOnClickListener {
            setFragmentResult("requestKey", bundleOf("bundleKey" to CameraGalleryEnum.GALLERY.name))
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
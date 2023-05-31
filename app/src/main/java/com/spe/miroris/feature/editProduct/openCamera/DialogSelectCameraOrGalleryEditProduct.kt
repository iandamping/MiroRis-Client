package com.spe.miroris.feature.editProduct.openCamera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.spe.miroris.R
import com.spe.miroris.databinding.DialogSelectCameraOrGalleryBinding
import com.spe.miroris.feature.addProduct.FragmentAddProductFirstStep.Companion.BACK_STACK_ENTRY_KEY

class DialogSelectCameraOrGalleryEditProduct : DialogFragment() {

    private var _binding: DialogSelectCameraOrGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Dialog_FullWidth)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSelectCameraOrGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.llSelectCamera.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                BACK_STACK_ENTRY_KEY,
                CameraEditProductGalleryEnum.CAMERA.name
            )
            dismiss()
        }
        binding.llSelectGallery.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                BACK_STACK_ENTRY_KEY,
                CameraEditProductGalleryEnum.GALLERY.name
            )
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
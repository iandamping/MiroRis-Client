package com.spe.miroris.feature.editProduct.openCamera

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import coil.load
import com.spe.miroris.R
import com.spe.miroris.databinding.DialogSelectImageBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DialogSelectImageEditProduct(private val uri: Uri, private val listener: DialogSelectImageCallback) :
    DialogFragment() {

    interface DialogSelectImageCallback {
        fun deleteThisImage(uri: Uri)
    }

    private var _binding: DialogSelectImageBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSelectImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.Dialog_App
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivImage.load(uri)

        binding.ivImageSelect.setOnClickListener {
            dismiss()
        }
        binding.ivImageDelete.setOnClickListener {
            listener.deleteThisImage(uri)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
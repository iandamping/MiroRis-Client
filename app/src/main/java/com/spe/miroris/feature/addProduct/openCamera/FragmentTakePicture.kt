package com.spe.miroris.feature.addProduct.openCamera

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.camera.core.CameraInfoUnavailableException
import androidx.camera.core.CameraSelector
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.spe.miroris.R
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.databinding.FragmentTakePictureBinding
import com.spe.miroris.feature.addProduct.SharedAddProductViewModel
import com.spe.miroris.feature.addProduct.adapter.MultiAdapterData
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FragmentTakePicture : BaseFragmentViewBinding<FragmentTakePictureBinding>() {

    companion object {
        private const val ANIMATION_FAST_MILLIS = 50L
        private const val ANIMATION_SLOW_MILLIS = 100L
        private const val FAILED_CAPTURE_IMAGE = "Photo capture failed :"
    }

    private val cameraVm: CameraViewModel by viewModels()

    private val addProductVm: SharedAddProductViewModel by activityViewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTakePictureBinding
        get() = FragmentTakePictureBinding::inflate

    override fun initView() {
        observeCameraLens()

        try {
            binding.switchCamera.isEnabled = cameraVm.hasBackCamera() && cameraVm.hasFrontCamera()
        } catch (exception: CameraInfoUnavailableException) {
            binding.switchCamera.isEnabled = false
        }
    }

    override fun viewCreated() {
        observeListener()
        backPress()


        binding.switchCamera.setOnClickListener {
            if (CameraSelector.LENS_FACING_FRONT == cameraVm.lensStateFlow.value) {
                cameraVm.setLens(cameraVm.changeIntoBackCamera())
            } else {
                cameraVm.setLens(cameraVm.changeIntoFrontCamera())
            }
        }

        binding.captureImage.setOnClickListener {
            cameraVm.takePhoto()
            flashAnimationAfterTakingPicture()
        }
    }

    private fun backPress() {
        overrideFragmentBackPressed {
            findNavController().navigate(
                FragmentTakePictureDirections.actionFragmentTakePictureToFragmentAddProductFirstStep()
            )
        }
    }

    private fun observeListener() {
        consumeSuspend {
            cameraVm.photoState.collect {
                when {
                    it.failedMessage.isNotEmpty() -> {
                        Snackbar.make(
                            requireContext(),
                            binding.root,
                            it.failedMessage,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    it.successUri.isNotEmpty() || it.successUri == FAILED_CAPTURE_IMAGE -> {
                        cameraVm.resetImageCaptureState()
                        addProductVm.setMiniImageData(MultiAdapterData.Main(Uri.parse(it.successUri)))
                        findNavController().navigate(
                            FragmentTakePictureDirections.actionFragmentTakePictureToFragmentAddProductFirstStep()
                        )
                    }
                }
            }
        }
    }


    private fun observeCameraLens() {
        consumeSuspend {
            cameraVm.lensStateFlow.collect { lensState ->
                try {
                    cameraVm.startCamera(
                        lifecycleOwner = viewLifecycleOwner,
                        lens = lensState,
                        preview = cameraVm.providePreview(view = binding.viewFinder)
                    ) { camera ->
                        try {
                            cameraVm.autoFocusPreview(
                                view = binding.viewFinder,
                                camera = camera
                            )
                        } catch (e: Exception) {
                            Snackbar.make(
                                requireContext(),
                                binding.root,
                                getString(R.string.failed_focus_camera, e.message),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    Snackbar.make(
                        requireContext(),
                        binding.root,
                        getString(R.string.failed_start_camera, e.message),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

            }

        }
    }

    private fun flashAnimationAfterTakingPicture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            with(binding.root) {
                postDelayed({
                    foreground = ColorDrawable(Color.WHITE)
                    postDelayed(
                        { foreground = null }, ANIMATION_FAST_MILLIS
                    )
                }, ANIMATION_SLOW_MILLIS)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraVm.shutdownExecutor()
    }


}
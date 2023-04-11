package com.spe.miroris.feature.addProduct.openCamera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.spe.miroris.R
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.databinding.FragmentTakePictureBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class FragmentTakePicture : BaseFragmentViewBinding<FragmentTakePictureBinding>() {

    companion object {
        private const val OPEN_DIALOG_FRAGMENT_TAG = "select photo from dialog"
    }

    private val cameraVm: CameraViewModel by viewModels()
    private lateinit var openCameraPermissionLauncher: ActivityResultLauncher<String>

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTakePictureBinding
        get() = FragmentTakePictureBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("requestPictureKey") { _, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            val result = bundle.getString("bundlePictureKey")
            if (!result.isNullOrEmpty()){
                Timber.e("result : $result")
            }
        }
    }

    override fun initView() {
        invokePermission()
        checkPermission()
    }

    override fun viewCreated() {
        observeListener()
    }

    private fun invokePermission() {
        openCameraPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    observeCameraLens()
                } else {
                    Snackbar.make(
                        requireContext(),
                        binding.root,
                        getString(R.string.permission_not_granted),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun checkPermission() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) -> {
                observeCameraLens()
            }
            else -> {
                openCameraPermissionLauncher.launch(
                    Manifest.permission.CAMERA
                )
            }
        }
    }


    private fun observeListener() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
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
                        it.successMessage.isNotEmpty() -> {
                            cameraVm.resetImageCaptureState()
                            showDialog()
                        }
                    }
                }
            }
        }
    }


    private fun observeCameraLens() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
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
    }

    private fun showDialog() {
        val dialogFragment = DialogSelectImage()
        dialogFragment.show(childFragmentManager, OPEN_DIALOG_FRAGMENT_TAG)
    }

}
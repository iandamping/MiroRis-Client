package com.spe.miroris.feature.detail

import android.Manifest
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearSnapHelper
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.spe.miroris.R
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.databinding.FragmentDetailProductBinding
import com.spe.miroris.di.qualifier.CustomDialogQualifier
import com.spe.miroris.util.saveToInternalStorage
import com.spe.miroris.util.shareCacheDirBitmap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


@AndroidEntryPoint
class FragmentDetailProduct : BaseFragmentViewBinding<FragmentDetailProductBinding>() {


    @Inject
    @CustomDialogQualifier
    lateinit var customDialog: AlertDialog


    private val detailProductViewModel: DetailProductViewModel by viewModels()
    private val args: FragmentDetailProductArgs by navArgs()

    private val epoxyProductMiniImageController: EpoxyProductMiniImageController by lazy {
        EpoxyProductMiniImageController()
    }

    private var bitmapToShare: Bitmap? = null
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDetailProductBinding
        get() = FragmentDetailProductBinding::inflate

    override fun initView() {
        if (args.passedProductCatalog.productImage.isNotEmpty()) {
            binding.ivDetailProduct.load(args.passedProductCatalog.productImage.first())
        } else {
            binding.ivDetailProduct.load(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.img_dummy_product
                )
            )

        }

        with(binding.rvMiniDetailProduct) {
            setController(epoxyProductMiniImageController)
            LinearSnapHelper().attachToRecyclerView(this)
        }

        val imageBytes = Base64.decode(args.passedProductCatalog.productQris, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        if (args.passedProductCatalog.productType == "2") {
            binding.groupGenerateQr.visibility = View.GONE
            binding.ivProductDetailQr.visibility = View.VISIBLE
            binding.ivProductDetailQr.load(decodedImage)
            binding.ivQrisToDownload.load(decodedImage)
        } else {
            binding.ivProductDetailQr.visibility = View.GONE
            binding.groupGenerateQr.visibility = View.VISIBLE
            binding.imageView11.setOnClickListener {
                if (detailProductViewModel.getUserToken().isNotEmpty()) {
                    detailProductViewModel.generateQr(args.passedProductCatalog.productId)
                } else {
                    findNavController().navigate(FragmentDetailProductDirections.actionFragmentDetailProductToFragmentLogin())
                }
            }
        }
        if (args.passedProductCatalog.productImage.isNotEmpty()) {
            epoxyProductMiniImageController.setData(args.passedProductCatalog.productImage)
        }

        binding.tvProductName.text = args.passedProductCatalog.productName
        binding.tvProductDescription.text = args.passedProductCatalog.productDetail
    }

    override fun viewCreated() {

        observeRefreshToken()

        observeGenerateQr()

        binding.btnDownload.setOnClickListener {
            consumeSuspend {
                when (val downloadData = withContext(Dispatchers.Default) {
                    detailProductViewModel.getBitmapFromView(
                        binding.ivQrisToDownload, binding.ivQrisToDownload.height,
                        binding.ivQrisToDownload.width,
                        wrapper = ContextWrapper(requireContext())
                    )
                }) {
                    is ScreenshotData.Error -> {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle(resources.getString(R.string.error_token_dialog_title))
                            .setMessage(downloadData.msg)
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                                dialog.dismiss()
                            }.show()
                    }

                    is ScreenshotData.Success -> {
                        bitmapToShare = downloadData.data
                        requestPermission()
//                        // Save the bitmap to internal storage and get uri
//                        val uri = downloadData.data.saveToInternalStorage(requireContext())
//                        // Finally, share the internal storage saved bitmap
//                        requireActivity().shareCacheDirBitmap(uri)
                    }
                }
            }
        }


        binding.btnShare.setOnClickListener {
            consumeSuspend {
                when (val downloadData = withContext(Dispatchers.Default) {
                    detailProductViewModel.getBitmapFromView(
                        binding.ivQrisToDownload, binding.ivQrisToDownload.height,
                        binding.ivQrisToDownload.width,
                        wrapper = ContextWrapper(requireContext())
                    )
                }) {
                    is ScreenshotData.Error -> {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle(resources.getString(R.string.error_token_dialog_title))
                            .setMessage(downloadData.msg)
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                                dialog.dismiss()
                            }.show()
                    }

                    is ScreenshotData.Success -> {
                        // Save the bitmap to internal storage and get uri
                        val uri = downloadData.data.saveToInternalStorage(requireContext())
                        // Finally, share the internal storage saved bitmap
                        requireActivity().shareCacheDirBitmap(uri)
                    }
                }
            }
        }


    }

    private fun observeRefreshToken() {
        consumeSuspend {
            detailProductViewModel.refreshTokenUiState.collect {
                when (it.successState) {
                    RefreshTokenDetailProductUiState.SuccessState.Initialize -> {}
                    RefreshTokenDetailProductUiState.SuccessState.Failed -> customDialog.dismiss()
                    RefreshTokenDetailProductUiState.SuccessState.Success -> {
                        customDialog.show()
                        detailProductViewModel.generateQr(args.passedProductCatalog.productId)
                    }

                }
                if (it.errorMessage.isNotEmpty()) {
                    customDialog.dismiss()
                }
            }
        }
    }

    private fun observeGenerateQr() {
        consumeSuspend {
            detailProductViewModel.generateQrState.collect {
                when (it.successState) {
                    GenerateQrUiState.SuccessState.Initialize -> {}
                    GenerateQrUiState.SuccessState.Error -> {
                        customDialog.dismiss()
                        if (it.errorMessage.isNotEmpty()) {
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle(resources.getString(R.string.error_token_dialog_title))
                                .setMessage(it.errorMessage)
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                                    dialog.dismiss()
                                }.show()
                        }
                    }

                    GenerateQrUiState.SuccessState.Success -> {
                        customDialog.dismiss()
                        if (it.data.isNotEmpty()) {
                            val imageBytes = Base64.decode(it.data, Base64.DEFAULT)
                            val decodedImage =
                                BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                            binding.groupGenerateQr.visibility = View.GONE
                            binding.ivProductDetailQr.visibility = View.VISIBLE
                            binding.ivProductDetailQr.load(decodedImage)
                            binding.ivQrisToDownload.load(decodedImage)
                        }
                    }

                    GenerateQrUiState.SuccessState.RefreshToken -> {
                        customDialog.show()
                        detailProductViewModel.refreshToken(detailProductViewModel.getUserEmail())
                    }

                }
                if (it.errorMessage.isNotEmpty()) {
                    customDialog.dismiss()
                }
            }
        }
    }

    private fun askForWritePermission() {
        askPermissions.launch(listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE).toTypedArray())
    }

    private val askPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val isGranted = permissions.entries.all {
                it.value == true
            }

            if (isGranted) {
                consumeSuspend {
                    when (val downloadData = withContext(Dispatchers.Default) {
                        detailProductViewModel.saveImageToGallery(
                            requireContext().contentResolver,
                            bitmapToShare
                        )
                    }) {
                        is DownloadData.Error -> {
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle(resources.getString(R.string.error_token_dialog_title))
                                .setMessage(downloadData.msg)
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                                    dialog.dismiss()
                                }.show()
                        }

                        is DownloadData.Success -> {
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle(resources.getString(R.string.download_done_dialog_title))
                                .setMessage(downloadData.msg)
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                                    dialog.dismiss()
                                }.show()
                        }
                    }
                }
            } else {
                askForWritePermission()
            }
        }

    private fun requestPermission() {
        val minSDK = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        val isWritePermissionGranted = (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED) || minSDK

        if (!isWritePermissionGranted) {
            askForWritePermission()
        } else {
            consumeSuspend {
                when (val downloadData = withContext(Dispatchers.Default) {
                    detailProductViewModel.saveImageToGallery(
                        requireContext().contentResolver,
                        bitmapToShare
                    )
                }) {
                    is DownloadData.Error -> {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle(resources.getString(R.string.error_token_dialog_title))
                            .setMessage(downloadData.msg)
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                                dialog.dismiss()
                            }.show()
                    }

                    is DownloadData.Success -> {
                        Snackbar.make(binding.root, downloadData.msg, Snackbar.LENGTH_SHORT).show()
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle(resources.getString(R.string.download_done_dialog_title))
                            .setMessage(downloadData.msg)
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                                dialog.dismiss()
                            }.show()
                    }
                }
            }

        }
    }
}
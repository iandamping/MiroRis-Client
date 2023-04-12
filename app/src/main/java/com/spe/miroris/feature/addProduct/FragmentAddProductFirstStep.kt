package com.spe.miroris.feature.addProduct

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import com.google.android.material.snackbar.Snackbar
import com.spe.miroris.R
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.databinding.FragmentAddProductFirstStepBinding
import com.spe.miroris.feature.addProduct.adapter.EpoxyAddProductImageController
import com.spe.miroris.feature.addProduct.adapter.MultiAdapterData
import com.spe.miroris.feature.addProduct.openCamera.CameraGalleryEnum
import com.spe.miroris.feature.addProduct.openCamera.DialogSelectCameraOrGalleryDirections
import com.spe.miroris.feature.addProduct.openCamera.DialogSelectImage
import dagger.hilt.android.AndroidEntryPoint

private var PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)

@AndroidEntryPoint
class FragmentAddProductFirstStep : BaseFragmentViewBinding<FragmentAddProductFirstStepBinding>(),
    EpoxyAddProductImageController.EpoxyAddProductImageClickListener,
    AdapterView.OnItemSelectedListener,
    DialogSelectImage.DialogSelectImageCallback {

    companion object {
        const val BACK_STACK_ENTRY_KEY = "key from DialogSelectCameraOrGallery"
        private const val OPEN_SELECT_IMAGE_DIALOG = "select photo from dialog"
        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private val viewModel: AddProductFirstStepViewModel by activityViewModels()

    private lateinit var epoxyAddProductImageController: EpoxyAddProductImageController

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAddProductFirstStepBinding
        get() = FragmentAddProductFirstStepBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // add the storage access permission request for Android 9 and below.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            val permissionList = PERMISSIONS_REQUIRED.toMutableList()
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            PERMISSIONS_REQUIRED = permissionList.toTypedArray()
        }
    }

    override fun initView() {
        epoxyAddProductImageController = EpoxyAddProductImageController(this)
        val spinner = binding.spBankSelection
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.default_bank_spinner,
            android.R.layout.simple_spinner_item
        ).also { adapter ->

            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this
    }

    override fun viewCreated() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(
            BACK_STACK_ENTRY_KEY
        )
            ?.observe(
                viewLifecycleOwner
            ) { result ->
                // Do something with the result.
                when (result) {
                    CameraGalleryEnum.CAMERA.name -> {
                        if (!hasPermissions(requireContext())) {
                            // Request camera-related permissions
                            activityResultLauncher.launch(PERMISSIONS_REQUIRED)
                        } else {
                            findNavController().navigate(DialogSelectCameraOrGalleryDirections.actionDialogSelectCameraOrGalleryToFragmentTakePicture())
                        }
                    }
                    CameraGalleryEnum.GALLERY.name -> {
                        val intent = Intent(Intent.ACTION_PICK)
                        intent.type = "image/*"
                        intentGalleryLauncher.launch(intent)
                    }
                }
            }


        with(binding.rvMiniAddProduct) {
            setController(epoxyAddProductImageController)
            LinearSnapHelper().attachToRecyclerView(this)
        }
        binding.swOwnAccount.setOnCheckedChangeListener { buttonView, isChecked ->
            // Responds to switch being checked/unchecked
        }
        binding.btnContinue.setOnClickListener {
            //we will check this later
            findNavController().navigate(FragmentAddProductFirstStepDirections.actionFragmentAddProductFirstStepToFragmentAddProductSecondStep())
        }

        consumeSuspend {
            viewModel.listMiniImageData.collect { data ->
                epoxyAddProductImageController.setData(data)
            }
        }

    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

    override fun onEpoxyClick(data: MultiAdapterData) {
        when (data) {
            MultiAdapterData.Footer -> {
                //open gallery or camera
                showDialogSelectCameraOrGallery()
            }
            is MultiAdapterData.Main -> {
                //open the selected image
                val dialogFragment = DialogSelectImage(uri = data.image, listener = this)
                dialogFragment.show(childFragmentManager, OPEN_SELECT_IMAGE_DIALOG)
            }
        }
    }

    private fun showDialogSelectCameraOrGallery() {
        findNavController().navigate(FragmentAddProductFirstStepDirections.actionFragmentAddProductFirstStepToDialogSelectCameraOrGallery())
    }


    private val intentGalleryLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            try {
                val intentResult = result.data?.data
                viewModel.setMiniImageData(MultiAdapterData.Main(checkNotNull(intentResult)))
            } catch (e: Exception) {
                Snackbar.make(
                    requireContext(),
                    binding.root,
                    getString(R.string.failed_to_get_image_from_gallery),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in PERMISSIONS_REQUIRED && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Snackbar.make(
                    requireContext(),
                    binding.root,
                    getString(R.string.permission_not_granted),
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                findNavController().navigate(FragmentAddProductFirstStepDirections.actionFragmentAddProductFirstStepToFragmentTakePicture())
            }
        }

    override fun deleteThisImage(uri: Uri) {
        consumeSuspend {
            viewModel.listMiniImageData.collect { data ->
                val position = data.indexOfFirst {
                    if (it is MultiAdapterData.Main) {
                        it.image == uri
                    } else false
                }
                viewModel.deleteMiniImageData(position)
            }
        }
    }
}
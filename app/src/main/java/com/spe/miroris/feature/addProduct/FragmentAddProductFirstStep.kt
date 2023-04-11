package com.spe.miroris.feature.addProduct

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import com.google.android.material.snackbar.Snackbar
import com.spe.miroris.R
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.databinding.FragmentAddProductFirstStepBinding
import com.spe.miroris.feature.addProduct.adapter.EpoxyAddProductImageController
import com.spe.miroris.feature.addProduct.adapter.MultiAdapterData
import com.spe.miroris.feature.addProduct.openCamera.CameraGalleryEnum
import com.spe.miroris.feature.addProduct.openCamera.DialogSelectCameraOrGallery
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class FragmentAddProductFirstStep : BaseFragmentViewBinding<FragmentAddProductFirstStepBinding>(),
    EpoxyAddProductImageController.EpoxyAddProductImageClickListener,
    AdapterView.OnItemSelectedListener {

    companion object {
        private const val OPEN_DIALOG_FRAGMENT_GALLERY_CAMERA_TAG = "select gallery or camera fragment"
    }

    private val viewModel: AddProductFirstStepViewModel by viewModels()

    private lateinit var epoxyAddProductImageController: EpoxyAddProductImageController

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAddProductFirstStepBinding
        get() = FragmentAddProductFirstStepBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Use the Kotlin extension in the fragment-ktx artifact
        setFragmentResultListener("requestKey") { _, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            when (bundle.getString("bundleKey")) {
                CameraGalleryEnum.CAMERA.name -> {
                    findNavController().navigate(FragmentAddProductFirstStepDirections.actionFragmentAddProductFirstStepToFragmentTakePicture())
                }
                CameraGalleryEnum.GALLERY.name -> {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    intentGalleryLauncher.launch(intent)
                }
            }
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
        with(binding.rvMiniAddProduct) {
            setController(epoxyAddProductImageController)
            LinearSnapHelper().attachToRecyclerView(this)
        }
        binding.swOwnAccount.setOnCheckedChangeListener { buttonView, isChecked ->
            // Responds to switch being checked/unchecked
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
                Timber.e("open camera")
                showDialogSelectCameraOrGallery()
//                viewModel.setMiniImageData(MultiAdapterData.Main(Uri.EMPTY))
            }
            is MultiAdapterData.Main -> {
                //open the selected image
                Timber.e("select the image")

            }
        }
    }

    private fun showDialogSelectCameraOrGallery() {
        val dialogFragment = DialogSelectCameraOrGallery()
        dialogFragment.show(childFragmentManager, OPEN_DIALOG_FRAGMENT_GALLERY_CAMERA_TAG)
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
}
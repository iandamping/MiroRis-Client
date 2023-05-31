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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.spe.miroris.R
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.core.data.dataSource.remote.model.response.BankResponse
import com.spe.miroris.core.domain.model.CategoryProduct
import com.spe.miroris.databinding.FragmentAddProductFirstStepBinding
import com.spe.miroris.di.qualifier.CustomDialogQualifier
import com.spe.miroris.feature.addProduct.adapter.EpoxyAddProductImageController
import com.spe.miroris.feature.addProduct.adapter.MultiAdapterData
import com.spe.miroris.feature.addProduct.openCamera.CameraGalleryEnum
import com.spe.miroris.feature.addProduct.openCamera.DialogSelectCameraOrGalleryDirections
import com.spe.miroris.feature.addProduct.openCamera.DialogSelectImage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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

    private var listCategoryProduct: List<CategoryProduct> = mutableListOf()

    @Inject
    @CustomDialogQualifier
    lateinit var customDialog: AlertDialog

    private val addProductViewModelFirstStep: AddProductViewModelFirstStep by viewModels()

    private var bankCode: String = ""
    private var listBankResponse: List<BankResponse> = mutableListOf()
    private var isMyAccountCode = "0"
    private var selectedCategoryId: String = ""
    private var selectedCategory: String = ""
    private val viewModel: SharedAddProductViewModel by activityViewModels()

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
        spinner.onItemSelectedListener = this

        with(binding.rvMiniAddProduct) {
            setController(epoxyAddProductImageController)
            LinearSnapHelper().attachToRecyclerView(this)
        }

        if (addProductViewModelFirstStep.getBankCode()
                .isNotEmpty() && addProductViewModelFirstStep.getPhoneNumber().isNotEmpty()
        ) {
            binding.swOwnAccount.isChecked = true
            isMyAccountCode = "1"
        } else if (addProductViewModelFirstStep.getBankCode()
                .isEmpty() && addProductViewModelFirstStep.getPhoneNumber().isEmpty()
        ) {
            binding.swOwnAccount.isChecked = false
            isMyAccountCode = "0"
        }

        binding.textInputProductAccountNumber.setText(addProductViewModelFirstStep.getAccountNumber())
    }

    override fun viewCreated() {
        observeBackStackValue()

        setupSpinnerFromService()

        observeRefreshToken()

        observeCategoryProduct()


        binding.swOwnAccount.setOnCheckedChangeListener { buttonView, isChecked ->
            // Responds to switch being checked/unchecked
            isMyAccountCode = if (isChecked) "1" else "0"
        }
        binding.btnContinue.setOnClickListener {
            //we will check this later
            val productName = binding.textInputProductName.text.toString()
            val productDesc = binding.textInputProductDesc.text.toString()
            val accountNumber = binding.textInputProductAccountNumber.text.toString()
            when {
                productName.isEmpty() -> {
                    binding.textInputProductNameLayout.error =
                        "Product name must be filled"
                }

                productDesc.isEmpty() -> {
                    binding.textInputProductDescLayout.error =
                        "Product description must be filled"
                }

                accountNumber.isEmpty() -> {
                    binding.textInputProductAccountNumberLayout.error =
                        "Account number name must be filled"
                }

                else -> {
                    findNavController().navigate(
                        FragmentAddProductFirstStepDirections.actionFragmentAddProductFirstStepToFragmentAddProductSecondStep(
                            passedProductName = productName,
                            passedProductDesc = productDesc,
                            passedBankCode = bankCode,
                            passedProductAccountNumber = accountNumber,
                            passedOwnAccount = isMyAccountCode,
                            passedCategory = selectedCategory,
                            passedCategoryId = selectedCategoryId
                        )
                    )
                }
            }
        }

        consumeSuspend {
            viewModel.listMiniImageData.collect { data ->
                epoxyAddProductImageController.setData(data)
            }
        }

    }

    private fun observeCategoryProduct() {
        consumeSuspend {
            addProductViewModelFirstStep.categoryProductUiState.collect {
                when {
                    it.errorMessage.isNotEmpty() -> {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle(resources.getString(R.string.error_token_dialog_title))
                            .setMessage(it.errorMessage)
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                                dialog.dismiss()
                            }.show()
                    }

                    it.data != null -> {
                        if (it.data.listOfCategoryProduct.isNotEmpty()) {
                            listCategoryProduct = it.data.listOfCategoryProduct
                        }
                        if (it.data.listOfCategoryProduct.map { category -> category.categoryName }
                                .isNotEmpty()) {
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                it.data.listOfCategoryProduct.map { category -> category.categoryName }
                            ).also { adapter ->
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                // Apply the adapter to the spinner
                                binding.spCategoryTypeSelection.adapter = adapter
                            }

                            binding.spCategoryTypeSelection.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {
                                        val selectedPosition =
                                            listCategoryProduct.indexOfFirst { product ->
                                                product.categoryName == parent?.getItemAtPosition(
                                                    position
                                                ).toString()
                                            }
                                        selectedCategoryId =
                                            listCategoryProduct[selectedPosition].categoryId
                                        selectedCategory =
                                            parent?.getItemAtPosition(position).toString()
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                    }

                                }
                        }

                    }
                }
            }
        }
    }

    private fun observeBackStackValue() {
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
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        val selectedPosition = listBankResponse.indexOfFirst {
            it.bankName == parent.getItemAtPosition(pos).toString()
        }
        if (listBankResponse[selectedPosition].bankCode != null) {
            bankCode = listBankResponse[selectedPosition].bankCode!!
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

    private fun observeRefreshToken() {
        consumeSuspend {
            addProductViewModelFirstStep.refreshTokenUiState.collect {
                when (it.successState) {
                    RefreshTokenAddProductUiState.SuccessState.Initialize -> {}
                    RefreshTokenAddProductUiState.SuccessState.Failed -> customDialog.dismiss()
                    RefreshTokenAddProductUiState.SuccessState.Success -> {
                        customDialog.show()
                        addProductViewModelFirstStep.getBankData()
                    }

                }
                if (it.errorMessage.isNotEmpty()) {
                    customDialog.dismiss()
                }
            }
        }
    }

    private fun setupSpinnerFromService() {
        consumeSuspend {
            addProductViewModelFirstStep.bankUiState.collect {

                if (it.data.isNotEmpty()) {
                    listBankResponse = it.data

                    val selectedPosition = it.data.indexOfFirst { bank ->
                        bank.bankName == addProductViewModelFirstStep.getBankName()
                    }

                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        it.data.map { bankData -> bankData.bankName }
                    ).also { adapter ->
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        // Apply the adapter to the spinner
                        binding.spBankSelection.adapter = adapter
                        binding.spBankSelection.setSelection(selectedPosition)
                    }
                }
                when (it.successState) {
                    AddProductBankUiState.SuccessState.Initialize -> {
                        customDialog.show()
                    }

                    AddProductBankUiState.SuccessState.Success -> {
                        customDialog.dismiss()
                    }

                    AddProductBankUiState.SuccessState.Error -> {
                        customDialog.dismiss()
                    }

                    AddProductBankUiState.SuccessState.RefreshToken -> {
                        customDialog.dismiss()
                        addProductViewModelFirstStep.refreshToken(
                            email = addProductViewModelFirstStep.getUserEmail(),
                        )
                    }
                }
            }
        }
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
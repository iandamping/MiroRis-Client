package com.spe.miroris.feature.profile.edit

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
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.spe.miroris.R
import com.spe.miroris.base.BaseFragmentDataBinding
import com.spe.miroris.core.data.dataSource.remote.model.response.BankResponse
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.BASE_URL
import com.spe.miroris.databinding.DialogSaveEditProfileBinding
import com.spe.miroris.databinding.FragmentEditProfileBinding
import com.spe.miroris.di.qualifier.CustomDialogQualifier
import com.spe.miroris.feature.addProduct.openCamera.CameraGalleryEnum
import com.spe.miroris.feature.profile.edit.openCameraEditProfile.DialogSelectCameraOrGalleryEditProfileDirections
import com.spe.miroris.util.layoutInflater
import com.spe.miroris.util.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import java.io.File
import javax.inject.Inject

private var PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)

@AndroidEntryPoint
class FragmentEditProfile : BaseFragmentDataBinding<FragmentEditProfileBinding>(),
    AdapterView.OnItemSelectedListener {
    companion object {
        const val EDIT_PROFILE_BACK_STACK_ENTRY_KEY =
            "key from DialogSelectCameraOrGallery to edit profile"

        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private var getFile: File? = null

    @Inject
    @CustomDialogQualifier
    lateinit var customDialog: AlertDialog

    private val args: FragmentEditProfileArgs by navArgs()

    private val editProfileViewModel: EditProfileViewModel by viewModels()

    private var _dialogEditProfileBinding: DialogSaveEditProfileBinding? = null

    private var bankCode: String = ""
    private var listBankResponse: List<BankResponse> = mutableListOf()
    private val dialogEditProfileBinding get() = _dialogEditProfileBinding!!
    private lateinit var alert: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // add the storage access permission request for Android 9 and below.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            val permissionList = PERMISSIONS_REQUIRED.toMutableList()
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            PERMISSIONS_REQUIRED = permissionList.toTypedArray()
        }
    }


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentEditProfileBinding
        get() = FragmentEditProfileBinding::inflate


    override fun initView() {
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            editProfileVm = editProfileViewModel
        }

        if (args.PassedUri != null) {
            val parsedUri = Uri.parse(args.PassedUri)
            editProfileViewModel.setPassedUri(parsedUri)
        }

        val spinner = binding.spBankSelection
        spinner.onItemSelectedListener = this

        setupDialogView()


    }

    override fun viewCreated() {
        observeEditProfile()

        observeRefreshToken()

        observePassedUri()

        setupSpinnerFromService()

        observeBackStack()

        observeEditTexts()

        binding.tvChangePassword.setOnClickListener {
            findNavController().navigate(FragmentEditProfileDirections.actionFragmentEditProfileToFragmentChangePassword())
        }

        binding.ivUserEditProfile.load("$BASE_URL${editProfileViewModel.getDefaultImage()}")

        binding.ivUserEditProfile.setOnClickListener {
            findNavController().navigate(FragmentEditProfileDirections.actionFragmentEditProfileToDialogSelectCameraOrGalleryEditProfile())
        }

        binding.btnSaveChange.setOnClickListener {
            when {
                editProfileViewModel.emailFlow.value.isEmpty() -> {
                    binding.textInputEmailEditProfileLayout.error =
                        getString(R.string.username_error)
                }

                editProfileViewModel.userNameFlow.value.isEmpty() -> {
                    binding.textInputUsernameEditProfileLayout.error =
                        getString(R.string.password_error)
                }


                editProfileViewModel.addressFlow.value.isEmpty() -> {
                    binding.textInputAddressEditProfileLayout.error =
                        getString(R.string.address_error)
                }


                editProfileViewModel.phoneNumberFlow.value.isEmpty() -> {
                    binding.textInputPhoneNumberEditProfileLayout.error =
                        getString(R.string.phone_error)
                }


                editProfileViewModel.cityFlow.value.isEmpty() -> {
                    binding.textInputCityEditProfileLayout.error =
                        getString(R.string.city_error)
                }


                editProfileViewModel.accountNumberFlow.value.isEmpty() -> {
                    binding.textInputLayout5.error = getString(R.string.acc_num_error)
                }

                else -> {
                    customDialog.show()
                    editProfileViewModel.editProfile(
                        email = editProfileViewModel.emailFlow.value,
                        username = editProfileViewModel.userNameFlow.value,
                        address = editProfileViewModel.addressFlow.value,
                        city = editProfileViewModel.cityFlow.value,
                        phoneNumber = editProfileViewModel.phoneNumberFlow.value,
                        accountNumber = editProfileViewModel.accountNumberFlow.value,
                        bankCode = bankCode,
                        file = getFile
                    )
                }
            }
        }
    }

    private fun observePassedUri() {
        consumeSuspend {
            editProfileViewModel.passedUri.collect {
                if (it != null) {
                    getFile = uriToFile(it, requireContext())
                    binding.ivUserEditProfile.load(it)
                }
            }
        }
    }

    private fun observeRefreshToken() {
        consumeSuspend {
            editProfileViewModel.refreshTokenUiState.collect {
                when (it.successState) {
                    RefreshTokenEditUiState.SuccessState.Initialize -> {}
                    RefreshTokenEditUiState.SuccessState.Failed -> customDialog.dismiss()
                    RefreshTokenEditUiState.SuccessState.SuccessForBank -> {
                        customDialog.show()
                        editProfileViewModel.getBankData()
                    }

                    RefreshTokenEditUiState.SuccessState.SuccessForEditUser -> {
                        customDialog.show()
                        editProfileViewModel.editProfile(
                            email = editProfileViewModel.emailFlow.value,
                            username = editProfileViewModel.userNameFlow.value,
                            address = editProfileViewModel.addressFlow.value,
                            city = editProfileViewModel.cityFlow.value,
                            phoneNumber = editProfileViewModel.phoneNumberFlow.value,
                            accountNumber = editProfileViewModel.accountNumberFlow.value,
                            bankCode = bankCode,
                            file = getFile
                        )
                    }
                }
                if (it.errorMessage.isNotEmpty()) {
                    customDialog.dismiss()
                    customEditProfileDialog(errorMessage = it.errorMessage, false)
                }
            }
        }
    }

    private fun observeEditProfile() {
        consumeSuspend {
            editProfileViewModel.editProfileUiState.collect {
                when (it.successState) {
                    EditProfileUiState.SuccessState.Initialize -> {}
                    EditProfileUiState.SuccessState.Success -> {
                        customDialog.dismiss()
                        customEditProfileDialog(null, true)
                    }

                    EditProfileUiState.SuccessState.Error -> {
                        customDialog.dismiss()
                    }

                    EditProfileUiState.SuccessState.RefreshToken -> {
                        editProfileViewModel.refreshToken(
                            email = editProfileViewModel.getUserEmail(),
                            call = EditProfileCaller.EditUser
                        )
                    }
                }
                if (it.errorMessage.isNotEmpty()) {
                    customDialog.dismiss()
                    customEditProfileDialog(errorMessage = it.errorMessage, false)
                }
            }
        }
    }

    private fun customEditProfileDialog(errorMessage: String?, isSuccess: Boolean) {
        if (::alert.isInitialized) {
            with(dialogEditProfileBinding) {
                ivEditProfile.load(
                    if (isSuccess) {
                        AppCompatResources.getDrawable(
                            this.root.context,
                            R.drawable.img_success_save
                        )
                    } else AppCompatResources.getDrawable(
                        this.root.context,
                        R.drawable.img_failed_save
                    )
                )

                tvEditProfileInfo.text = if (isSuccess) {
                    "Proses simpan berhasil"
                } else "Proses simpan gagal : $errorMessage"

                btnOk.setOnClickListener {
                    alert.dismiss()
                    if (isSuccess) {
                        findNavController().popBackStack()
                    }
                }
                ivCloseDialog.setOnClickListener {
                    alert.dismiss()
                    if (isSuccess) {
                        findNavController().popBackStack()
                    }
                }
            }
            alert.show()
        }
    }

    private fun observeBackStack() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(
            EDIT_PROFILE_BACK_STACK_ENTRY_KEY
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
                            findNavController().navigate(
                                DialogSelectCameraOrGalleryEditProfileDirections.actionDialogSelectCameraOrGalleryEditProfileToFragmentTakePictureEditProfile()
                            )
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

    private fun setupSpinnerFromService() {
        consumeSuspend {
            editProfileViewModel.bankUiState.collect {

                if (it.data.isNotEmpty()) {
                    listBankResponse = it.data

                    val selectedPosition = it.data.indexOfFirst { bank ->
                        bank.bankName == editProfileViewModel.getBankName()
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
                    BankUiState.SuccessState.Initialize -> {
                        customDialog.show()
                    }

                    BankUiState.SuccessState.Success -> {
                        customDialog.dismiss()
                    }

                    BankUiState.SuccessState.Error -> {
                        customDialog.dismiss()
                    }

                    BankUiState.SuccessState.RefreshToken -> {
                        customDialog.dismiss()
                        editProfileViewModel.refreshToken(
                            email = editProfileViewModel.getUserEmail(),
                            call = EditProfileCaller.Bank
                        )
                    }
                }
            }
        }
    }

    private fun observeEditTexts() {
        consumeSuspend {
            editProfileViewModel.userNameFlow.collectLatest { email ->
                if (email.startsWith(" ")) {
                    editProfileViewModel.setUsernameManually("")
                }
                if (email.isNotEmpty()) {
                    binding.textInputUsernameEditProfileLayout.error = ""
                }
            }
        }
        consumeSuspend {
            editProfileViewModel.emailFlow.collectLatest { email ->
                if (email.startsWith(" ")) {
                    editProfileViewModel.setEmailManually("")
                }
                if (email.isNotEmpty()) {
                    binding.textInputEmailEditProfileLayout.error = ""
                }
            }
        }
        consumeSuspend {
            editProfileViewModel.addressFlow.collectLatest { email ->
                if (email.startsWith(" ")) {
                    editProfileViewModel.setAddressManually("")
                }
                if (email.isNotEmpty()) {
                    binding.textInputAddressEditProfileLayout.error = ""
                }
            }
        }
        consumeSuspend {
            editProfileViewModel.phoneNumberFlow.collectLatest { email ->
                if (email.startsWith(" ")) {
                    editProfileViewModel.setPhoneNumberManually("")
                }
                if (email.isNotEmpty()) {
                    binding.textInputPhoneNumberEditProfileLayout.error = ""
                }
            }
        }
        consumeSuspend {
            editProfileViewModel.cityFlow.collectLatest { email ->
                if (email.startsWith(" ")) {
                    editProfileViewModel.setCityManually("")
                }
                if (email.isNotEmpty()) {
                    binding.textInputCityEditProfileLayout.error = ""
                }
            }
        }
        consumeSuspend {
            editProfileViewModel.accountNumberFlow.collectLatest { email ->
                if (email.startsWith(" ")) {
                    editProfileViewModel.setAccountNumberManually("")
                }
                if (email.isNotEmpty()) {
                    binding.textInputLayout5.error = ""
                }
            }
        }
    }

    private fun setupDialogView() {
        _dialogEditProfileBinding =
            DialogSaveEditProfileBinding.inflate(requireContext().layoutInflater)

        val materialDialogBuilder =
            MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_Round)
                .setView(dialogEditProfileBinding.root)

        alert = materialDialogBuilder.create().apply {
            setCancelable(true)
            setCanceledOnTouchOutside(true)
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        val selectedPosition = listBankResponse.indexOfFirst {
            it.bankName == parent.getItemAtPosition(pos).toString()
        }
        Timber.e("pos : $selectedPosition")
        Timber.e("as : ${parent.getItemAtPosition(pos).toString()}")
        if (listBankResponse[selectedPosition].bankCode!=null){
            bankCode = listBankResponse[selectedPosition].bankCode!!

        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _dialogEditProfileBinding = null
    }


    private val intentGalleryLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            try {
                val intentResult = result.data?.data
                editProfileViewModel.setPassedUri(intentResult)
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
                findNavController().navigate(FragmentEditProfileDirections.actionFragmentEditProfileToFragmentTakePictureEditProfile())
            }
        }

}
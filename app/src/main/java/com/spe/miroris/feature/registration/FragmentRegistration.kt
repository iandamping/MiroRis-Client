package com.spe.miroris.feature.registration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.spe.miroris.R
import com.spe.miroris.base.BaseFragmentDataBinding
import com.spe.miroris.databinding.FragmentRegistrationBinding
import com.spe.miroris.di.qualifier.CustomDialogQualifier
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class FragmentRegistration : BaseFragmentDataBinding<FragmentRegistrationBinding>() {
    @Inject
    @CustomDialogQualifier
    lateinit var customDialog: AlertDialog

    private val registrationViewModel: RegistrationViewModel by viewModels()
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentRegistrationBinding
        get() = FragmentRegistrationBinding::inflate

    override fun initView() {
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            registerVm = registrationViewModel
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        registrationViewModel.resetState()
    }

    override fun viewCreated() {

        consumeSuspend {
            registrationViewModel.registrationUiState.collect {
                when (it.successState) {
                    RegistrationUiState.SuccessState.Initialize -> {}
                    RegistrationUiState.SuccessState.Success -> {
                        customDialog.dismiss()
                        registrationViewModel.resetState()
                        registrationViewModel.setEmailManually("")
                        registrationViewModel.setPasswordManually("")
                        registrationViewModel.setConfirmPasswordManually("")
                        findNavController().navigate(FragmentRegistrationDirections.actionFragmentRegistrationToFragmentHome())

                    }

                    RegistrationUiState.SuccessState.Failed -> {
                        registrationViewModel.resetState()
                        customDialog.dismiss()
                    }

                    RegistrationUiState.SuccessState.RefreshToken -> {
                        registrationViewModel.resetState()
                        customDialog.dismiss()
                    }
                }
                if (it.errorMessage.isNotEmpty()) {
                    registrationViewModel.resetState()
                    customDialog.dismiss()
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.error_token_dialog_title))
                        .setMessage(it.errorMessage)
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                            dialog.dismiss()
                        }.show()
                    registrationViewModel.setEmailManually("")
                    registrationViewModel.setPasswordManually("")
                    registrationViewModel.setConfirmPasswordManually("")
                }
            }

        }
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(FragmentRegistrationDirections.actionFragmentRegistrationToFragmentLogin())
        }

        binding.btnRegister.setOnClickListener {
            when {
                registrationViewModel.emailFlow.value.isEmpty() -> {
                    binding.textInputEmailRegistrationLayout.error =
                        getString(R.string.username_error)
                }

                registrationViewModel.passwordFlow.value.isEmpty() -> {
                    binding.textInputPasswordRegistrationLayout.error =
                        getString(R.string.password_error)
                }

                registrationViewModel.confirmPasswordFlow.value.isEmpty() -> {
                    binding.textInputConfirmPasswordRegistrationLayout.error =
                        getString(R.string.cfm_password_error)
                }

                else -> {
                    customDialog.show()
                    registrationViewModel.register(
                        email = registrationViewModel.emailFlow.value,
                        password = registrationViewModel.passwordFlow.value,
                        cfmPassword = registrationViewModel.confirmPasswordFlow.value
                    )
                }
            }
        }

        consumeSuspend {
            registrationViewModel.emailFlow.collectLatest { email ->
                if (email.startsWith(" ")) {
                    registrationViewModel.setEmailManually("")
                }
                if (email.isNotEmpty()) {
                    binding.textInputEmailRegistrationLayout.error = ""
                }
            }
        }


        consumeSuspend {
            registrationViewModel.passwordFlow.collectLatest { password ->
                if (password.startsWith(" ")) {
                    registrationViewModel.setPasswordManually("")
                }
                if (password.isNotEmpty()) {
                    binding.textInputPasswordRegistrationLayout.error = ""
                }
            }
        }


        consumeSuspend {
            registrationViewModel.confirmPasswordFlow.collectLatest { password ->
                if (password.startsWith(" ")) {
                    registrationViewModel.setConfirmPasswordManually("")
                }
                if (password.isNotEmpty()) {
                    binding.textInputConfirmPasswordRegistrationLayout.error = ""
                }
            }
        }
    }
}
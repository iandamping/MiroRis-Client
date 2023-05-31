package com.spe.miroris.feature.profile.changePassword

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.spe.miroris.R
import com.spe.miroris.base.BaseFragmentDataBinding
import com.spe.miroris.databinding.FragmentChangePasswordBinding
import com.spe.miroris.di.qualifier.CustomDialogQualifier
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


@AndroidEntryPoint
class FragmentChangePassword : BaseFragmentDataBinding<FragmentChangePasswordBinding>() {

    @Inject
    @CustomDialogQualifier
    lateinit var customDialog: AlertDialog

    private val changePassVm: ChangePasswordViewModel by viewModels()
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentChangePasswordBinding
        get() = FragmentChangePasswordBinding::inflate

    override fun initView() {
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            changePasswordVm = changePassVm
        }
    }

    override fun viewCreated() {
        consumeSuspend {
            changePassVm.currentPasswordFlow.collectLatest { email ->
                if (email.startsWith(" ")) {
                    changePassVm.setCurrentPasswordManually("")
                }
                if (email.isNotEmpty()) {
                    binding.textInputPasswordChangePasswordLayout.error = ""
                }
            }
        }
        consumeSuspend {
            changePassVm.newPasswordFlow.collectLatest { email ->
                if (email.startsWith(" ")) {
                    changePassVm.setNewPasswordManually("")
                }
                if (email.isNotEmpty()) {
                    binding.textInputNewPasswordChangePasswordLayout.error = ""
                }
            }
        }
        consumeSuspend {
            changePassVm.confirmPasswordFlow.collectLatest { email ->
                if (email.startsWith(" ")) {
                    changePassVm.setConfirmPasswordManually("")
                }
                if (email.isNotEmpty()) {
                    binding.textInputConfirmPasswordChangePasswordLayout.error = ""
                }
            }
        }

        binding.btnSave.setOnClickListener {
            when {
                changePassVm.currentPasswordFlow.value.isEmpty() -> {
                    binding.textInputPasswordChangePasswordLayout.error =
                        getString(R.string.current_password_error)
                }

                changePassVm.newPasswordFlow.value.isEmpty() -> {
                    binding.textInputNewPasswordChangePasswordLayout.error =
                        getString(R.string.new_password_error)
                }


                changePassVm.confirmPasswordFlow.value.isEmpty() -> {
                    binding.textInputConfirmPasswordChangePasswordLayout.error =
                        getString(R.string.confirm_password)
                }


                else -> {
                    customDialog.show()
                    changePassVm.changePassword(
                        currentPassword = changePassVm.currentPasswordFlow.value,
                        newPassword = changePassVm.newPasswordFlow.value,
                        confirmPassword = changePassVm.confirmPasswordFlow.value
                    )
                }
            }
        }

        consumeSuspend {
            changePassVm.changePasswordUiState.collect {
                when (it.successState) {
                    ChangePasswordUiState.SuccessState.Initialize -> {}
                    ChangePasswordUiState.SuccessState.Success -> {
                        customDialog.dismiss()
                        findNavController().navigate(FragmentChangePasswordDirections.actionFragmentChangePasswordToFragmentHome())
                    }

                    ChangePasswordUiState.SuccessState.Failed -> {
                        customDialog.dismiss()
                    }

                    ChangePasswordUiState.SuccessState.RefreshToken -> {
                        customDialog.dismiss()
                        changePassVm.refreshToken(changePassVm.getUserEmail())
                    }
                }
                if (it.errorMessage.isNotEmpty()) {
                    customDialog.dismiss()
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.error_token_dialog_title))
                        .setMessage(it.errorMessage)
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                            dialog.dismiss()
                        }.show()
                }
            }
        }

        consumeSuspend {
            changePassVm.refreshTokenUiState.collect {
                when (it.successState) {
                    RefreshTokenChangePasswordUiState.SuccessState.Initialize -> {}
                    RefreshTokenChangePasswordUiState.SuccessState.Success -> {
                        customDialog.show()
                        changePassVm.changePassword(
                            currentPassword = changePassVm.currentPasswordFlow.value,
                            newPassword = changePassVm.newPasswordFlow.value,
                            confirmPassword = changePassVm.confirmPasswordFlow.value
                        )
                    }

                    RefreshTokenChangePasswordUiState.SuccessState.Failed -> {
                        customDialog.dismiss()
                    }
                }

                if (it.errorMessage.isNotEmpty()) {
                    customDialog.dismiss()
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.error_token_dialog_title))
                        .setMessage(it.errorMessage)
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                            dialog.dismiss()
                        }.show()
                }
            }
        }
    }
}
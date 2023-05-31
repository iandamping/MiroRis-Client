package com.spe.miroris.feature.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.spe.miroris.R
import com.spe.miroris.base.BaseFragmentDataBinding
import com.spe.miroris.databinding.FragmentLoginBinding
import com.spe.miroris.di.qualifier.CustomDialogQualifier
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class FragmentLogin : BaseFragmentDataBinding<FragmentLoginBinding>() {

    @Inject
    @CustomDialogQualifier
    lateinit var customDialog: AlertDialog

    private val viewModel: LoginViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLoginBinding
        get() = FragmentLoginBinding::inflate

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.resetState()
    }

    override fun initView() {
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            loginViewModel = viewModel
        }
        consumeSuspend {
            viewModel.loginUiState.collect {
                when (it.successState) {
                    LoginUiState.SuccessState.Initialize -> {}
                    LoginUiState.SuccessState.Success -> {
                        customDialog.dismiss()
                        viewModel.resetState()
                        viewModel.setEmailManually("")
                        viewModel.setPasswordManually("")
                        findNavController().navigate(FragmentLoginDirections.actionFragmentLoginToFragmentHome())
                    }

                    LoginUiState.SuccessState.Failed -> {
                        viewModel.resetState()
                        customDialog.dismiss()
                    }

                    LoginUiState.SuccessState.RefreshToken -> {
                        viewModel.resetState()
                        customDialog.dismiss()
                    }
                }


                if (it.errorMessage.isNotEmpty()) {
                    viewModel.resetState()
                    customDialog.dismiss()
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.error_token_dialog_title))
                        .setMessage(it.errorMessage)
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                            dialog.dismiss()
                        }.show()
                    viewModel.setEmailManually("")
                    viewModel.setPasswordManually("")
                }
            }

        }
    }

    override fun viewCreated() {
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(FragmentLoginDirections.actionFragmentLoginToFragmentRegistration())
        }

        binding.btnRegister.setOnClickListener {
            when {
                viewModel.emailFlow.value.isEmpty() -> {
                    binding.textInputEmailLoginLayout.error = getString(R.string.username_error)
                }

                viewModel.passwordFlow.value.isEmpty() -> {
                    binding.textInputPasswordLoginLayout.error = getString(R.string.password_error)
                }

                else -> {
                    customDialog.show()
                    viewModel.login(
                        email = viewModel.emailFlow.value,
                        password = viewModel.passwordFlow.value
                    )
                }
            }
        }

        consumeSuspend {
            viewModel.emailFlow.collectLatest { email ->
                if (email.startsWith(" ")) {
                    viewModel.setEmailManually("")
                }
                if (email.isNotEmpty()) {
                    binding.textInputEmailLoginLayout.error = ""
                }
            }
        }


        consumeSuspend {
            viewModel.passwordFlow.collectLatest { password ->
                if (password.startsWith(" ")) {
                    viewModel.setPasswordManually("")
                }
                if (password.isNotEmpty()) {
                    binding.textInputPasswordLoginLayout.error = ""
                }
            }
        }
    }
}
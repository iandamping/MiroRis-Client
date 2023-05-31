package com.spe.miroris.feature.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.spe.miroris.R
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.BASE_URL
import com.spe.miroris.databinding.FragmentProfileBinding
import com.spe.miroris.di.qualifier.CustomDialogQualifier
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FragmentProfile : BaseFragmentViewBinding<FragmentProfileBinding>() {

    @Inject
    @CustomDialogQualifier
    lateinit var customDialog: AlertDialog

    private val profileViewModel: ProfileViewModel by viewModels()
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProfileBinding
        get() = FragmentProfileBinding::inflate

    override fun onDestroyView() {
        super.onDestroyView()
        profileViewModel.resetState()
    }

    override fun initView() {
        profileViewModel.getUserProfile(profileViewModel.getUserEmail())


        consumeSuspend {
            profileViewModel.profileUiState.collect {
                when (it.successState) {
                    ProfileUiState.SuccessState.Initialize -> {}
                    ProfileUiState.SuccessState.Success -> {
                        customDialog.dismiss()
                    }

                    ProfileUiState.SuccessState.Error -> {
                        customDialog.dismiss()
                    }

                    ProfileUiState.SuccessState.RefreshToken -> {
                        customDialog.dismiss()
                        profileViewModel.refreshToken(
                            email = profileViewModel.getUserEmail(),
                            caller = ProfileCaller.Profile
                        )
                    }
                }
                when {
                    it.errorMessage.isNotEmpty() -> {
                        customDialog.dismiss()
                        profileViewModel.resetState()
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
        consumeSuspend {
            profileViewModel.logoutUiState.collect {
                when (it.successState) {
                    LogoutUiState.SuccessState.Initialize -> {}
                    LogoutUiState.SuccessState.Success -> {
                        customDialog.dismiss()
                        profileViewModel.resetState()
                        findNavController().navigate(FragmentProfileDirections.actionFragmentProfileToFragmentHome())
                    }

                    LogoutUiState.SuccessState.Failed -> {
                        customDialog.dismiss()
                        profileViewModel.resetState()
                    }
                    LogoutUiState.SuccessState.RefreshToken -> {
                        profileViewModel.resetState()
                        profileViewModel.refreshToken(
                            email = profileViewModel.getUserEmail(),
                            caller = ProfileCaller.Logout
                        )
                    }
                }
                when {
                    it.errorMessage.isNotEmpty() -> {
                        customDialog.dismiss()
                        profileViewModel.resetState()
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

        consumeSuspend {
            profileViewModel.refreshTokenUiState.collect {
                when (it.successState) {
                    RefreshTokenLogoutUiState.SuccessState.Initialize -> {}

                    RefreshTokenLogoutUiState.SuccessState.SuccessLogout -> {
                        customDialog.show()
                        profileViewModel.resetState()
                        profileViewModel.userLogout(profileViewModel.getUserEmail())
                    }

                    RefreshTokenLogoutUiState.SuccessState.SuccessProfile -> {
                        customDialog.show()
                        profileViewModel.resetState()
                        profileViewModel.getUserProfile(profileViewModel.getUserEmail())
                    }

                    RefreshTokenLogoutUiState.SuccessState.Failed -> {
                        customDialog.dismiss()
                        profileViewModel.resetState()
                    }
                }
                when {
                    it.errorMessage.isNotEmpty() -> {
                        customDialog.dismiss()
                        profileViewModel.resetState()
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

    override fun viewCreated() {
        consumeSuspend {
            profileViewModel.getUserEmailFlow().collect { email ->
                binding.tvUserEmail.text = email
            }
        }
        consumeSuspend {
            profileViewModel.getUserNameFlow().collect { userName ->
                binding.tvUserName.text = userName

            }
        }

        consumeSuspend {
            profileViewModel.getUserImageProfileFlow().collect { url ->
                if (url.isEmpty()) {
                    binding.tvFirstUserText.isVisible = true
                    binding.tvFirstUserText.text =
                        profileViewModel.getUserEmail().first().toString()
                    binding.ivUserProfile.setBackgroundColor(requireContext().getColor(R.color.grey_divider))
                } else {
                    binding.tvFirstUserText.isVisible = false
                    binding.ivUserProfile.load("$BASE_URL$url") {
                        this.error(R.drawable.ic_no_data)
                    }

                }
            }
        }

        binding.ivInvestment.setOnClickListener {
            findNavController().navigate(FragmentProfileDirections.actionFragmentProfileToFragmentInvestmentManagement())
        }
        binding.ivFunding.setOnClickListener {
            findNavController().navigate(FragmentProfileDirections.actionFragmentProfileToFragmentFundManagement())
        }
        binding.ivProduct.setOnClickListener {
            findNavController().navigate(FragmentProfileDirections.actionFragmentProfileToFragmentProductManagement())
        }

        binding.layerLogout.setOnClickListener {
            customDialog.show()
            profileViewModel.userLogout(profileViewModel.getUserEmail())
        }
        binding.layerChangePassword.setOnClickListener {
            findNavController().navigate(FragmentProfileDirections.actionFragmentProfileToFragmentChangePassword())

        }
        binding.layerEditProfile.setOnClickListener {
            findNavController().navigate(
                FragmentProfileDirections.actionFragmentProfileToFragmentEditProfile(
                    null
                )
            )
        }
    }
}
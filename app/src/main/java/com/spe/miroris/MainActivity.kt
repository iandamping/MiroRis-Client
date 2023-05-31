package com.spe.miroris

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.spe.miroris.databinding.ActivityMainBinding
import com.spe.miroris.feature.token.TokenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var keepScreen: Boolean = true
    private val tokenVm: TokenViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        observeToken()
        // Keep the splash screen visible for this Activity
        // false will make screen change, true will hold splash
        splashScreen.setKeepOnScreenCondition { keepScreen }


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.fcMain.id) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbarHome.setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.fragmentHome -> binding.toolbarHome.visibility = View.GONE
                R.id.fragmentTakePicture -> binding.toolbarHome.visibility = View.GONE
                R.id.fragmentTakePictureEditProfile -> binding.toolbarHome.visibility = View.GONE
                R.id.fragmentEditProductTakePicture -> binding.toolbarHome.visibility = View.GONE
                R.id.dialogFragmentFilter -> binding.toolbarHome.visibility = View.GONE
                else -> {
                    lifecycleScope.launch {
                        binding.toolbarHome.visibility = View.VISIBLE
                    }
                }
            }
        }
    }


    private fun observeToken() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tokenVm.tokenUiState.collect {
                    if (it.isSuccess) {
                        keepScreen = false
                    }
                    if (it.errorMessage.isNotEmpty()) {
                        keepScreen = false
                        errorTokenDialog(it.errorMessage)
                    }
                }
            }
        }
    }

    private fun errorTokenDialog(errorMessage: String) {
        MaterialAlertDialogBuilder(this@MainActivity)
            .setTitle(resources.getString(R.string.error_token_dialog_title))
            .setMessage(errorMessage)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                dialog.dismiss()
                finishAndRemoveTask()
            }.show()
    }
}
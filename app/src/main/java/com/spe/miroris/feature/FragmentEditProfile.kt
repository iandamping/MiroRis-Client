package com.spe.miroris.feature

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.spe.miroris.R
import com.spe.miroris.base.BaseFragmentDataBinding
import com.spe.miroris.databinding.DialogSaveEditProfileBinding
import com.spe.miroris.databinding.FragmentEditProfileBinding
import com.spe.miroris.util.layoutInflater

class FragmentEditProfile : BaseFragmentDataBinding<FragmentEditProfileBinding>(),
    AdapterView.OnItemSelectedListener {

    private var _dialogEditProfileBinding: DialogSaveEditProfileBinding? = null
    private val dialogEditProfileBinding get() = _dialogEditProfileBinding!!
    private lateinit var alert: AlertDialog


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentEditProfileBinding
        get() = FragmentEditProfileBinding::inflate


    override fun initView() {
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
        setupDialogView()

        binding.btnSaveChange.setOnClickListener {
            if (::alert.isInitialized) {
                //todo : manipulasi ini berdasarkan data dari service. ini hanya untuk melihat dialog nya saja
                with(dialogEditProfileBinding) {
                    ivEditProfile.load(
                        AppCompatResources.getDrawable(
                            this.root.context,
                            R.drawable.img_failed_save
                        )
                    )
                    tvEditProfileInfo.text = "Proses simpan gagal"
                    btnOk.setOnClickListener {
                        alert.dismiss()
                    }
                    ivCloseDialog.setOnClickListener {
                        alert.dismiss()
                    }
                }
                alert.show()
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
        // parent.getItemAtPosition(pos)

    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _dialogEditProfileBinding = null
    }

}
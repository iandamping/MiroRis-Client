package com.spe.miroris.feature.addProduct

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.spe.miroris.R
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.databinding.FragmentAddProductSecondStepBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class FragmentAddProductSecondStep :
    BaseFragmentViewBinding<FragmentAddProductSecondStepBinding>() {

    companion object {
        private const val START_DATE = "start-date"
        private const val END_DATE = "end-date"
    }

    private val dateCalendar: Date = Calendar.getInstance().time

    private val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private val viewModel: SharedAddProductViewModel by activityViewModels()

    private val addProductViewModelSecondStep: AddProductViewModelSecondStep by viewModels()

    private var productStatusCode: String = "1"
    private var productFundId: Int = 0
    private var productFund: String = ""
    private var productTypeId: Int = 0
    private var productType: String = ""
    private var productDuration: String = ""
    private var productDurationId: Int = 0
    private var selectedStartFunding: String = ""
    private var selectedEndFunding: String = ""

    private val args: FragmentAddProductSecondStepArgs by navArgs()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAddProductSecondStepBinding
        get() = FragmentAddProductSecondStepBinding::inflate

    override fun initView() {
        binding.textInputStartDateFund.hint = dateString.format(dateCalendar)
        binding.textInputEndDateFund.hint = dateString.format(dateCalendar)

        val spinnerFund = binding.spFundSelection
        val spinnerDuration = binding.spFundDurationSelection
        val spinnerType = binding.spFundTypeSelection
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.default_fund_selection,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerFund.adapter = adapter
        }
        spinnerFund.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                productFundId = position + 1
                productFund = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.default_duration_fund_selection,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerDuration.adapter = adapter
        }
        spinnerDuration.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                productDurationId = position + 1
                productDuration = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.default_type_selection,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerType.adapter = adapter
        }
        spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                productTypeId = position + 1
                productType = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    override fun viewCreated() {
        val startFundDatePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.starting_fund))
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        val endFundDatePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.ending_fund))
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        binding.swProductStatus.setOnCheckedChangeListener { buttonView, isChecked ->
            // Responds to switch being checked/unchecked
            productStatusCode = if (isChecked) "1" else "0"
        }
        binding.textInputEndDateFund.setOnClickListener {
            endFundDatePicker.show(childFragmentManager, END_DATE)
        }
        binding.textInputStartDateFund.setOnClickListener {
            startFundDatePicker.show(childFragmentManager, START_DATE)
        }

        startFundDatePicker.addOnPositiveButtonClickListener { value ->
            selectedStartFunding = dateString.format(value)
            binding.textInputStartDateFund.setText(dateString.format(value))
        }
        endFundDatePicker.addOnPositiveButtonClickListener { value ->
            selectedEndFunding = dateString.format(value)
            binding.textInputEndDateFund.setText(dateString.format(value))
        }

        binding.btnSave.setOnClickListener {
            addProductViewModelSecondStep.createProduct(
                categoryId = args.passedCategoryId,
                email = addProductViewModelSecondStep.getUserEmail(),
                productName = args.passedProductName,
                productDetail = args.passedProductDesc,
                productDuration = productDurationId.toString(),
                productTypePayment = productTypeId.toString(),
                productType = productFundId.toString(),
                productStartFunding = selectedStartFunding,
                productFinishFunding = selectedEndFunding,
                productBankCode = args.passedBankCode,
                productAccountNumber = args.passedProductAccountNumber,
                personalAccount = args.passedOwnAccount,
                productStatus = productStatusCode
            )
        }



        consumeSuspend {
            addProductViewModelSecondStep.createProductUiState.collect {
                when (it.successState) {
                    AddCreateProductUiState.SuccessState.Initialize -> {

                    }

                    AddCreateProductUiState.SuccessState.Success -> {
                        findNavController().navigate(FragmentAddProductSecondStepDirections.actionFragmentAddProductSecondStepToFragmentProductManagement())
                    }

                    AddCreateProductUiState.SuccessState.Error -> {
                        if (it.errorMessage.isNotEmpty()) {
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle(resources.getString(R.string.error_token_dialog_title))
                                .setMessage(it.errorMessage)
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                                    dialog.dismiss()
                                }.show()
                        }
                    }

                    AddCreateProductUiState.SuccessState.RefreshToken -> {

                    }
                }
            }
        }

    }

}
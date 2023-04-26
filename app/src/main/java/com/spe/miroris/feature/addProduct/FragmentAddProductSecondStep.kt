package com.spe.miroris.feature.addProduct

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.spe.miroris.R
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.databinding.FragmentAddProductSecondStepBinding
import java.text.SimpleDateFormat
import java.util.*

class FragmentAddProductSecondStep :
    BaseFragmentViewBinding<FragmentAddProductSecondStepBinding>() {

    companion object {
        private const val START_DATE = "start-date"
        private const val END_DATE = "end-date"
    }

    private val dateCalendar: Date = Calendar.getInstance().time

    private val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private val viewModel: AddProductFirstStepViewModel by activityViewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAddProductSecondStepBinding
        get() = FragmentAddProductSecondStepBinding::inflate

    override fun initView() {
        binding.textInputStartDateFund.hint = dateString.format(dateCalendar)
        binding.textInputEndDateFund.hint = dateString.format(dateCalendar)
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
        binding.textInputEndDateFund.setOnClickListener {
            endFundDatePicker.show(childFragmentManager, END_DATE)
        }
        binding.textInputStartDateFund.setOnClickListener {
            startFundDatePicker.show(childFragmentManager, START_DATE)
        }

        startFundDatePicker.addOnPositiveButtonClickListener { value ->
            binding.textInputStartDateFund.setText(dateString.format(value))
        }
        endFundDatePicker.addOnPositiveButtonClickListener { value ->
            binding.textInputEndDateFund.setText(dateString.format(value))
        }

    }

}
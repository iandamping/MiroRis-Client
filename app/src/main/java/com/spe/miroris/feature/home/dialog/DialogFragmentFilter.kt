package com.spe.miroris.feature.home.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.spe.miroris.databinding.DialogFilterBinding
import com.spe.miroris.feature.home.FragmentHome.Companion.SELECTED_FILTER_KEY
import com.spe.miroris.feature.home.common.ParcelableSelectedFilterData

class DialogFragmentFilter : BottomSheetDialogFragment() {

    private var _binding: DialogFilterBinding? = null
    private val binding: DialogFilterBinding get() = _binding!!
    private var selectedCategoryId: String = ""
    private var selectedCategory: String = ""
    private var selectedFunds: String = ""

    private val args: DialogFragmentFilterArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvSelectedCategory.text = args.SavedSelectedCategory.ifEmpty { "" }
        binding.tvSelectFund.text = args.SavedSelectedFund.ifEmpty { "" }

        selectedCategory = args.SavedSelectedCategory.ifEmpty { "" }
        selectedFunds = args.SavedSelectedFund.ifEmpty { "" }

        binding.ivCloseDialog.setOnClickListener {
            dismiss()
        }

        binding.btnApplyFilter.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                SELECTED_FILTER_KEY, ParcelableSelectedFilterData(
                    selectedCategoryId = selectedCategoryId,
                    selectedCategory = selectedCategory,
                    selectedFunds = selectedFunds
                )
            )
            dismiss()
        }

        binding.btnResetFilter.setOnClickListener {
            selectedCategoryId = ""
            selectedCategory = ""
            selectedFunds = ""
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                SELECTED_FILTER_KEY, ParcelableSelectedFilterData(
                    selectedCategoryId = "",
                    selectedCategory = "",
                    selectedFunds = ""
                )
            )
            dismiss()
        }

        binding.llCategory.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Category")
                .setItems(args.FilterData.listOfCategoryProduct.map { it.categoryName }
                    .toTypedArray()) { dialog, which ->
                    // Respond to item chosen
                    binding.tvSelectedCategory.text =
                        args.FilterData.listOfCategoryProduct[which].categoryName
                    selectedCategory = args.FilterData.listOfCategoryProduct[which].categoryName
                    selectedCategoryId = args.FilterData.listOfCategoryProduct[which].categoryId
                    dialog.dismiss()
                }
                .show()

        }

        binding.llFunds.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Jenis Pendanaan")
                .setItems(args.FilterData.listOfFundType.toTypedArray()) { dialog, which ->
                    // Respond to item chosen
                    binding.tvSelectFund.text = args.FilterData.listOfFundType[which]
                    selectedFunds = args.FilterData.listOfFundType[which]
                    dialog.dismiss()
                }
                .show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
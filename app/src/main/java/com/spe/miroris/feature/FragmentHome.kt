package com.spe.miroris.feature

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import com.spe.miroris.R
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.databinding.FragmentHomeBinding

class FragmentHome : BaseFragmentViewBinding<FragmentHomeBinding>(),
    AdapterView.OnItemSelectedListener, SearchView.OnQueryTextListener {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding
        get() = FragmentHomeBinding::inflate


    override fun initView() {
        val toolbar = binding.toolbarHome
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_to_profile -> {
                    findNavController().navigate(FragmentHomeDirections.actionFragmentHomeToFragmentProfile())
                    true
                }
                else -> {
                    false
                }
            }
        }
        val spinner = binding.spHomeCategory
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.default_product_category,
            android.R.layout.simple_spinner_item
        ).also { adapter ->

            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this

        val searchView = binding.svHomeProduct
        searchView.setOnQueryTextListener(this)
    }

    override fun viewCreated() {
        binding.tvHomeFilterProduct.setOnClickListener {
            findNavController().navigate(FragmentHomeDirections.actionFragmentHomeToFragmentProductManagement())
        }

    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        // do something on text submit
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        // do something when text changes
        return false
    }

}
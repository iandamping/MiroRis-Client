package com.spe.miroris.feature.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.spe.miroris.R
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.core.domain.model.CategoryProduct
import com.spe.miroris.core.domain.model.ProductCatalog
import com.spe.miroris.databinding.FragmentHomeBinding
import com.spe.miroris.feature.home.banner.EpoxyMultiProductBannerController
import com.spe.miroris.feature.home.common.ParcelableFilterData
import com.spe.miroris.feature.home.common.ParcelableSelectedFilterData
import com.spe.miroris.feature.home.product.EpoxyMultiProductHomeController
import com.spe.miroris.feature.home.product.MultiAdapterProductHomeData
import com.spe.miroris.feature.home.state.ProductCatalogUiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentHome : BaseFragmentViewBinding<FragmentHomeBinding>(),
    AdapterView.OnItemSelectedListener, SearchView.OnQueryTextListener,
    EpoxyMultiProductHomeController.EpoxyProductHomeClickListener {

    private val epoxyMultiProductBannerController: EpoxyMultiProductBannerController by lazy {
        EpoxyMultiProductBannerController()
    }

    private val epoxyMultiProductHomeController: EpoxyMultiProductHomeController by lazy {
        EpoxyMultiProductHomeController(this)
    }

    companion object {
        const val SELECTED_FILTER_KEY = "selected_filter"
    }

    private val homeViewModel: HomeViewModel by viewModels()
    private var parcelableFilterData: ParcelableFilterData? = null
    private var listCategoryProduct: List<CategoryProduct> = mutableListOf()
    private var listProductCatalog: List<ProductCatalog> = mutableListOf()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding
        get() = FragmentHomeBinding::inflate


    override fun initView() {
        val toolbar = binding.toolbarHome
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_to_profile -> {
                    if (homeViewModel.getUserEmail().isEmpty()) {
                        findNavController().navigate(FragmentHomeDirections.actionFragmentHomeToFragmentLogin())
                    } else {
                        findNavController().navigate(FragmentHomeDirections.actionFragmentHomeToFragmentProfile())
                    }
                    true
                }

                else -> {
                    false
                }
            }
        }
        val spinner = binding.spHomeCategory
        spinner.onItemSelectedListener = this

        val searchView = binding.svHomeProduct
        searchView.setOnQueryTextListener(this)


        with(binding.rvHomeBanner) {
            setController(epoxyMultiProductBannerController)
            LinearSnapHelper().attachToRecyclerView(this)
        }


        with(binding.rvHomeProduct) {
            setController(epoxyMultiProductHomeController)
            layoutManager = GridLayoutManager(this.context, 2)
            LinearSnapHelper().attachToRecyclerView(this)
        }
    }

    override fun viewCreated() {

        ProcessCameraProvider.getInstance(requireContext())

        observeEpoxyBannerData()

        observeEpoxyProductData()

        observeSelectedFilterFromDialogFragmentFilter()

        getCategoryProduct()

        getProductCatalog()

        consumeSuspend {
            homeViewModel.getSelectedCategoryIdFlow().collect { categoryId ->
                homeViewModel.getProductCatalog(
                    page = "1",
                    limit = "20",
                    productName = "",
                    categoryId = categoryId,
                    productType = homeViewModel.getSelectedFund()
                )
            }
        }

        binding.tvHomeFilterProduct.setOnClickListener {
            if (parcelableFilterData != null) {
                findNavController().navigate(
                    FragmentHomeDirections.actionFragmentHomeToDialogFragmentFilter(
                        parcelableFilterData!!,
                        homeViewModel.getSelectedCategory(),
                        homeViewModel.getSelectedFund()
                    )
                )
            }
        }


    }

    private fun observeSelectedFilterFromDialogFragmentFilter() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<ParcelableSelectedFilterData>(
            SELECTED_FILTER_KEY
        )
            ?.observe(
                viewLifecycleOwner
            ) { result ->
                // Do something with the result.
                homeViewModel.saveSelectedCategoryId(result.selectedCategoryId)
                homeViewModel.saveSelectedCategory(result.selectedCategory)
                homeViewModel.saveSelectedFunds(result.selectedFunds)

                homeViewModel.saveSelectedCategoryIdToDataStore(result.selectedCategoryId)
                homeViewModel.saveSelectedCategoryToDataStore(result.selectedCategory)
                homeViewModel.saveSelectedFundsToDataStore(result.selectedFunds)

                homeViewModel.getProductCatalog(
                    page = "1",
                    limit = "20",
                    productName = "",
                    categoryId = result.selectedCategoryId,
                    productType = result.selectedFunds
                )
            }
    }

    private fun observeEpoxyBannerData() {
        consumeSuspend {
            homeViewModel.listBannerData.collect { data ->
                epoxyMultiProductBannerController.setData(data)
            }
        }
    }

    private fun observeEpoxyProductData() {
        consumeSuspend {
            homeViewModel.listProductData.collect { data ->
                if (data.first() != MultiAdapterProductHomeData.Error) {
                    binding.ivHomeSelectList.setOnClickListener {
                        binding.rvHomeProduct.layoutManager = LinearLayoutManager(requireContext())
                    }
                    binding.ivHomeSelectGrid.setOnClickListener {
                        binding.rvHomeProduct.layoutManager = GridLayoutManager(requireContext(), 2)
                    }
                }
                epoxyMultiProductHomeController.setData(data)
            }
        }
    }

    private fun getCategoryProduct() {
        consumeSuspend {
            homeViewModel.categoryProductUiState.collect {
                when {
                    it.errorMessage.isNotEmpty() -> {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle(resources.getString(R.string.error_token_dialog_title))
                            .setMessage(it.errorMessage)
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                                dialog.dismiss()
                            }.show()
                    }

                    it.data != null -> {
                        if (it.data.listOfCategoryProduct.isNotEmpty()) {
                            listCategoryProduct = it.data.listOfCategoryProduct
                        }
                        if (it.data.listOfCategoryProduct.map { category -> category.categoryName }
                                .isNotEmpty()) {
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                it.data.listOfCategoryProduct.map { category -> category.categoryName }
                            ).also { adapter ->
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                // Apply the adapter to the spinner
                                binding.spHomeCategory.adapter = adapter
                            }
                        }

                        parcelableFilterData = ParcelableFilterData(
                            listOfFundType = it.data.listOfFundType,
                            listOfCategoryProduct = it.data.listOfCategoryProduct
                        )

                    }
                }
                binding.tvHomeFilterProduct.isVisible = !it.isLoading
            }
        }
    }

    private fun getProductCatalog() {
        consumeSuspend {
            homeViewModel.productCatalogUiState.collect {
                when (it.successState) {
                    ProductCatalogUiState.SuccessState.Initialize -> {}
                    ProductCatalogUiState.SuccessState.Success -> {
                        if (it.data.isNotEmpty()) {
                            listProductCatalog = it.data
                            binding.rvHomeProduct.layoutManager =
                                GridLayoutManager(this@FragmentHome.requireContext(), 2)
                            homeViewModel.setEpoxyProduct(it.data)
                        } else {
                            binding.rvHomeProduct.layoutManager = LinearLayoutManager(requireContext())
                            homeViewModel.setErrorEpoxyProduct()
                        }
                    }

                    ProductCatalogUiState.SuccessState.Failed -> {
                        binding.rvHomeProduct.layoutManager = LinearLayoutManager(requireContext())
                        if (it.errorMessage.isNotEmpty()) {
                            homeViewModel.setErrorEpoxyProduct()
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
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        val selectedPosition = listCategoryProduct.indexOfFirst {
            it.categoryName == parent.getItemAtPosition(pos).toString()
        }
        homeViewModel.saveSelectedCategoryId(listCategoryProduct[selectedPosition].categoryId)
        homeViewModel.saveSelectedCategory(parent.getItemAtPosition(pos).toString())
        homeViewModel.saveSelectedCategoryIdToDataStore(listCategoryProduct[selectedPosition].categoryId)
        homeViewModel.saveSelectedCategoryToDataStore(parent.getItemAtPosition(pos).toString())

    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        // do something on text submit
        consumeSuspend {
            homeViewModel.getProductCatalog(
                page = "1",
                limit = "20",
                productName = "",
                categoryId = homeViewModel.getSelectedCategoryId(),
                productType = homeViewModel.getSelectedFund()
            )
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        // do something when text changes
        return false
    }

    override fun onProductClick(selectedProductId: String) {
        val selectedPosition = listProductCatalog.indexOfFirst {
            it.productId == selectedProductId
        }
        val data = listProductCatalog[selectedPosition]
        findNavController().navigate(
            FragmentHomeDirections.actionFragmentHomeToFragmentDetailProduct(
                data
            )
        )
    }

}
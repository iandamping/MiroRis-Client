package com.spe.miroris.feature.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.spe.miroris.R
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.databinding.FragmentHomeBinding
import com.spe.miroris.feature.home.banner.DummyBanner
import com.spe.miroris.feature.home.banner.EpoxyStateProductBannerController
import com.spe.miroris.feature.home.banner.multiData.EpoxyMultiProductBannerController
import com.spe.miroris.feature.home.banner.nullAble.EpoxyProductBannerController
import kotlinx.coroutines.delay

class FragmentHome : BaseFragmentViewBinding<FragmentHomeBinding>(),
    AdapterView.OnItemSelectedListener, SearchView.OnQueryTextListener,
    EpoxyProductBannerController.EpoxyProductBannerClickListener {

    private val viewModel: HomeViewModel by viewModels()

    private val epoxyMultiProductBannerController: EpoxyMultiProductBannerController by lazy {
        EpoxyMultiProductBannerController()
    }

    private val epoxyStateProductBannerController: EpoxyStateProductBannerController by lazy {
        EpoxyStateProductBannerController()
    }

    private val epoxyProductBannerController: EpoxyProductBannerController by lazy {
        EpoxyProductBannerController(this)
    }

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

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
    }

    override fun viewCreated() {

        with(binding.rvHomeBanner) {
            //state approach
            setController(epoxyStateProductBannerController)
            //multi-data approach
//            setController(epoxyMultiProductBannerController)
            //nullable approach
//            setController(epoxyProductBannerController)
            LinearSnapHelper().attachToRecyclerView(this)
        }
        binding.tvHomeFilterProduct.setOnClickListener {
            findNavController().navigate(FragmentHomeDirections.actionFragmentHomeToFragmentProductManagement())
        }
        //multi-data approach
        consumeSuspend {
            delay(5000)
            viewModel.setDummyBanner2(
                listOf(
                    DummyBanner(0, R.drawable.ic_movies_2),
                    DummyBanner(1, R.drawable.ic_movies_3),
                    DummyBanner(2, R.drawable.ic_movies_4)
                )
            )
        }
        //multi-data approach
        consumeSuspend {
            viewModel.listBannerData2.collect { data ->
                epoxyMultiProductBannerController.setData(data)
            }
        }

        //nullable approach
        consumeSuspend {
            delay(5000)
            viewModel.setDummyBanner(
                listOf(
                    DummyBanner(0, R.drawable.ic_movies_2),
                    DummyBanner(1, R.drawable.ic_movies_3),
                    DummyBanner(2, R.drawable.ic_movies_4)
                )
            )
        }
        //nullable approach
        consumeSuspend {
            viewModel.listBannerData.collect { data ->
                epoxyProductBannerController.setData(data)
            }
        }

        //state approach
        consumeSuspend {
            viewModel.bannerUiState.collect { data ->
                epoxyStateProductBannerController.setData(data)
            }
        }

        binding.ivHomeSelectGrid.setOnClickListener {
            viewModel.setBannerLoading()
        }
        binding.ivHomeSelectList.setOnClickListener {
            viewModel.setBannerData(
                listOf(
                    R.drawable.ic_movies_2,
                    R.drawable.ic_movies_3,
                    R.drawable.ic_movies_4
                )
            )
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

    override fun onBannerClick() {

    }

}
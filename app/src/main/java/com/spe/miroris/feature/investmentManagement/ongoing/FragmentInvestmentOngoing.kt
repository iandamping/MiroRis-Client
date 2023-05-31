package com.spe.miroris.feature.investmentManagement.ongoing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearSnapHelper
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.databinding.FragmentInvestmentDoneBinding
import com.spe.miroris.feature.investmentManagement.SharedInvestmentManagementViewModel
import timber.log.Timber

class FragmentInvestmentOngoing : BaseFragmentViewBinding<FragmentInvestmentDoneBinding>(),
    EpoxyMultiInvestmentOnGoingController.EpoxyMultiInvestmentOnGoingControllerClickListener {

    private val sharedInvestmentManagementViewModel: SharedInvestmentManagementViewModel by activityViewModels()

    private val epoxyMultiProductHomeController: EpoxyMultiInvestmentOnGoingController by lazy {
        EpoxyMultiInvestmentOnGoingController(this)
    }
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentInvestmentDoneBinding
        get() = FragmentInvestmentDoneBinding::inflate

    override fun initView() {
        with(binding.rvInvestmentDone) {
            setController(epoxyMultiProductHomeController)
            LinearSnapHelper().attachToRecyclerView(this)
        }

    }

    override fun viewCreated() {
        observeEpoxyInvestmentData()
    }

    private fun observeEpoxyInvestmentData() {
        consumeSuspend {
            sharedInvestmentManagementViewModel.listInvestmentData.collect { data ->
                epoxyMultiProductHomeController.setData(data)
            }
        }
    }

    override fun onInvestmentClick(selectedInvestmentId: String) {
        Timber.e(selectedInvestmentId)
    }

}
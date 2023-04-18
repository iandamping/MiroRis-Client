package com.spe.miroris.feature.investmentManagement

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.spe.miroris.feature.investmentManagement.done.FragmentInvestmentDone
import com.spe.miroris.feature.investmentManagement.ongoing.FragmentInvestmentOngoing

class InvestmentManagementAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return InvestmentManagementEnum.values().size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentInvestmentOngoing()
            else -> FragmentInvestmentDone()
        }
    }
}
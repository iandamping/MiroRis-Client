package com.spe.miroris.feature.fundManagement

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.spe.miroris.feature.fundManagement.done.FragmentFundDone
import com.spe.miroris.feature.fundManagement.ongoing.FragmentFundOngoing

class FundManagementAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return FundManagementEnum.values().size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentFundOngoing()
            else -> FragmentFundDone()
        }
    }
}
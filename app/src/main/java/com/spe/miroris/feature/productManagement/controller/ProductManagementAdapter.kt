package com.spe.miroris.feature.productManagement.controller

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.spe.miroris.feature.productManagement.active.FragmentProductActive
import com.spe.miroris.feature.productManagement.done.FragmentProductDone
import com.spe.miroris.feature.productManagement.ongoing.FragmentProductOngoing

class ProductManagementAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return ProductManagementEnum.values().size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentProductActive()
            1 -> FragmentProductOngoing()
            else -> FragmentProductDone()
        }
    }
}
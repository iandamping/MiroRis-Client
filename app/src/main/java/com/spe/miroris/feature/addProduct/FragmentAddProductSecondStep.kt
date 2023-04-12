package com.spe.miroris.feature.addProduct

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.spe.miroris.base.BaseFragmentViewBinding
import com.spe.miroris.databinding.FragmentAddProductSecondStepBinding

class FragmentAddProductSecondStep :
    BaseFragmentViewBinding<FragmentAddProductSecondStepBinding>() {

    private val viewModel: AddProductFirstStepViewModel by activityViewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAddProductSecondStepBinding
        get() = FragmentAddProductSecondStepBinding::inflate

    override fun initView() {
    }

    override fun viewCreated() {
    }

}
package com.spe.miroris.feature.home.product

import androidx.appcompat.content.res.AppCompatResources
import coil.load
import com.spe.miroris.R
import com.spe.miroris.databinding.ItemProductBinding
import com.spe.miroris.util.ViewBindingEpoxyModelWithHolder

class EpoxyViewHolderProductHome(
    private val data: MultiAdapterProductHomeData.ProductHome,
    private val clickListener: (String) -> Unit
) : ViewBindingEpoxyModelWithHolder<ItemProductBinding>() {
    override fun ItemProductBinding.bind() {
        tvItemProductHomeTitle.text = data.productName
        tvItemProductHomeDesc.text = data.productDetail
        tvItemProductRate.text = data.randomRating.toString()
        if (data.productImage != EpoxyMultiProductHomeController.EMPTY_IMAGE) {
            ivItemProductHome.load(data.productImage)
        } else {
            ivItemProductHome.load(
                AppCompatResources.getDrawable(
                    root.context,
                    R.drawable.img_dummy_product
                )
            )
        }
        when (data.randomRating) {
            0 -> {
                ivStar1.setImageDrawable(
                    AppCompatResources.getDrawable(
                        root.context,
                        R.drawable.baseline_star_24_gray
                    )
                )
                ivStar2.setImageDrawable(
                    AppCompatResources.getDrawable(
                        root.context,
                        R.drawable.baseline_star_24_gray
                    )
                )
                ivStar3.setImageDrawable(
                    AppCompatResources.getDrawable(
                        root.context,
                        R.drawable.baseline_star_24_gray
                    )
                )
                ivStar4.setImageDrawable(
                    AppCompatResources.getDrawable(
                        root.context,
                        R.drawable.baseline_star_24_gray
                    )
                )
                ivStar5.setImageDrawable(
                    AppCompatResources.getDrawable(
                        root.context,
                        R.drawable.baseline_star_24_gray
                    )
                )
            }

            in 1..100 -> {
                ivStar1.setImageDrawable(
                    AppCompatResources.getDrawable(
                        root.context,
                        R.drawable.baseline_star_24_gold
                    )
                )
                ivStar2.setImageDrawable(
                    AppCompatResources.getDrawable(
                        root.context,
                        R.drawable.baseline_star_24_gray
                    )
                )
                ivStar3.setImageDrawable(
                    AppCompatResources.getDrawable(
                        root.context,
                        R.drawable.baseline_star_24_gray
                    )
                )
                ivStar4.setImageDrawable(
                    AppCompatResources.getDrawable(
                        root.context,
                        R.drawable.baseline_star_24_gray
                    )
                )
                ivStar5.setImageDrawable(
                    AppCompatResources.getDrawable(
                        root.context,
                        R.drawable.baseline_star_24_gray
                    )
                )
            }

            in 101..200 -> {
                ivStar1.setImageDrawable(
                    AppCompatResources.getDrawable(
                        root.context,
                        R.drawable.baseline_star_24_gold
                    )
                )
                ivStar2.setImageDrawable(
                    AppCompatResources.getDrawable(
                        root.context,
                        R.drawable.baseline_star_24_gold
                    )
                )
                ivStar3.setImageDrawable(
                    AppCompatResources.getDrawable(
                        root.context,
                        R.drawable.baseline_star_24_gray
                    )
                )
                ivStar4.setImageDrawable(
                    AppCompatResources.getDrawable(
                        root.context,
                        R.drawable.baseline_star_24_gray
                    )
                )
                ivStar5.setImageDrawable(AppCompatResources.getDrawable(root.context,R.drawable.baseline_star_24_gray))
            }
            in 201 .. 300 -> {
                ivStar1.setImageDrawable(AppCompatResources.getDrawable(root.context,R.drawable.baseline_star_24_gold))
                ivStar2.setImageDrawable(AppCompatResources.getDrawable(root.context,R.drawable.baseline_star_24_gold))
                ivStar3.setImageDrawable(AppCompatResources.getDrawable(root.context,R.drawable.baseline_star_24_gold))
                ivStar4.setImageDrawable(AppCompatResources.getDrawable(root.context,R.drawable.baseline_star_24_gray))
                ivStar5.setImageDrawable(AppCompatResources.getDrawable(root.context,R.drawable.baseline_star_24_gray))
            }
            in 301 ..400 -> {
                ivStar1.setImageDrawable(AppCompatResources.getDrawable(root.context,R.drawable.baseline_star_24_gold))
                ivStar2.setImageDrawable(AppCompatResources.getDrawable(root.context,R.drawable.baseline_star_24_gold))
                ivStar3.setImageDrawable(AppCompatResources.getDrawable(root.context,R.drawable.baseline_star_24_gold))
                ivStar4.setImageDrawable(AppCompatResources.getDrawable(root.context,R.drawable.baseline_star_24_gold))
                ivStar5.setImageDrawable(AppCompatResources.getDrawable(root.context,R.drawable.baseline_star_24_gray))
            }
            in 401..500 -> {
                ivStar1.setImageDrawable(AppCompatResources.getDrawable(root.context,R.drawable.baseline_star_24_gold))
                ivStar2.setImageDrawable(AppCompatResources.getDrawable(root.context,R.drawable.baseline_star_24_gold))
                ivStar3.setImageDrawable(AppCompatResources.getDrawable(root.context,R.drawable.baseline_star_24_gold))
                ivStar4.setImageDrawable(AppCompatResources.getDrawable(root.context,R.drawable.baseline_star_24_gold))
                ivStar5.setImageDrawable(AppCompatResources.getDrawable(root.context,R.drawable.baseline_star_24_gold))
            }
        }

        root.setOnClickListener {
            clickListener.invoke(data.productId)
        }
    }

    override fun getDefaultLayout(): Int {
        return R.layout.item_product
    }
}
package com.tech.sonet.ui.chart

import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.tech.sonet.R
import com.tech.sonet.databinding.CardlistImageBinding
import com.tech.sonet.ui.base.BaseActivity
import com.tech.sonet.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CardImageActivity:BaseActivity<CardlistImageBinding>() {
    private val viewModel: CardImageViewModel by viewModels()

    companion object{
        var imageLoad:String?=null
    }
    override fun getLayoutResource(): Int {
        return R.layout.cardlist_image
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }
    override fun onCreateView() {
        initOnClick()
        Glide.with(Objects.requireNonNull(this))
            .load(imageLoad) // image url
            .into(binding.listImage);


    }
    private fun initOnClick() {
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.back_list->{
                    onBackPressedDispatcher.onBackPressed()
                }
            }
            }
    }

}
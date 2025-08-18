package com.tech.sonet.ui.home_fragment

import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.tech.sonet.R
import com.tech.sonet.databinding.MypcardImageBinding
import com.tech.sonet.ui.base.BaseActivity
import com.tech.sonet.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MypCardImageActivity: BaseActivity<MypcardImageBinding>() {
    private val viewModel: MypCardImageVM by viewModels()

    companion object{
        var imageLoad:String?=null
    }
    override fun getLayoutResource(): Int {
        return R.layout.mypcard_image
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        initOnClick()
        Glide.with(Objects.requireNonNull(this))
            .load(imageLoad) // image url
            .into(binding.Ivpcard);
    }

    private fun initOnClick() {
        viewModel.onClick.observe(this, androidx.lifecycle.Observer {
            when (it?.id) {
                R.id.Ivback->{
                    onBackPressed()
                }
            }
        })
    }

}
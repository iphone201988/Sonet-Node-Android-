package com.tech.sonet.ui.chart

import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
        Log.i("dsfdsfdsfds", "onCreateView: $imageLoad")
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
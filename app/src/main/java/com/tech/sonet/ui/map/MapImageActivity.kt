package com.tech.sonet.ui.map

import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.tech.sonet.R
import com.tech.sonet.databinding.MapImageActivityBinding
import com.tech.sonet.ui.base.BaseActivity
import com.tech.sonet.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapImageActivity: BaseActivity<MapImageActivityBinding>() {
    private val viewModel: MapViewModel by viewModels()
    override fun getLayoutResource(): Int {
        return R.layout.map_image_activity
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        initOnClick()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(this, Observer {
            when (it?.id) {
                R.id.back_map->{
                    onBackPressed()
                }
            }
        })
    }

}
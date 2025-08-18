package com.tech.sonet.ui.like_sent

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.tech.sonet.R
import com.tech.sonet.data.model.LikeSentApiResponse
import com.tech.sonet.databinding.ActivityLikeSentDetailBinding
import com.tech.sonet.ui.base.BaseActivity
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.ui.chart.CardImageActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LikeSentDetailActivity : BaseActivity<ActivityLikeSentDetailBinding>() {

    var str:String=""
    private val viewModel : LikeSentDetailActivityVm by viewModels()

    override fun getLayoutResource(): Int {
        return R.layout.activity_like_sent_detail
     }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        val bundle = intent.extras
        if (bundle != null) {
            val data = bundle.getParcelable<LikeSentApiResponse.LikeSentApiResponseData>("data")
            data as LikeSentApiResponse.LikeSentApiResponseData
            binding.bean = data
            str= data.post_image.toString()
        }
        initOnClick()
    }

    private fun initOnClick() {
       viewModel.onClick.observe(this , Observer {
           when(it?.id){
               R.id.Ivback ->{
                   onBackPressedDispatcher.onBackPressed()
               }
               R.id.request_image ->{
                   CardImageActivity.imageLoad = str
                   val intent = Intent(this, CardImageActivity::class.java)
                   startActivity(intent)
               }
           }
       })
    }
}
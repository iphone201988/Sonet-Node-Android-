package com.tech.sonet.ui.subscription_module

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.tech.sonet.R
import com.tech.sonet.databinding.ActivitySubscriptionBinding
import com.tech.sonet.ui.base.BaseActivity
import com.tech.sonet.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubscriptionActivity : BaseActivity<ActivitySubscriptionBinding>() {

    private  val viewModel : SubscriptionActivityVm  by viewModels()

    override fun getLayoutResource(): Int {
        return  R.layout.activity_subscription
    }

    override fun getViewModel(): BaseViewModel {
       return viewModel
    }

    override fun onCreateView() {
       initOnClick()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(this , Observer {
            when(it?.id){

                R.id.ivBack -> {
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }
}
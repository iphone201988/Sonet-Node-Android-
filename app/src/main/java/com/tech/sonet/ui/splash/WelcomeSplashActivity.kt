package com.tech.sonet.ui.splash

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.tech.sonet.R
import com.tech.sonet.data.local.SharedPrefManager
import com.tech.sonet.databinding.WelcomeScreenBinding
import com.tech.sonet.ui.HomeActivity
import com.tech.sonet.ui.base.BaseActivity
import com.tech.sonet.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeSplashActivity @Inject constructor() : BaseActivity<WelcomeScreenBinding>() {


    private val viewModel: WelcomeSplashVM by viewModels()



    override fun getLayoutResource(): Int {
        return R.layout.welcome_screen
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }


    override fun onCreateView() {
      //  openActivity2()
        initOnClick()
//        val totelUser = sharedPrefManager.getCurrentUser()?.data.toString()
//        binding.TVnumber.text = totelUser
    }

    private fun initOnClick() {
        viewModel.onClick.observe(this , Observer {
            when(it?.id){
                R.id.back_home ->{
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        })
    }
    // var count = 0
    /* private fun openActivity() {
         for(i in 1..100){
             if(userId.equals(sharedPrefManager.getCurrentUser()?.id.toString())){
                 if(sharedPrefManager.getCountNumber() == 0){
                     count = 0
                 }else{
                     count += 1
                     sharedPrefManager.saveCountNumber(count)
                     if(count % 2 == 0){
                         openActivity2()
                     }else{
                         val intent = Intent(this, HomeActivity::class.java)
                         startActivity(intent)
                         finish()
                     }
                 }
             }else{
                 val intent = Intent(this, HomeActivity::class.java)
                 startActivity(intent)
                 finish()
             }
         }

         }*/

    fun openActivity2() {
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }, 7000)
        }
    }



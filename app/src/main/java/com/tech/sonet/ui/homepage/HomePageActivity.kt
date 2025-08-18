package com.tech.sonet.ui.homepage

import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.tech.sonet.R
import com.tech.sonet.databinding.HomePageBinding
import com.tech.sonet.ui.base.BaseActivity
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.ui.login.LoginActivity
import com.tech.sonet.ui.signup.SignUpActivity
import com.tech.sonet.ui.terms_conditions.TermsConditionsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomePageActivity : BaseActivity<HomePageBinding>() {
    private val viewModel: HomePageVM by viewModels()
    override fun getLayoutResource(): Int {
        return R.layout.home_page
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        initOnClick()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.logintext -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }

                R.id.registertext -> {
                    val intent = Intent(this, TermsConditionsActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

}
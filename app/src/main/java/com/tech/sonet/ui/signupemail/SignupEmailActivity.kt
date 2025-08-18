package com.tech.sonet.ui.signupemail

import android.content.Intent
import androidx.activity.viewModels
import com.tech.sonet.R
import com.tech.sonet.databinding.SignupEmailBinding
import com.tech.sonet.ui.base.BaseActivity
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.ui.homepage.HomePageActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupEmailActivity : BaseActivity<SignupEmailBinding>() {
    private val viewModel: SignupEmailVM by viewModels()

    override fun getLayoutResource(): Int {
        return R.layout.signup_email
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
                R.id.back_home -> {
                   val intent = Intent(this,HomePageActivity::class.java)
                    startActivity(intent)
                }

            }
        }
    }
}
package com.tech.sonet.ui.forgotemail

import android.content.Intent
import androidx.activity.viewModels
import com.tech.sonet.R
import com.tech.sonet.databinding.ForgotPasswordEmailBinding
import com.tech.sonet.ui.addcard.MypCardActivity
import com.tech.sonet.ui.base.BaseActivity
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotEmailActivity : BaseActivity<ForgotPasswordEmailBinding>() {
    private val viewModel: ForgotEmailVM by viewModels()
    override fun getLayoutResource(): Int {
        return R.layout.forgot_password_email
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
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }

            }
        }

    }

}
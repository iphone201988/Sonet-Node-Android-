package com.tech.sonet.ui.changepassword

import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.tech.sonet.R
import com.tech.sonet.databinding.ActivityChangePasswordBinding
import com.tech.sonet.ui.base.BaseActivity
import com.tech.sonet.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordActivity : BaseActivity<ActivityChangePasswordBinding>() {
    private val viewModel: ChangePasswordVM by viewModels()


    override fun getLayoutResource(): Int {
        return R.layout.activity_change_password
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {

        initOnClick()
        initView()
    }


    private fun initView() {

    }

    private fun initOnClick() {
        viewModel.onClick.observe(this, Observer {
            when (it?.id) {
                R.id.btnContinue -> {

                }
//                R.id.backimage -> {
//                    val intent= Intent(this, ForgotPasswordActivity::class.java)
//                    startActivity(intent)
//                }
            }
        })
    }

}
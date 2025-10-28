package com.tech.sonet.ui.forgotpassword

import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.tech.sonet.R
import com.tech.sonet.data.model.ForgotPasswordApiResponse
import com.tech.sonet.databinding.ActivityForgotpasswordBinding
import com.tech.sonet.ui.base.BaseActivity
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.ui.forgotemail.ForgotEmailActivity
import com.tech.sonet.ui.forgotemail.emial_verify.EmailVerifyActivity
import com.tech.sonet.utils.Constant
import com.tech.sonet.utils.ImageBindingUtil
import com.tech.sonet.utils.Status
import com.tech.sonet.utils.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordActivity : BaseActivity<ActivityForgotpasswordBinding>() {
    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun getLayoutResource(): Int {
        return R.layout.activity_forgotpassword
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initOnClick()
        initView()
        initObserver()
    }


    private fun initView() {

    }

    private fun initOnClick() {
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.reset_password -> {
                    if (isEmptyField()){
                        if (Patterns.EMAIL_ADDRESS.matcher(binding.etemail.text.toString().trim())
                                .matches())
                        {
                            forgotPassword()
                        }else{
                            showToast("Invalid Email")
                        }
                    }



                }
                R.id.Imageback -> {
                    onBackPressedDispatcher.onBackPressed()
                }

            }
        }
    }

    private fun initObserver() {
        viewModel.obsForgotPassword.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showLoading("Loading")
                }
                Status.SUCCESS -> {
                    hideLoading()
                    val myDataModel : ForgotPasswordApiResponse ? = ImageBindingUtil.parseJson(it.data.toString())
                    if (myDataModel != null){
                        if (myDataModel.data != null){
                            val intent = Intent(this, EmailVerifyActivity::class.java)
                            intent.putExtra("sessionId", myDataModel.data!!.session)
                            startActivity(intent)

                            showSuccessToast(it.data.toString())
                        }
                    }

                }
                Status.ERROR -> {
                    hideLoading()
                    showToast(it.message.toString())
                }
                else -> {}

                }
            }
        }

    private fun forgotPassword() {

            val map = HashMap<String, Any>()
            map["email"] = binding.etemail.text.toString().trim()
            viewModel.forgotPassword(map, Constant.FORGOT_PASSWORD)


        }
    private fun isEmptyField(): Boolean {
    if (TextUtils.isEmpty(binding.etemail.text.toString().trim())) {
        showToast("Please enter Email")
        return false
    }
        return true

}
    }



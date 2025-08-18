package com.tech.sonet.ui.forgotemail.reset_password

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.tech.sonet.R
import com.tech.sonet.data.api.SimpleApiResponse
import com.tech.sonet.data.model.OtpVerifyResponse
import com.tech.sonet.databinding.ActivityResetPasswordBinding
import com.tech.sonet.ui.base.BaseActivity
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.ui.login.LoginActivity
import com.tech.sonet.utils.Constant
import com.tech.sonet.utils.ImageBindingUtil
import com.tech.sonet.utils.Status
import com.tech.sonet.utils.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordActivity : BaseActivity<ActivityResetPasswordBinding>() {

    private val viewModel : ResetPasswordActivityVm by viewModels()

    private var token : String ? = null
    override fun getLayoutResource(): Int {
       return R.layout.activity_reset_password
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        getToken()
        initOnClick()
        setObserver()
      }

    private fun setObserver() {
        viewModel.obrCommon.observe(this , Observer {
            when(it?.status){
                Status.LOADING ->{
                    showLoading("Loading")
                }
                Status.SUCCESS -> {
                    hideLoading()
                    val myDataModel : SimpleApiResponse? = ImageBindingUtil.parseJson(it.data.toString())
                    if (myDataModel != null){
                           val intent = Intent(this , LoginActivity:: class.java)
                            startActivity(intent)
                            showSuccessToast(it.data.toString())
                        }
                    }


                Status.ERROR -> {
                    hideLoading()
                    showToast(it.message.toString())
                }
                else -> {

                }

            }


        })
    }

    private fun getToken() {
        token = intent.getStringExtra("token")
    }

    private fun initOnClick() {
        viewModel.onClick.observe(this , Observer {
            when(it?.id){
                R.id.restPassword ->{
                    resetPassword()
                }
                R.id.chevron_left ->{
                    val intent  = Intent(this , LoginActivity:: class.java)
                    startActivity(intent)
                }
            }
        })
    }

    private fun resetPassword() {
        val password = binding.newPassword.text.toString().trim()

        if (binding.newPassword.text.isEmpty()) {
            showToast("Please enter new password")
            return
        }
        if (binding.confirmpassword.text.isEmpty()) {
            showToast("Please enter confirm password")
            return
        }
        if (password.length < 8) {
            showToast("Password must be at least 8 characters long")
            return
        }
        if (password != binding.confirmpassword.text.toString().trim())
        {
            showToast("Password and confirm password not matched")
            return
        }

        val data = HashMap<String, Any>()
        data["password"] = binding.newPassword.text.toString().trim()
        viewModel.resetPassword(data,Constant.CHANGE_PASSWORD)



    }
}
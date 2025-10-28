package com.tech.sonet.ui.forgotemail.emial_verify

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.tech.sonet.R
import com.tech.sonet.data.model.ForgotPasswordApiResponse
import com.tech.sonet.data.model.OtpVerifyResponse
import com.tech.sonet.databinding.ActivityEmailVerifyBinding
import com.tech.sonet.ui.base.BaseActivity
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.ui.changepassword.ChangePasswordActivity
import com.tech.sonet.ui.forgotemail.reset_password.ResetPasswordActivity
import com.tech.sonet.utils.Constant
import com.tech.sonet.utils.ImageBindingUtil
import com.tech.sonet.utils.Status
import com.tech.sonet.utils.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.processor.internal.definecomponent.codegen._dagger_hilt_android_components_ActivityComponent

@AndroidEntryPoint
class EmailVerifyActivity : BaseActivity<ActivityEmailVerifyBinding>() {

    private val viewModel : EmailVerifyActivityVm by viewModels()

    var sessionId : String ? = null


    override fun getLayoutResource(): Int {
        return R.layout.activity_email_verify
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
        getData()
        initOnClick()
        setOberver()

     }

    private fun setOberver() {
        viewModel.obrCommon.observe(this , Observer {
            when(it?.status){
                Status.LOADING ->{
                    showLoading("Loading")
                }
                Status.SUCCESS -> {
                    hideLoading()
                    val myDataModel : OtpVerifyResponse? = ImageBindingUtil.parseJson(it.data.toString())
                    if (myDataModel != null){
                        if (myDataModel.data != null){
                            Constant.authToken = myDataModel.data!!
                            val intent = Intent(this, ResetPasswordActivity::class.java)


                            startActivity(intent)

                            showSuccessToast(it.data.toString())
                        }
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

    private fun getData() {
        sessionId = intent.getStringExtra("sessionId")

    }

    private fun initOnClick() {
     viewModel.onClick.observe(this, Observer {
         when(it?.id){
             R.id.verifyAccount ->{
                  verifyOtp()
             }
             R.id.Imageback->{
                 onBackPressedDispatcher.onBackPressed()
             }
         }
     })
    }

    private fun verifyOtp() {
        if (binding.etVerifyCode.text.isEmpty()) {
            showToast("Please enter verification code")
            return
        }

        val data = HashMap<String, Any>()
        data["session"] = sessionId.toString()
        data["otp"] = binding.etVerifyCode.text.toString().trim()

        viewModel.otpVerify(data, Constant.OTP_VERIFY)

    }
}
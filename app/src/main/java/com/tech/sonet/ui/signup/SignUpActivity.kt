package com.tech.sonet.ui.signup

import android.content.Intent
import android.graphics.Rect
import android.util.Log
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.tech.sonet.R
import com.tech.sonet.data.model.response.LoginResponse
import com.tech.sonet.databinding.ActivitySignupBinding
import com.tech.sonet.ui.base.BaseActivity
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.ui.login.LoginActivity
import com.tech.sonet.ui.signupemail.SignupEmailActivity
import com.tech.sonet.ui.welcome.WelcomeActivity
import com.tech.sonet.utils.Constant
import com.tech.sonet.utils.ImageBindingUtil
import com.tech.sonet.utils.Status
import com.tech.sonet.utils.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : BaseActivity<ActivitySignupBinding>() {
    private val viewModel: SignUpViewModel by viewModels()
    override fun getLayoutResource(): Int {
        return R.layout.activity_signup
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.innerCl)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }





        initOnClick()
        initView()
        initObserver()
        getDeviceToken()
    }
    private var token = ""
    private fun getDeviceToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("SignUpActivity", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            token = task.result
        })
    }


    private fun initView() {

    }

    private fun initOnClick() {
        viewModel.onClick.observe(this, Observer {
            when (it?.id) {

                R.id.tvlogin -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finishAffinity()

                }
                R.id.back_arrow1 -> {
                    onBackPressedDispatcher.onBackPressed()

                }
                R.id.btnsignUp -> {
                    signUp()
                }
            }
        })

    }

    private fun initObserver() {
        viewModel.observerSignup.observe(this, Observer {
            when (it?.status) {
                Status.LOADING -> {
                    showLoading("Loading")
                }
                Status.SUCCESS -> {
                    hideLoading()
                    val myDataModel : LoginResponse? = ImageBindingUtil.parseJson(it.data.toString())
                    if (myDataModel != null){
                        if (myDataModel.data != null){
                            sharedPrefManager.saveUser(myDataModel)
                            myDataModel.data!!.token?.let { it1 -> sharedPrefManager.saveToken(it1) }
                            myDataModel.data!!._id?.let { it1 -> sharedPrefManager.saveUserId(it1) }
                            val intent = Intent(this, SignupEmailActivity::class.java)
                            startActivity(intent)
                            showSuccessToast(it.message.toString())
                        }
                    }

                }
                Status.ERROR -> {
                    hideLoading()
                    showSuccessToast(it.message.toString())
                }
                else -> {}
            }
        })
    }


    fun signUp() {
        val password = binding.etPassword.text.toString().trim()
        if (binding.etDisplayName.text.isEmpty()) {
            showToast("Please enter your name")
            return
        }
        if (binding.etEmail.text.isEmpty()) {
            showToast("Please enter your email")
            return
        }
        if (binding.etPassword.text.isEmpty()) {
            showToast("Please enter your password")
            return
        }
        if (binding.etCfPassword.text.isEmpty()) {
            showToast("Please enter your confirm password")
            return
        }
        if (password.length < 8) {
            showToast("Password must be at least 8 characters long")
            return
        }
        if (password != binding.etCfPassword.text.toString().trim())
        {
            showToast("Password and confirm password not matched")
            return
        }




        val map = HashMap<String, Any>()
        map["user_name"] = binding.etDisplayName.text.toString().trim()
        map["email"] = binding.etEmail.text.toString().trim()
        map["password"] = binding.etPassword.text.toString().trim()
        map["c_password"] = binding.etCfPassword.text.toString().trim()
//        map["latitude"] = WelcomeActivity.lat
//        map["longitude"] = WelcomeActivity.long
        map["device_type"] = "Android"
        map["device_token"] = token
        map["mobile"] = binding.etMessangerId.text.toString().trim()
        map["level"] = 1
        map["location"] = mapOf(
            "type" to "Point", "coordinates" to listOf(
                WelcomeActivity.long , WelcomeActivity.lat
            ))

        Log.i("Sfdfdsd", "signUp: $map")



        viewModel.signUp(map, Constant.SIGN_UP)




    }

}
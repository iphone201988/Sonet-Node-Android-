package com.tech.sonet.ui.login

import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.tech.sonet.R
import com.tech.sonet.data.local.CustomSharedPrefManager
import com.tech.sonet.data.model.GetMyCardApiResponse
import com.tech.sonet.data.model.response.LoginResponse
import com.tech.sonet.databinding.ActivityLoginBinding
import com.tech.sonet.ui.HomeActivity
import com.tech.sonet.ui.addcard.MypCardActivity
import com.tech.sonet.ui.base.BaseActivity
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.ui.forgotpassword.ForgotPasswordActivity
import com.tech.sonet.ui.signup.SignUpActivity
import com.tech.sonet.ui.signupemail.SignupEmailActivity
import com.tech.sonet.ui.splash.WelcomeSplashActivity
import com.tech.sonet.ui.terms_conditions.TermsConditionsActivity
import com.tech.sonet.ui.welcome.WelcomeActivity
import com.tech.sonet.utils.Constant
import com.tech.sonet.utils.ImageBindingUtil
import com.tech.sonet.utils.Status
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    private val viewModel: LoginVM by viewModels()
    private var count = 0
    private var userID : String ? = null

    private lateinit var customSharedPrefManager: CustomSharedPrefManager

    override fun getLayoutResource(): Int {
        return R.layout.activity_login
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    companion object {
        var isupdate = false

    }

    override fun onCreateView() {
        getDeviceToken()
        customSharedPrefManager = CustomSharedPrefManager(this)
        initOnClick()
        initObserver()

    }


    private var token = "tjmkrkmr,"
    private fun getDeviceToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {

                return@OnCompleteListener
            }
            token = task.result
            Log.w("rttrhkgbhgk",  token)

        })
    }


    private fun initOnClick() {
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.btnLoginIn -> {
                    login()
                }
                R.id.tvForgotPass -> {
                    val intent = Intent(this, ForgotPasswordActivity::class.java)
                    startActivity(intent)

                }
                R.id.tvRegister -> {
                    val intent = Intent(this, TermsConditionsActivity::class.java)
                    startActivity(intent)
                }
                R.id.back_arrow -> {
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        }
    }

    private fun initObserver() {
        viewModel.obsLogin.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showToast("LOADING")
                }
                Status.SUCCESS -> {
                    val gson = Gson()
                    val myDataModel: LoginResponse?

                    it.data?.let { responseBody ->
                        myDataModel =
                            gson.fromJson(responseBody.string(), LoginResponse::class.java)
                        myDataModel?.let { data ->
                            if(data.data?.isAccountVerified == true){
                                sharedPrefManager.saveUser(data)
                                customSharedPrefManager.saveUser(data)

                                data.data?._id?.let { it1 -> sharedPrefManager.saveUserId(it1) }
                                data.data!!.isLocation?.let { it1 ->
                                    sharedPrefManager.saveLocation(
                                        it1
                                    )
                                }
                                data.data!!.isNotification?.let { it1 ->
                                    sharedPrefManager.savePushNotification(
                                        it1
                                    )
                                }

                                val coordinates = data.data?.location?.coordinates
                                userID = data.data?._id


                                if (data.data?.card_uploaded == 0){
                                    isupdate = true
                                    val intent = Intent(this, MypCardActivity::class.java)
                                    startActivity(intent)
                                }else {
                                    if (count % 2 == 0) {
                                        val intent = Intent(this, WelcomeSplashActivity::class.java)
                                        startActivity(intent)
                                    } else {
                                        val intent = Intent(this, HomeActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                                    //    getMyCard()
//
//                            val intent = Intent(this , HomeActivity::class.java)
//                            startActivity(intent)
                            }
                            else{

                                val intent = Intent(this, SignupEmailActivity::class.java)
                                startActivity(intent)
                            }
                        }

                    }



//                    it.data?.let { it1 -> sharedPrefManager.saveUser(it1) }
//                    it.data?.let {
//                        sharedPrefManager.saveUserId(it.id.toString())
//                    }
//                    sharedPrefManager.saveLocation(it.data?.is_location_active.toString())
//                    sharedPrefManager.savePushNotification(it.data?.notification_is_active.toString())
//
//
//                    if (sharedPrefManager.getCurrentUser() != null) {
//                        count = sharedPrefManager.getCurrentUser()?.login_times!!
//                    }
//                    if (it.data?.latitude != null) {
//                        getMyCard(it.data.latitude, it.data.longitude!!)
//                    }
                }
                Status.ERROR -> {
                    showToast(it.message.toString())
                }
                else -> {}
            }
        }


//        viewModel.obrCard.observe(this , Observer{
//            when(it?.status){
//                Status.LOADING ->{
//                    showLoading("Loading")
//                }
//                Status.SUCCESS ->{
//                    hideLoading()
//
//                    val myDataModel : GetMyCardApiResponse? = ImageBindingUtil.parseJson(it.data.toString())
//                    if (myDataModel != null){
//                        if (myDataModel.data?.isEmpty() == true){
//                            val intent = Intent(this , MypCardActivity :: class.java)
//                            startActivity(intent)
//
//                        }else{
//                            val intent = Intent(this , HomeActivity :: class.java)
//                            startActivity(intent)
//                        }
//                    }
//                }
//                Status.ERROR ->{
//                    hideLoading()
//                    showToast(it.message.toString())
//                }
//                else ->{
//
//                }
//            }
//        })



//                    if (it.data?.data?.isEmpty() == true) {
//                        isupdate = true
//                        val intent = Intent(this, MypCardActivity::class.java)
//                        startActivity(intent)
//                    } else {
//                        if (count % 2 == 0) {
//                            val intent = Intent(this, WelcomeSplashActivity::class.java)
//                            startActivity(intent)
//                        } else {
//                            val intent = Intent(this, HomeActivity::class.java)
//                            startActivity(intent)
//                            finish()
//                        }
//                    }
                }





    fun login() {
        if (binding.email.text.isEmpty()) {
            showToast("Please enter your email")
            return
        }
        if (binding.password.text.isEmpty()) {
            showToast("Please enter your password")
            return
        }


        val  data = LoginRequest1(
            binding.email.text.toString().trim(),
            binding.password.text.toString().trim(),
            "Android",
            token,
             Location("Point",
                 listOf(WelcomeActivity.long.toDouble(),WelcomeActivity.lat.toDouble()))
        )

// Send the map in the request
        Log.i("Dfdsfsdfsdf", "login: $token")
       viewModel.loginUser(data)

    }


    data class LoginRequest1(
        val email: String,
        val password: String,
        val device_type: String,
        val device_token: String,
        val location: Location
    )

    data class Location(
        val type: String,
        val coordinates: List<Double>
    )

    private fun getMyCard() {
        if (sharedPrefManager.getCurrentUser()?.data?.token != null){
            viewModel.myCard(Constant.MY_CARDS)
        }
        else{
            showToast("token required")
        }
    }

}
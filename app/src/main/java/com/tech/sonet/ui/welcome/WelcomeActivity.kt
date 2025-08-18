package com.tech.sonet.ui.welcome

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.tech.sonet.R
import com.tech.sonet.data.model.GetMyCardApiResponse
import com.tech.sonet.databinding.ActivityWelcomeBinding
import com.tech.sonet.ui.HomeActivity
import com.tech.sonet.ui.addcard.MypCardActivity
import com.tech.sonet.ui.base.BaseActivity
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.ui.base.location.LocationHandler
import com.tech.sonet.ui.base.location.LocationResultListener
import com.tech.sonet.ui.base.permission.PermissionHandler
import com.tech.sonet.ui.base.permission.Permissions
import com.tech.sonet.ui.homepage.HomePageActivity
import com.tech.sonet.ui.login.LoginActivity
import com.tech.sonet.utils.Constant
import com.tech.sonet.utils.ImageBindingUtil
import com.tech.sonet.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeActivity : BaseActivity<ActivityWelcomeBinding>(), LocationResultListener {

    private val viewModel: WelcomeActivityVM by viewModels()
    private var locationHandler: LocationHandler? = null
    private var mCurrentLocation: Location? = null
    private val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
    companion object {
        var long = "0.00"
        var lat = "0.00"
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_welcome
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        checkLocation()
        checkNotificationPermission()
        setObserver()
    }

    private fun setObserver() {
        viewModel.obrCard.observe(this , Observer{
            when(it?.status){
                Status.LOADING ->{
                    showLoading("Loading")
                }
                Status.SUCCESS ->{
                    hideLoading()

                    val myDataModel : GetMyCardApiResponse ? = ImageBindingUtil.parseJson(it.data.toString())
                    if (myDataModel != null){
                        if (myDataModel.data?.isEmpty() == true){
                            val intent = Intent(this , MypCardActivity :: class.java)
                            startActivity(intent)

                        }else{
                            val intent = Intent(this , HomeActivity :: class.java)
                            startActivity(intent)
                        }
                    }
                }
                Status.ERROR ->{
                    hideLoading()
                    showToast(it.message.toString())
                }
                else ->{

                }
            }
        })
    }
    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request notification permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("WelcomeActivity", "Notification permission granted")
            } else {
                Log.d("WelcomeActivity", "Notification permission denied")
            }
        }
    }
    private fun checkLocation() {
        Permissions.check(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION,
            0,
            object : PermissionHandler() {
                override fun onGranted() {
                    createLocationHandler()
                }

                override fun onDenied(context: Context?, deniedPermissions: ArrayList<String>?) {
                    super.onDenied(context, deniedPermissions)
                    checkLocation()
                }
            })
    }

    private fun createLocationHandler() {
        locationHandler = LocationHandler(this, this)
        locationHandler?.getUserLocation()
        locationHandler?.removeLocationUpdates()
    }

    override fun getLocation(location: Location) {
        this.mCurrentLocation = location
        lat = location.latitude.toString()
        long = location.longitude.toString()
        openActivity()


//        viewModel.obsmyCards.observe(this) {
//            when (it?.status) {
//                Status.LOADING -> {
//                    showLoading("Loading")
//                }
//                Status.SUCCESS -> {
//                    hideLoading()
//                    if (it.data?.data?.isEmpty() == true) {
//                        Log.i("jnfjij", "getLocation: "+it.data.data.size)
//                  /*      val bundle = Bundle()
//                        bundle.putParcelable("mycard", it.data.data[0])
//                        val intent = Intent(this, MypCardActivity::class.java)
//                        intent.putExtras(bundle)
//                        startActivity(intent)*/
//                        val intent = Intent(this, MypCardActivity::class.java)
//                        startActivity(intent)
//                        finish()
//                    } else {
//                        val intent = Intent(this, HomeActivity::class.java)
//                        startActivity(intent)
//                        finish()
//                    }
//                }
//                Status.ERROR -> {
//                    hideLoading()
//                  //  sharedPrefManager.clear()
//                    val intent = Intent(this, LoginActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                    showToast(it.message.toString())
//                }
//                else -> {}
//            }
//        }
    }


    private fun openActivity() {
        if (sharedPrefManager.getCurrentUser() != null) {
//            val intent = Intent(this , HomeActivity::class.java)
//            startActivity(intent)
            getMyCard()
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, HomePageActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000)
        }
    }

    private fun getMyCard() {
            viewModel.myCard(Constant.MY_CARDS)
    }

}
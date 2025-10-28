package com.tech.sonet.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.tech.sonet.R
import com.tech.sonet.databinding.ActivityHomeBinding
import com.tech.sonet.ui.base.BaseActivity
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.ui.chart.ChartFragment
import com.tech.sonet.ui.home_fragment.HomeFragment
import com.tech.sonet.ui.map.MapFragment
import com.tech.sonet.ui.settings.SettingsFragment
import com.tech.sonet.utils.BackStackManager
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    private val viewModel: HomeActivityVM by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var backPressedTime: Long = 0
    var isHome= false

    override fun getLayoutResource(): Int {
        return R.layout.activity_home
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 14) {
            if (permissions.size == 1 && permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient = this.let {
                    LocationServices.getFusedLocationProviderClient(
                        it
                    )
                }
                // initCurrentLocation()
            }
        }
    }



    override fun onCreateView() {
//        enableEdgeToEdge()
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        bottomNav()
        initOnBackPressed()
        changeFragment(HomeFragment())
        isHome = true
        ActivityCompat.requestPermissions(
            (this@HomeActivity),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            14
        )

    }
    private fun initOnBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(isHome){
                    finishAffinity()
                }else{
                    changeFragment(HomeFragment())
                    isHome = true
                    binding.bottomNavigation.selectedItemId = R.id.homeFragment
                }

            }
        })
    }
    private fun bottomNav() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    changeFragment(HomeFragment())
                    isHome = true
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.mapFragment -> {
                    changeFragment(MapFragment())
                    isHome = false
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.chartFragment -> {
                    changeFragment(ChartFragment())
                    isHome = false
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.settingsFragment -> {
                    changeFragment(SettingsFragment())
                    isHome = false
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    isHome = false
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }
    }
    private fun changeFragment(fragment: Fragment) {
        val transaction = this.supportFragmentManager
            .beginTransaction()
        transaction.replace(R.id.frame, fragment)
        transaction.commit()
    }



    fun pushFragment(tag: String, fragment: Fragment) {
        BackStackManager.getInstance(this).pushSubFragments(R.id.frame, tag, fragment, true)
        binding.bottomNavigation.menu.findItem(R.id.mapFragment).isChecked = true
    }
}


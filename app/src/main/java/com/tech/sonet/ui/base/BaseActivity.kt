package com.tech.sonet.ui.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.tech.sonet.App
import com.tech.sonet.BR
import com.tech.sonet.R
import com.tech.sonet.data.local.SharedPrefManager
import com.tech.sonet.databinding.ViewProgressSheetBinding
import com.tech.sonet.ui.base.connectivity.ConnectivityProvider
import com.tech.sonet.utils.AlertManager
import com.tech.sonet.utils.event.NoInternetSheet
import com.tech.sonet.utils.hideKeyboard
import com.tech.sonet.data.network.ErrorCodes
import com.tech.sonet.data.network.NetworkError
import com.tech.sonet.ui.homepage.HomePageActivity
import javax.inject.Inject

abstract class BaseActivity<Binding : ViewDataBinding> : AppCompatActivity(),
    ConnectivityProvider.ConnectivityStateListener {
    private  var progressSheet: ProgressSheet?=null
    open val onRetry: (() -> Unit)? = null
    lateinit var binding: Binding
    val app: App
        get() = application as App

    private lateinit var connectivityProvider: ConnectivityProvider
    private var noInternetSheet: NoInternetSheet? = null

    @Inject
    lateinit var sharedPrefManager : SharedPrefManager

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout: Int = getLayoutResource()
        binding = DataBindingUtil.setContentView(this, layout)
        val vm = getViewModel()
        binding.setVariable(BR.vm, vm)
        vm.onUnAuth.observe(this) {
            showUnauthorised()
        }
      //  binding.setVariable(BR.vm, getViewModel())
        connectivityProvider = ConnectivityProvider.createProvider(this)
        connectivityProvider.addListener(this)
        onCreateView()
    }

    protected abstract fun getLayoutResource(): Int
    protected abstract fun getViewModel(): BaseViewModel
    protected abstract fun onCreateView()

    fun showToast(msg: String? = "Something went wrong !!") {
        Toast.makeText(this, msg ?: "Showed null value !!", Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()
        hideKeyboard()
    }
    fun showLoading(s: Int) {
        showLoading(getString(s))
    }
    fun showLoading(s: String?) {
        progressSheet?.dismissAllowingStateLoss()
        progressSheet = ProgressSheet(object : ProgressSheet.BaseCallback {
            override fun onClick(view: View?) {}
            override fun onBind(bind: ViewProgressSheetBinding) {
                progressSheet?.showMessage(s);
            }
        })
        progressSheet?.isCancelable=false
        progressSheet?.show(supportFragmentManager, progressSheet?.tag)

    }

    fun hideLoading() {
     progressSheet?.dismissAllowingStateLoss()
    }

    fun onError(error: Throwable, showErrorView: Boolean) {
        if (error is NetworkError) {
            //show if you have any error view
            /*   if (showErrorView) {
                   val errorView: View? = findViewById(R.id.errorView)
                   errorView?.visibility = View.VISIBLE
               }*/
            when (error.errorCode) {
                ErrorCodes.SESSION_EXPIRED -> {
                    showToast(getString(R.string.session_expired))
                    app.onLogout()
                }
                else -> AlertManager.showNegativeAlert(
                    this,
                    error.message,
                    getString(R.string.alert)
                )
            }
        } else {
            AlertManager.showNegativeAlert(
                this,
                getString(R.string.please_try_again),
                getString(R.string.alert)
            )
        }
    }
    fun showUnauthorised() {
        sharedPrefManager.clear()
        val intent = Intent(this, HomePageActivity::class.java)
        startActivity(intent)
//        startActivity(LoginActivity.newIntent(this))
        finishAffinity()
    }
    override fun onDestroy() {
        progressSheet?.dismissAllowingStateLoss()
        connectivityProvider.removeListener(this)
        super.onDestroy()
    }


    override fun onStateChange(state: ConnectivityProvider.NetworkState) {

        if (supportFragmentManager.isStateSaved){
            return
        }

        if (noInternetSheet == null) {
            noInternetSheet = NoInternetSheet()
            noInternetSheet?.isCancelable = false
        }
        if (state.hasInternet()) {
            if (noInternetSheet?.isVisible == true)
                noInternetSheet?.dismiss()
        } else {
            if (noInternetSheet?.isVisible == false)
                noInternetSheet?.show(supportFragmentManager, noInternetSheet?.tag)
        }
    }

    private fun ConnectivityProvider.NetworkState.hasInternet(): Boolean {
        return (this as? ConnectivityProvider.NetworkState.ConnectedState)?.hasInternet == true
    }

}
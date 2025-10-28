package com.tech.sonet.ui.terms_conditions

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.tech.sonet.R
import com.tech.sonet.databinding.ActivityTermsConditionsBinding
import com.tech.sonet.ui.base.BaseActivity
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.ui.homepage.HomePageActivity
import com.tech.sonet.ui.login.LoginActivity
import com.tech.sonet.ui.signup.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermsConditionsActivity : BaseActivity<ActivityTermsConditionsBinding>() {

    private val viewModel : TermsConditionVm by viewModels()

    override fun getLayoutResource(): Int {
        return  R.layout.activity_terms_conditions
    }

    override fun getViewModel(): BaseViewModel {
         return viewModel
    }

    override fun onCreateView() {
      initOnClick()
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.webView.loadUrl("https://sonet-singles-nearby.com/terms-and-conditions")
        binding.webView.webViewClient = WebViewClient()
        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100) {
                    binding.loadingIndicator.visibility = View.GONE
                } else {
                    binding.loadingIndicator.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initOnClick() {
        viewModel.onClick.observe(this, Observer {
            when(it?.id){
                R.id.ivCross ->{
                    val intent = Intent(this, HomePageActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.agreeTermsBtn ->{
                    val intent = Intent(this , SignUpActivity:: class.java)
                    startActivity(intent)
                    finish()
                }
            }
        })
    }
}
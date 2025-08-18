package com.tech.sonet.ui.web_view

import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.tech.sonet.R
import com.tech.sonet.databinding.ActivityWebBinding
import com.tech.sonet.ui.base.BaseActivity
import com.tech.sonet.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebActivity : BaseActivity<ActivityWebBinding>() {

    private val viewModel : WebActivityVm by viewModels()
    private var url : String ? = null
    private var heading : String ? = null


    override fun getLayoutResource(): Int {
        return R.layout.activity_web
    }

    override fun getViewModel(): BaseViewModel {
      return  viewModel
    }

    override fun onCreateView() {
        initOnClick()
        getData()

    }

    private fun getData() {
        url = intent.getStringExtra("url")
        heading = intent.getStringExtra("heading")

        if (url != null && heading != null){
            binding.tvHeading.text = heading
            binding.webView.loadUrl(url.toString())
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
    }

    private fun initOnClick() {
      viewModel.onClick.observe(this , Observer {
          when(it?.id){
              R.id.ivBack ->{
                  onBackPressedDispatcher.onBackPressed()
              }

          }
      })
    }
}
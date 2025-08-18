package com.tech.sonet.ui.request

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.tech.sonet.R
import com.tech.sonet.data.model.LikeReceiveApiResponse
import com.tech.sonet.data.model.response.Data
import com.tech.sonet.databinding.RequestPopupActivityBinding
import com.tech.sonet.ui.base.BaseActivity
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.ui.chart.CardImageActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@AndroidEntryPoint
class RequestViewActivity : BaseActivity<RequestPopupActivityBinding>() {
    private val viewModel: RequestViewVM by viewModels()
   var str:String=""
    override fun getLayoutResource(): Int {
        return R.layout.request_popup_activity
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        val bundle = intent.extras
        if (bundle != null) {
            val data = bundle.getParcelable<LikeReceiveApiResponse.LikeReceiveApiResponseData>("data")
            data as LikeReceiveApiResponse.LikeReceiveApiResponseData
            binding.bean = data
            str= data.post_image.toString()
        }
        initOnClick()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.Ivback -> {
                    onBackPressedDispatcher.onBackPressed()
                }
                R.id.request_image->{
                    CardImageActivity.imageLoad = str
                    val intent = Intent(this, CardImageActivity::class.java)
                    startActivity(intent)
                }
                R.id.screenshot->{
//                    val tv = findViewById<TextView>(R.id.screenshot)
//                    val bitmap = captureScreenshot(binding.ConstraintLayout)
//                    if (bitmap != null) {
//                        saveMediaToStorage(bitmap)
//                    }

                }
            }
        }
    }
    private fun captureScreenshot(v: View): Bitmap? {
        var screenshot: Bitmap? = null
        try {
            screenshot =
                Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(screenshot)
            v.draw(canvas)
        } catch (e: Exception) {
            Log.e("sonate", "Failed to capture screenshot because:" + e.message)
        }
        return screenshot
    }


    private fun saveMediaToStorage(bitmap: Bitmap) {
        val filename = "${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.contentResolver?.also { resolver ->

                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(this, "Captured View and saved to Gallery", Toast.LENGTH_SHORT).show()
        }
    }

}


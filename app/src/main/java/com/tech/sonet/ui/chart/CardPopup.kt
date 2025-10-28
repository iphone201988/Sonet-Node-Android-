package com.tech.sonet.ui.chart

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
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.tech.sonet.R
import com.tech.sonet.data.model.CardDataListApiResponse
import com.tech.sonet.data.model.RequestsSentApiResponse
import com.tech.sonet.data.model.response.CardData
import com.tech.sonet.databinding.CardPopupBinding
import com.tech.sonet.ui.base.BaseActivity
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.ui.welcome.WelcomeActivity
import com.tech.sonet.utils.Constant
import com.tech.sonet.utils.ImageBindingUtil
import com.tech.sonet.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@AndroidEntryPoint
class CardPopup : BaseActivity<CardPopupBinding>() {
    private val viewModel: CardPopupVM by viewModels()

    var cardid  : String ? = null
    var str:String=""

    companion object{
        val cardData : CardDataListApiResponse.CardDataListApiResponseData ? = null
    }

    override fun getLayoutResource(): Int {
        return R.layout.card_popup
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        initOnClick()
        initObserver()
    }

    override fun onStart() {
        super.onStart()
        val bundle = intent.extras
        val mapData = bundle?.getParcelable<CardDataListApiResponse.CardDataListApiResponseData>("mapDAta")
        val data = bundle?.getParcelable<CardDataListApiResponse.CardDataListApiResponseData>("cardData")
        if(mapData != null){
            binding.bean = mapData
            cardid = mapData._id

            Log.i("fdsfsdf", "onStart: ${data?.post_image}")
            str= mapData?.post_image.toString()

        }else{
            binding.bean = data
            cardid = data?._id ?: ""
            Log.i("fdsfsdf", "onStart: ${data?.post_image}")

            str= data?.post_image.toString()
        }
    }

    private fun initOnClick() {
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.ivCross -> {
                    onBackPressedDispatcher.onBackPressed()
                }
                R.id.screenshot,R.id.screenshot1 -> {
//                    val tv = findViewById<TextView>(R.id.screenshot)
//                    val bitmap = captureScreenshot(binding.cl)
//                    if (bitmap != null) {
//                        saveMediaToStorage(bitmap)
//                    }
                }
                R.id.request_image->{
                    CardImageActivity.imageLoad=str
                    val intent = Intent(this, CardImageActivity::class.java)
                    startActivity(intent)
                }

                R.id.request , R.id.clMain -> {
                    val data = HashMap<String, Any>()
                    data["cardId"] = cardid.toString()
                    if (data != null){
                        viewModel.requestSent(data,Constant.REQUEST_SENT)
                    }
//                    viewModel.requestsend(
//                        map,
//                        sharedPrefManager.getCurrentUser()?.id.toString(),
//                        sharedPrefManager.getCurrentUser()?.session_key.toString()
//                    )
                }
            }
        }
    }

    private fun initObserver() {
        viewModel.obrCommon.observe(this , Observer {
            when(it?.status){
                Status.LOADING ->{
                    showLoading("Loading")
                }
                Status.SUCCESS ->{
                    hideLoading()
                    val myDataModel : RequestsSentApiResponse ? = ImageBindingUtil.parseJson(it.data.toString())
                    if (myDataModel != null){
                        if (myDataModel.data != null){
                            binding.request.text = "Like Sent"
                            binding.request.isClickable = false
                            binding.request.isEnabled = false
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
//        viewModel.obsSendRequest.observe(this) {
//            when (it?.status) {
//                Status.LOADING -> {
//                    showLoading("Loading")
//                }
//                Status.SUCCESS -> {
//                    hideLoading()
//                    binding.request.text = "Like Sent"
//                }
//                Status.ERROR -> {
//                    hideLoading()
//                    showToast(it.message.toString())
//                }
//                else -> {}
//            }
//        }
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

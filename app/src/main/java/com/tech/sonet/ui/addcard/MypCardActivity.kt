package com.tech.sonet.ui.addcard

import RealPathUtilExits.getFilePath
import UtilMethod
import android.app.Activity
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.tech.sonet.BR
import com.tech.sonet.R
import com.tech.sonet.data.model.CardUpdateApiResponse
import com.tech.sonet.data.model.CardUploadApiResponse
import com.tech.sonet.data.model.GetCategoryApiResponse
import com.tech.sonet.data.model.GetLevelsApiResponse
import com.tech.sonet.data.model.GetMyCardApiResponse
import com.tech.sonet.data.model.UploadCardApiResponse
import com.tech.sonet.data.model.UploadImageApiResponse
import com.tech.sonet.data.model.response.*
import com.tech.sonet.databinding.*
import com.tech.sonet.ui.HomeActivity
import com.tech.sonet.ui.base.BaseActivity
import com.tech.sonet.ui.base.BaseCustomBottomSheet
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.ui.base.SimpleRecyclerViewAdapter
import com.tech.sonet.ui.welcome.WelcomeActivity
import com.tech.sonet.utils.Constant
import com.tech.sonet.utils.ImageBindingUtil
import com.tech.sonet.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@AndroidEntryPoint
class MypCardActivity : BaseActivity<MypcardBinding>() {

    private val viewModel: MypCardActivityVM by viewModels()
    var online: Int? = 1
    var id :String ? =null
    var image: String? = null
    var imagePart: MultipartBody.Part? = null
    private var uploadImageUrl : String ? = null

    override fun getLayoutResource(): Int {
        return R.layout.mypcard_
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


        val bundle = intent.extras
        if (bundle != null) {
            val data = bundle.getParcelable<GetMyCardApiResponse.GetMyCardApiResponseData>("mycard")
            data as GetMyCardApiResponse.GetMyCardApiResponseData
            binding.bean = data
            if (data.mobile != null){
                binding.phone.setText(data.mobile)
            }

            id = data._id!!
            levelId = data.levelId!!.toString()
            categoryId = data.categoryId!!.toString()
            online = data.isOnline!!
            image = data.post_image ?:""
            Log.i("9+8+9859+5959+5", "lCreateView: " + online)
            Log.i("dfsfsfs", "onCreateView: $image")
        } else {
            var mobile = sharedPrefManager.getCurrentUser()?.data?.mobile
            if (mobile != null){
                binding.phone.setText(mobile)
            }

            Log.i("trfur6euy", "lkp;i8rdk: ")
        }
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute(Runnable {
            showLoading("please wait")
            val request = image?.let {
                if (it.startsWith("http://") || it.startsWith("https://")) {
                    Request.Builder().url(it).build()
                } else {
                    throw IllegalArgumentException("Invalid URL: $it. Ensure it starts with http:// or https://")
                }
            }
            val client = OkHttpClient()
            val response = request?.let { client.newCall(it).execute() }

            response?.body?.byteStream()?.use { inputStream ->
                val extension = image?.substringAfterLast(".", "")
                val fileName = "image.${extension}"

                val file = File.createTempFile("image", ".$extension")
                file.deleteOnExit()
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }

                val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)


                imagePart = MultipartBody.Part.createFormData("file", fileName, requestFile)
            }
            handler.post {
                hideLoading()

            }
        })
        initOnClick()
        initObserver()

        binding.header.mypcard.text = "My Profile"
        binding.header.backHome.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            finish()
        }

        binding.etDescription.addTextChangedListener(textWatcher)

    }



    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val count = s?.length ?: 0
            binding.textNumber.text = "$count/150"
        }
    }

    private fun initOnClick() {
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.category_choice, R.id.down_arrow1 -> {
                    viewModel.getCategory(Constant.GET_CATEGORY)
                }
                R.id.level_choice, R.id.down_arrow -> {
                    viewModel.getLevels(Constant.GET_LEVEL)
//                    viewModel.level(
//                        sharedPrefManager.getCurrentUser()?.id.toString(),
//                        sharedPrefManager.getCurrentUser()?.session_key.toString()
//                    )
                }
                R.id.imgEdit -> {
                    getImage()
                }
                R.id.save_btn -> {
                    val bundle = intent.extras
                    if (bundle == null) {
                        saveCard()
                        Log.i("hghjg", "initOnClick: save")
                    } else {
                        updateCard()
                        Log.i("hghjg", "initOnClick: up")
                    }
                }
                R.id.toggleBtn -> {
                    if (online == 0) {
                        online = 1
                        binding.toggleBtn.trackDrawable =
                            ContextCompat.getDrawable(this, R.drawable.switch_button_background)
                    } else if (online == 1) {
                        online = 0
                        binding.toggleBtn.trackDrawable = ContextCompat.getDrawable(
                            this, R.drawable.toggle_btn_background
                        )

                    }
                }
            }
        }
    }

    private lateinit var initbottomsheet: BaseCustomBottomSheet<BottomSheetBinding>
    private lateinit var initAdapter: SimpleRecyclerViewAdapter<GetCategoryApiResponse.GetCategoryApiResponseData, BottomListBinding>

    var categoryId = "1"
    var levelId = "1"

    private fun showBottomsheet(data: List<GetCategoryApiResponse.GetCategoryApiResponseData?>?) {
        initbottomsheet = BaseCustomBottomSheet(
            this, R.layout.bottom_sheet
        ) {

        }
        initAdapter = SimpleRecyclerViewAdapter(
            R.layout.bottom__list, BR.bean
        ) { v, m, pos ->
            when (v.id) {
                R.id.cl -> {
                    binding.categoryChoice.text = m.category_name
                    categoryId = m._id!!
                    Glide.with(this).load(m.category_image).into(binding.categoryPic)
                    initbottomsheet.dismiss()
                }
            }
        }
        initbottomsheet.binding.recy.adapter = initAdapter
        initbottomsheet.show()
        initbottomsheet.setCancelable(true)
        initAdapter.list = data
    }

    private lateinit var initbottom: BaseCustomBottomSheet<LevelBottomSheetBinding>
    private lateinit var adapter: SimpleRecyclerViewAdapter<GetLevelsApiResponse.GetLevelsApiResponseData, LevelBottomListBinding>
    private fun showBottomSheet(data: List<GetLevelsApiResponse.GetLevelsApiResponseData?>?) {
        initbottom = BaseCustomBottomSheet(
            this, R.layout.level_bottom_sheet
        ) {

        }

        adapter = SimpleRecyclerViewAdapter(
            R.layout.level_bottom__list, BR.bean
        ) { v, m, pos ->
            when (v.id) {
                R.id.level -> {
                    binding.levelChoice.text = m.level
                    levelId = m._id.toString()
                    initbottom.dismiss()
                }
            }
        }
        initbottom.binding.recyclerview.adapter = adapter
        initbottom.show()
        initbottom.setCancelable(true)
        adapter.list = data
    }

    private fun saveCard() {
        if (imageMultipart == null) {
            showToast("Please select an image")
            return
        }
        if (binding.textTitle.text.isEmpty()) {
            showToast("Please enter title")
            return
        }
        if (binding.etDescription.text.isEmpty()) {
            showToast("Please enter  description")
            return
        }
        if (binding.levelChoice.text.isEmpty()) {
            showToast("Please select  Level")
            return
        }
        if (binding.categoryChoice.text.isEmpty()) {
            showToast("Please select  category")
            return
        }
//        val map = HashMap<String, RequestBody>()
//        map["title"] = binding.textTitle.text.toString().trim().toRequestBody()
//        map["description"] = binding.etDescription.text.toString().trim().toRequestBody()
//        map["latitude"] = WelcomeActivity.lat.toRequestBody()
//        map["longitude"] = WelcomeActivity.long.toRequestBody()
//        map["mobile"] = binding.phone.text.toString().trim().toRequestBody()
//        map["social_media"] = binding.socialMedia.text.toString().trim().toRequestBody()
//        map["isOnline"] = online.toString().toRequestBody()
//        Log.i("fhrghk", "sfdhjkhgk$online")
//        viewModel.cardsUpload(
//            map,
//            imageMultipart!!,
//            levelId,
//            categoryId,
////            sharedPrefManager.getCurrentUser()?.id.toString(),
////            sharedPrefManager.getCurrentUser()?.session_key.toString()
//        )

        val data = HashMap<String,Any>()
        data["title"] = binding.textTitle.text.toString().trim()
        data["description"] = binding.etDescription.text.toString().trim()
        data["social_media"] = binding.socialMedia.text.toString().trim()
        data["post_image"] = uploadImageUrl.toString()
        data["categoryId"] = categoryId
        data["levelId"]  = levelId
        data["isOnline"] = online!!
        data["is_active"] = 1
        data["mobile"] = binding.phone.text.toString().trim()
        data["location"] = mapOf(
            "type" to "Point", "coordinates" to listOf(
                WelcomeActivity.long , WelcomeActivity.lat
            ))

        viewModel.cardUpload(data,Constant.UPLOAD_CARDS)
    }

    private fun getImage() {
        ImagePicker.with(this)
            .start()
    }


    private var imageUri: Uri? = null
    var imageMultipart: MultipartBody.Part? = null
    var photoFile: File? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                imageUri = data?.data!!
                imageMultipart =  convertImageToMultipart(imageUri!!)
                photoFile = File(imageUri.getFilePath(this))
               // imageMultipart = UtilMethod.createMultiPart(photoFile!!.path, "image_file", "jpeg")
                binding.cardImage.setImageURI(imageUri)

                viewModel.uploadImage(imageMultipart, Constant.UPLOAD_PHOTO)
            }
            ImagePicker.RESULT_ERROR -> {
                showToast(ImagePicker.getError(data))
            }
            else -> {
            }
        }
    }
    private fun convertImageToMultipart(imageUri: Uri): MultipartBody.Part {
        val file = com.github.dhaval2404.imagepicker.util.FileUtil.getTempFile(this, imageUri)
        val fileName =
            "${file!!.nameWithoutExtension}_${System.currentTimeMillis()}.${file.extension}"
        val newFile = File(file.parent, fileName)
        file.renameTo(newFile)
        return MultipartBody.Part.createFormData(
            "file", newFile.name, newFile.asRequestBody("image/*".toMediaTypeOrNull())
        )
    }
    private fun updateCard() {

        if (uploadImageUrl != null){
            val data = HashMap<String,Any>()
            data["title"] = binding.textTitle.text.toString().trim()
            data["description"] = binding.etDescription.text.toString().trim()
            data["social_media"] = binding.socialMedia.text.toString().trim()
            data["post_image"] = uploadImageUrl.toString()
            data["categoryId"] = categoryId
            data["levelId"]  = levelId
            data["isOnline"] = online!!
            data["mobile"] = binding.phone.text.toString().trim()
            data["is_active"] = 1
            data["location"] = mapOf(
                "type" to "Point", "coordinates" to listOf(
                    WelcomeActivity.long , WelcomeActivity.lat
                ))

            viewModel.cardUpdate(data,Constant.UPDATE_CARD+id)
        }else{
            val data = HashMap<String,Any>()
            data["title"] = binding.textTitle.text.toString().trim()
            data["description"] = binding.etDescription.text.toString().trim()
            data["social_media"] = binding.socialMedia.text.toString().trim()
            data["post_image"] = image.toString()
            data["categoryId"] = categoryId
            data["levelId"]  = levelId
            data["isOnline"] = online!!
            data["mobile"] = binding.phone.text.toString().trim()
            data["is_active"] = 1
            data["location"] = mapOf(
                "type" to "Point", "coordinates" to listOf(
                    WelcomeActivity.long , WelcomeActivity.lat
                ))

            viewModel.cardUpdate(data,Constant.UPDATE_CARD+id)
        }




    }


    private suspend fun downloadImageAsMultipart(imageUrl: String): MultipartBody.Part? =
        withContext(Dispatchers.IO) {
            val request = Request.Builder().url(imageUrl).build()

            val client = OkHttpClient()
            val response = client.newCall(request).execute()

            response.body?.byteStream()?.use { inputStream ->
                val extension = imageUrl.substringAfterLast(".", "")
                val fileName = "image.${extension}"

                val file = File.createTempFile("image", ".$extension")
                file.deleteOnExit()
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }

                val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                return@withContext MultipartBody.Part.createFormData("image", fileName, requestFile)
            }

            return@withContext null
        }


    private fun initObserver() {

        viewModel.obrCommon.observe(this, Observer {
            when(it?.status){
                Status.LOADING ->{
                    showLoading("Loading")
                }
                Status.SUCCESS ->{
                    hideLoading()
                    when(it.message){
                        "postCard" ->{
                            val myDataModel : CardUploadApiResponse ? = ImageBindingUtil.parseJson(it.data.toString())
                            if (myDataModel != null){
                                if (myDataModel.data != null){
                                    val intent = Intent(this, HomeActivity::class.java)
                                    startActivity(intent)
                                    finish()                        }
                            }
                        }

                        "getCategory" ->{
                            val myDataModel : GetCategoryApiResponse ? = ImageBindingUtil.parseJson(it.data.toString())
                            if (myDataModel != null){
                                if (myDataModel.data != null){
                                    showBottomsheet(myDataModel.data)

                                }
                            }
                        }
                        "getLevels" ->{
                            val myDataModel : GetLevelsApiResponse ? = ImageBindingUtil.parseJson(it.data.toString())
                            if (myDataModel != null){
                                if (myDataModel.data != null){
                                    showBottomSheet(myDataModel.data)
                                }
                            }
                        }
                        "uploadPhoto" ->{
                           val myDataModel : UploadImageApiResponse ? = ImageBindingUtil.parseJson(it.data.toString())
                            if (myDataModel != null){
                                if (myDataModel.data != null){
                                    uploadImageUrl = myDataModel.data!!.fileUrl
                                }
                            }
                        }
                        "cardUpload" -> {
                            val myDataModel: UploadCardApiResponse? =
                                ImageBindingUtil.parseJson(it.data.toString())
                            if (myDataModel != null) {
                                if (myDataModel.data != null) {
                                    val intent = Intent(this, HomeActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }
                        "cardUpdate" ->{
                            val myDataModel : CardUpdateApiResponse ? = ImageBindingUtil.parseJson(it.data.toString())
                            if (myDataModel != null){
                                if (myDataModel.data != null){
                                    showToast("Profile Updated")
                                    val intent = Intent(this, HomeActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
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
//        viewModel.obsCardUpload.observe(this) {
//            when (it?.status) {
//                Status.LOADING -> {
//                    showLoading("Loading")
//                }
//                Status.SUCCESS -> {
//                    hideLoading()
//                    val intent = Intent(this, HomeActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }
//                Status.ERROR -> {
//                    hideLoading()
//                    showToast(it.message.toString())
//                }
//                else -> {}
//            }
//        }

        viewModel.obsCardUpdate.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showLoading("Loading")
                }
                Status.SUCCESS -> {
                    hideLoading()
                    Log.i("dgdgfd", "initObserver: ${it.data}")
                    showToast("Profile Updated")
                    onBackPressedDispatcher.onBackPressed()
                }
                Status.ERROR -> {
                    hideLoading()
                    Log.i("dgdgfd", "initObserver: ${it.message}")
                    showToast(it.message.toString())
                }
                else -> {}
            }

        }
    }
}

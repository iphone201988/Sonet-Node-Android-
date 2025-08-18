package com.tech.sonet.ui.request

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tech.sonet.R
import com.tech.sonet.data.api.SimpleApiResponse
import com.tech.sonet.data.model.LikeReceiveApiResponse
import com.tech.sonet.data.model.RequestStatusApiResponse
import com.tech.sonet.databinding.CarddeletePopupBinding
import com.tech.sonet.databinding.ClearAllPopupBinding
import com.tech.sonet.databinding.RequestActivityBinding
import com.tech.sonet.ui.CustomSwipeMenuAdapter
import com.tech.sonet.ui.base.BaseActivity
import com.tech.sonet.ui.base.BaseCustomDialog
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.utils.Constant
import com.tech.sonet.utils.ImageBindingUtil
import com.tech.sonet.utils.Status
import com.tech.sonet.utils.VerticalPagination
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.HashMap

@AndroidEntryPoint
class RequestActivity : BaseActivity<RequestActivityBinding>(), BaseCustomDialog.Listener {
    private val viewModel: RequestVM by viewModels()

    private var pagination: VerticalPagination? = null
    var page = 1
    var isLoading = 1
    lateinit var dialogDelete: BaseCustomDialog<CarddeletePopupBinding>
    lateinit var clearAllPopup: BaseCustomDialog<ClearAllPopupBinding>

    companion object {
        const val TAG = "Request Activity"

        @JvmStatic
        fun newInstance() = RequestActivity().apply {

        }

    }

    override fun getLayoutResource(): Int {
        return R.layout.request_activity
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView() {
        initPopup()
        getRequestList()
        initObserver()
        initOnClick()
        initAdapter()
    }

    private fun initPopup() {
        clearAllPopup  = BaseCustomDialog(this, R.layout.clear_all_popup, this)

    }

    private fun initOnClick() {
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.backimg -> {
                    onBackPressedDispatcher.onBackPressed()
                }
                R.id.IvLiked -> {
//                    AcceptedActivity.side = "Received"
//                    val intent = Intent(this, AcceptedActivity::class.java)
//                    startActivity(intent)
                }
                R.id.tvClearAll -> {

                    clearAllPopup.show()
                    clearAllPopup.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

//                    viewModel.clearRequest(
//                        sharedPrefManager.getCurrentUser()?.id.toString(),
//                        sharedPrefManager.getCurrentUser()?.session_key.toString()
//                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initObserver() {

        viewModel.obrCommon.observe(this , androidx.lifecycle.Observer {
            when(it?.status){
                Status.LOADING ->{
                    showLoading("Loading")
                }
                Status.SUCCESS ->{
                    hideLoading()
                    when(it.message){
                        "getRequestList" ->{
                            val myDataModel : LikeReceiveApiResponse ? = ImageBindingUtil.parseJson(it.data.toString())
                            if (myDataModel != null){
                                if (myDataModel.data != null){
                                    if (myDataModel.data!!.isNotEmpty()) {
                                        binding.rvRequest.visibility = View.VISIBLE
                                        binding.tvNoData.visibility = View.GONE
                                        binding.tvClearAll.visibility = View.VISIBLE
                                        try {
//                                            if (myDataModel.links?.currentPage.toString() < myDataModel.links?.totalPages.toString()) {
//                                                pagination?.isLoading = false
//                                            }
                                            isLoading = if (page != myDataModel.links?.totalPages){
                                                1
                                            }else{
                                                pagination?.isLoading = false
                                                0
                                            }
                                            if (myDataModel.links?.currentPage.toString() == "1") {
                                                cs.setList(myDataModel.data)
                                            } else {
                                                cs.addToList(myDataModel.data)

                                            }
                                        }catch ( e :Exception){
                                            e.printStackTrace()
                                        }

                                    } else {
                                        binding.rvRequest.visibility = View.GONE
                                        binding.tvNoData.visibility = View.VISIBLE
                                        binding.tvClearAll.visibility = View.GONE
                                    }

//                                    for (i in myDataModel.data!!.indices) {
//                                        val inputDate = myDataModel.data!![i]?.createdAt
//                                        val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//                                        val outputFormat = DateTimeFormatter.ofPattern("MMMM dd HH:mm")
//                                        try {
//                                            val dateTime = LocalDateTime.parse(inputDate, inputFormat)
//                                            val formattedTime = outputFormat.format(dateTime)
//                                            myDataModel.data?.get(i)?.createdAt = formattedTime
//                                        } catch (e: ParseException) {
//                                            e.printStackTrace()
//                                        }
//                                    }
                                }
                            }
                        }
                        "requestStatus" ->{
                            val myDataModel : RequestStatusApiResponse ? = ImageBindingUtil.parseJson(it.data.toString())
                            if (myDataModel != null){
                                if (myDataModel.data != null){
                                     getRequestList()
                                }
                            }
                        }
                        "deleteRequest" ->{
                            val myDataModel : SimpleApiResponse? = ImageBindingUtil.parseJson(it.data.toString())
                            if (myDataModel != null)
                            {
                                    showToast("Delete post successfully")
                                    dialogDelete.dismiss()
                                    val data = HashMap<String,Any>()
                                    data["type"] = "receiver"
                                    viewModel.getRequestList(data,Constant.LIKE_SENT)


                            }
                        }
                        "deleteAllRequest" ->{
                            val myDataModel : SimpleApiResponse? = ImageBindingUtil.parseJson(it.data.toString())
                            if (myDataModel != null)
                            {
                                clearAllPopup.dismiss()
                                showToast("Delete posts successfully")
                                val data = HashMap<String,Any>()
                                data["type"] = "receiver"
                                viewModel.getRequestList(data,Constant.LIKE_SENT)


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

//        viewModel.obsAccept.observe(this) {
//            when (it?.status) {
//                Status.LOADING -> {
//                    showLoading("Loading")
//                }
//
//                Status.SUCCESS -> {
//                    hideLoading()
//                    getRequestList()
//                }
//
//                Status.ERROR -> {
//                    hideLoading()
//                    showToast(it.message.toString())
//                }
//                else -> {}
//            }
//        }
//        viewModel.observeClearRequest.observe(this) {
//            when (it?.status) {
//                Status.LOADING -> {
//                    showLoading("Loading")
//                }
//                Status.SUCCESS -> {
//                    hideLoading()
//                    getRequestList()
//                }
//                Status.ERROR -> {
//                    hideLoading()
//                    showToast(it.message.toString())
//                }
//
//                else -> {}
//            }
//        }
//        viewModel.obsDelete.observe(this) {
//            when (it?.status) {
//                Status.LOADING -> {
//                    hideLoading()
//                }
//                Status.SUCCESS -> {
//                    hideLoading()
//                    getRequestList()
//                }
//                Status.ERROR -> {
//                    hideLoading()
//                    showErrorToast(it.message.toString())
//                }
//                else -> {}
//            }
        }



    private fun getRequestList() {
        val data = HashMap<String,Any>()
        data["type"] = "receiver"
        data["page"] = 1
        data["limit"] = 10
        viewModel.getRequestList(data,Constant.LIKE_SENT)
    }

    var userId: Int? = null
    private val cs = CustomSwipeMenuAdapter(object : CustomSwipeMenuAdapter.CardCallback {
        override fun onItemClick(v: View?, m: LikeReceiveApiResponse.LikeReceiveApiResponseData?, pos: Int?) {

            when (v?.id) {
                R.id.constantSwap -> {
                    val data = HashMap<String, String>()
                    data["user_id"] = userId.toString()
                }
                 R.id.viewData , R.id.favConstantLayout-> {
                    val bundle = Bundle()
                    bundle.putParcelable("data", m)
                    val intent = Intent(this@RequestActivity, RequestViewActivity::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
                R.id.accept_text -> {
                    val data = HashMap<String, Any>()
                    data["type"] = 1
                    data["requestId"] = m?.requestId.toString()
                    viewModel.requestStatus(data,Constant.REQUEST_UPDATE)
//                    viewModel.accept(
//                        map,
//                        sharedPrefManager.getCurrentUser()?.id.toString(),
//                        sharedPrefManager.getCurrentUser()?.session_key.toString()
//                    )
                }
                R.id.reject_like ->{
                    val data = HashMap<String, Any>()
                    data["type"] = 2
                    data["requestId"] = m?.requestId.toString()
                    viewModel.requestStatus(data,Constant.REQUEST_UPDATE)
                }
                R.id.imageview -> {
                    RequestImageActivity.imageLoad = m?.post_image
                    val intent = Intent(this@RequestActivity, RequestImageActivity::class.java)
                    startActivity(intent)
                }
                R.id.tvdelete -> {
                    cardDeleteDialog(m!!)
                }
            }
        }
    })

    var id  : String ? = null
    fun cardDeleteDialog(m: LikeReceiveApiResponse.LikeReceiveApiResponseData) {
        dialogDelete = BaseCustomDialog(this, R.layout.carddelete_popup, this)
        dialogDelete.show()
        dialogDelete.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        id = m.requestId

    }

    override fun onViewClick(view: View?) {
        when (view?.id) {
            R.id.TvNo -> {
                dialogDelete.dismiss()
            }
            R.id.TvYes -> {
                dialogDelete.dismiss()
                val data = HashMap<String,Any>()
                data["delete_by"] = "receiver_delete"
                viewModel.deleteRequest(Constant.DELETE_CARD+id,data)
//                viewModel.delete(
//                    map,
//                    sharedPrefManager.getCurrentUser()?.id.toString(),
//                    sharedPrefManager.getCurrentUser()?.session_key.toString()
//                )
            }
            R.id.clearAll ->{
                val data = HashMap<String,Any>()
                data["delete_by"] = "receiver_delete"
                viewModel.deleteAllRequest(data,Constant.DELETE_ALL_CARDS)
            }
            R.id.clearNo ->{
                clearAllPopup.dismiss()
            }
        }
    }

    fun initAdapter() {
        binding.rvRequest.adapter = cs

        val lm = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        pagination = VerticalPagination(lm, 2)
        pagination?.setListener(object : VerticalPagination.VerticalScrollListener {
            override fun onLoadMore() {
                if (isLoading == 1){
                page++
                val data = HashMap<String,Any>()
                data["type"] = "sender"
                data["limit"] = 10
                data["page"] = page
                viewModel.getRequestList(data,Constant.LIKE_SENT)
                    isLoading = 0
                }
            }
        })
        pagination?.let {
            binding.rvRequest.addOnScrollListener(it)
        }

    }
}


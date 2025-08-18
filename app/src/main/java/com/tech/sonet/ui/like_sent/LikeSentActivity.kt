package com.tech.sonet.ui.like_sent

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tech.sonet.R
import com.tech.sonet.data.api.SimpleApiResponse
import com.tech.sonet.data.model.LikeSentApiResponse
import com.tech.sonet.databinding.ActivityLikeSentBinding
import com.tech.sonet.databinding.CarddeletePopupBinding
import com.tech.sonet.databinding.ClearAllPopupBinding
import com.tech.sonet.ui.base.BaseActivity
import com.tech.sonet.ui.base.BaseCustomDialog
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.ui.request.RequestImageActivity
import com.tech.sonet.utils.Constant
import com.tech.sonet.utils.ImageBindingUtil
import com.tech.sonet.utils.Status
import com.tech.sonet.utils.VerticalPagination
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LikeSentActivity : BaseActivity<ActivityLikeSentBinding>() , BaseCustomDialog.Listener {

    private val viewModel : LikeSentActivityVm  by viewModels()

    lateinit var dialogDelete: BaseCustomDialog<CarddeletePopupBinding>
    lateinit var clearAllPopup: BaseCustomDialog<ClearAllPopupBinding>

    private var pagination: VerticalPagination? = null
    var page = 1
    var isLoading = 1


//    private lateinit var likeSentAdapter : SimpleRecyclerViewAdapter<LikeSentApiResponse.LikeSentApiResponseData,ItemLayoutLikeSentBinding>

    override fun getLayoutResource(): Int {
        return  R.layout.activity_like_sent
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        initPopup()
        initOnClick()
        val data = HashMap<String,Any>()
        data["type"] = "sender"
        data["limit"] = 10
        data["page"] = 1
        viewModel.likeSentData(data,Constant.LIKE_SENT)
        setObserver()
        initAdapter()
    }

    private fun initPopup() {
        clearAllPopup  = BaseCustomDialog(this, R.layout.clear_all_popup, this)

    }

    fun initAdapter() {
        binding.rvLikeSent.adapter = cs

        val lm = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        pagination = VerticalPagination(lm, 2)
        pagination?.setListener(object : VerticalPagination.VerticalScrollListener {
            override fun onLoadMore() {
                if (isLoading == 1) {
                    page++
                    val data = HashMap<String, Any>()
                    data["type"] = "sender"
                    data["limit"] = 10
                    data["page"] = page
                    viewModel.likeSentData(data, Constant.LIKE_SENT)
                    isLoading = 0
                }
            }
        })
        pagination?.let {
            binding.rvLikeSent.addOnScrollListener(it)
        }

    }

    private fun setObserver() {
        viewModel.obrCommon.observe(this, Observer {
            when(it?.status){
                Status.LOADING ->{
                    showLoading("Loading")
                }
                Status.SUCCESS ->{
                    hideLoading()
                    when(it.message){
                        "getCard" ->{
                            val myDataModel : LikeSentApiResponse ? = ImageBindingUtil.parseJson(it.data.toString())
                            if (myDataModel != null){
                                if (myDataModel.data!!.isNotEmpty()) {
                                    binding.rvLikeSent.visibility = View.VISIBLE
                                    binding.tvNoData.visibility = View.GONE
                                    binding.tvClearAll.visibility = View.VISIBLE

                                    try {
//                                        if (myDataModel.links?.currentPage.toString() < myDataModel.links?.totalPages.toString()) {
//                                            pagination?.isLoading = false
//                                        }
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
                                    }catch (e: Exception) {
                                        e.printStackTrace()
                                    }

                                } else {
                                    binding.rvLikeSent.visibility = View.GONE
                                    binding.tvNoData.visibility = View.VISIBLE
                                    binding.tvClearAll.visibility = View.GONE
                                }
                            }
                        }
                        "deleteRequest" ->{
                            val myDataModel : SimpleApiResponse ? = ImageBindingUtil.parseJson(it.data.toString())
                            if (myDataModel != null)
                            {
                                    showToast("Delete post successfully")
                                    dialogDelete.dismiss()
                                    val data = HashMap<String,Any>()
                                    data["type"] = "sender"
                                    viewModel.likeSentData(data,Constant.LIKE_SENT)
                                }


                        }
                        "deleteAllRequest" ->{
                            val myDataModel : SimpleApiResponse ? = ImageBindingUtil.parseJson(it.data.toString())
                            if (myDataModel != null)
                            {
                                clearAllPopup.dismiss()
                                showToast("Delete posts successfully")
                                val data = HashMap<String,Any>()
                                data["type"] = "sender"
                                viewModel.likeSentData(data,Constant.LIKE_SENT)
                            }


                        }
                    }

                }
            }

        })
    }

    private fun initOnClick() {
        viewModel.onClick.observe(this , Observer {
            when(it?.id){
                R.id.backimg ->{
                    onBackPressedDispatcher.onBackPressed()

                }
                R.id.IvLiked ->{
//                    AcceptedActivity.side = "Sent"
//                    val intent = Intent(this, AcceptedActivity::class.java)
//                    startActivity(intent)
                }
                R.id.tvClearAll ->{
                        clearAllPopup.show()
                        clearAllPopup.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))



                }
            }
        })
    }

    var userId: Int? = null
    private val cs = CustomLikeSentAdapter(object : CustomLikeSentAdapter.CardCallback {
        override fun onItemClick(
            v: View?,
            m: LikeSentApiResponse.LikeSentApiResponseData,
            pos: Int?
        ) {
            when (v?.id) {
                R.id.constantSwap -> {
                    val data = HashMap<String, String>()
                    data["user_id"] = userId.toString()
                }
                R.id.view , R.id.consMain-> {
                    val bundle = Bundle()
                    bundle.putParcelable("data", m)
                    val intent = Intent(this@LikeSentActivity, LikeSentDetailActivity::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }

                R.id.imageview -> {
                    RequestImageActivity.imageLoad = m?.post_image
                    val intent = Intent(this@LikeSentActivity, RequestImageActivity::class.java)
                    startActivity(intent)
                }
                R.id.tvdelete -> {
                    cardDeleteDialog(m)
                }

            }
        }
    })

    var id  : String ? = null
    fun cardDeleteDialog(m: LikeSentApiResponse.LikeSentApiResponseData) {
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
                val data = HashMap<String, Any>()
                data["delete_by"] = "sender_delete"
                viewModel.deleteRequest(Constant.DELETE_CARD+id, data)

//                viewModel.delete(
//                    map,
//                    sharedPrefManager.getCurrentUser()?.id.toString(),
//                    sharedPrefManager.getCurrentUser()?.session_key.toString()
//                )
            }
            R.id.clearAll ->{
                val data = HashMap<String,Any>()
                data["delete_by"] = "sender_delete"
                viewModel.deleteAllRequest(data,Constant.DELETE_ALL_CARDS)
            }
            R.id.clearNo ->{
                clearAllPopup.dismiss()
            }
    }
}
}
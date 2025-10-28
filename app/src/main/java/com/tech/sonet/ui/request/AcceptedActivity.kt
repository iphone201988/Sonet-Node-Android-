package com.tech.sonet.ui.request

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tech.sonet.R
import com.tech.sonet.data.api.SimpleApiResponse
import com.tech.sonet.data.model.AcceptedStatusApiResponse
import com.tech.sonet.data.model.LikeSentApiResponse
import com.tech.sonet.databinding.AcceptedActivityBinding
import com.tech.sonet.databinding.AcceptedListBinding
import com.tech.sonet.databinding.CarddeletePopupBinding
import com.tech.sonet.databinding.ClearAllPopupBinding
import com.tech.sonet.ui.base.BaseActivity
import com.tech.sonet.ui.base.BaseCustomDialog
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.ui.base.SimpleRecyclerViewAdapter
import com.tech.sonet.utils.Constant
import com.tech.sonet.utils.ImageBindingUtil
import com.tech.sonet.utils.Status
import com.tech.sonet.utils.VerticalPagination
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AcceptedActivity : BaseActivity<AcceptedActivityBinding>() , BaseCustomDialog.Listener {
    private val viewModel: AcceptedViewModel by viewModels()
    private lateinit var adapter: SimpleRecyclerViewAdapter<AcceptedStatusApiResponse.AcceptedStatusApiResponseData, AcceptedListBinding>
    private var pagination: VerticalPagination? = null
    lateinit var dialogDelete: BaseCustomDialog<CarddeletePopupBinding>
    lateinit var clearAllPopup: BaseCustomDialog<ClearAllPopupBinding>

    var page = 1

    var isLoading = 1
    companion object{
        var side : String ? = null
    }
    override fun getLayoutResource(): Int {
        return R.layout.accepted_activity
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        getAcceptedList()
        initPopup()
        initObserve()
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
                R.id.tvClearAll ->{
                    clearAllPopup.show()
                    clearAllPopup.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initObserve() {

        viewModel.obrCommon.observe(this , Observer {
            when(it?.status){
                Status.LOADING ->{
                    showLoading("Loading")
                }
                Status.SUCCESS -> {
                    hideLoading()

                    when(it.message){
                        "acceptedStatus" ->{
                            val myDataModel : AcceptedStatusApiResponse ? = ImageBindingUtil.parseJson(it.data.toString())
                            if (myDataModel != null){
                                if (!myDataModel.data.isNullOrEmpty() ){
                                    binding.rvAccepte.visibility = View.VISIBLE
                                    binding.tvNoData.visibility = View.GONE
                                    binding.tvClearAll.visibility = View.VISIBLE
                                    try {
//                                if (myDataModel.links?.currentPage.toString() < myDataModel.links?.totalPages.toString()) {
//                                    pagination?.isLoading = false
//                                }
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
                                    }catch ( e : Exception){
                                        e.printStackTrace()
                                    }
                                    // adapter.list = myDataModel.data
                                }
                                else {
                                    binding.rvAccepte.visibility = View.GONE
                                    binding.tvNoData.visibility = View.VISIBLE
                                    binding.tvClearAll.visibility = View.GONE
                                }

                            }

                        }
                        "deleteRequest" ->{
                            val myDataModel : SimpleApiResponse ? = ImageBindingUtil.parseJson(it.data.toString())
                            if (myDataModel != null){
                                showToast("Profile removed successfully.")
                                dialogDelete.dismiss()
                                getAcceptedList()
                            }
                        }
                        "deleteAllRequest" ->{
                            val myDataModel : SimpleApiResponse ? = ImageBindingUtil.parseJson(it.data.toString())
                            if (myDataModel != null){
                                showToast("Profile removed successfully.")
                                clearAllPopup.dismiss()
                                getAcceptedList()
                            }
                        }
                    }

                }
                else ->{

                }
            }
        })

    }

    private fun getAcceptedList() {

        viewModel.acceptedStatus(Constant.ACCEPTED_STATUS)

//        if (side == "Sent"){
//            val data = HashMap<String,Any>()
//            data["type"] = "sender"
//            viewModel.acceptedStatus(data,Constant.ACCEPTED_STATUS)
//        }
//        else{
//            val data = HashMap<String,Any>()
//            data["type"] = "receiver"
//            viewModel.acceptedStatus(data,Constant.ACCEPTED_STATUS)
//        }

    }
    private fun initAdapter() {
        binding.rvAccepte.adapter = cs

        val lm = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        pagination = VerticalPagination(lm, 2)
        pagination?.setListener(object : VerticalPagination.VerticalScrollListener {
            override fun onLoadMore() {
                if (isLoading == 1) {
                    page++
                    viewModel.acceptedStatus(Constant.ACCEPTED_STATUS)
//                if (side == "Sent"){
//                    val data = HashMap<String,Any>()
//                    data["type"] = "sender"
//                    data["page"] = page
//                    data["limit"] = 10
//                    //viewModel.acceptedStatus(data,Constant.ACCEPTED_STATUS)
//                }
//                else{
//                    val data = HashMap<String,Any>()
//                    data["type"] = "receiver"
//                    data["page"] = page
//                    data["limit"] = 10
//                 //   viewModel.acceptedStatus(data,Constant.ACCEPTED_STATUS)
//                }
                    isLoading = 0
                }
            }
        })
        pagination?.let {
            binding.rvAccepte.addOnScrollListener(it)
        }
    }


    var userId: Int? = null
    private val cs = CustomAcceptedAdapter(object : CustomAcceptedAdapter.CardCallback {

        override fun onItemClick(
            v: View?,
            m: AcceptedStatusApiResponse.AcceptedStatusApiResponseData,
            pos: Int?
        ) {
            when (v?.id) {
                R.id.constantSwap -> {
                    val data = HashMap<String, String>()
                    data["user_id"] = userId.toString()
                }
                R.id.constraintlayout, R.id.tvView -> {
                    val bundle = Bundle()
                    bundle.putParcelable("accepted", m)
                    val intent = Intent(this@AcceptedActivity, AcceptedViewActivity::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }

                R.id.imageview -> {
                    RequestImageActivity.imageLoad = m?.post_image
                    val intent = Intent(this@AcceptedActivity, RequestImageActivity::class.java)
                    startActivity(intent)
                }
                R.id.tvdelete -> {
                    cardDeleteDialog(m)
                }

            }
        }
    })

    var id  : String ? = null
    fun cardDeleteDialog(m: AcceptedStatusApiResponse.AcceptedStatusApiResponseData) {
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
                val data= HashMap<String,Any>()
                data["delete_by"] = "match_delete"
                 viewModel.deleteRequest(Constant.DELETE_CARD+id,data)

            }
            R.id.clearAll ->{
                val data = HashMap<String,Any>()
                data["delete_by"] = "match_delete"
                viewModel.deleteAllRequest(data,Constant.DELETE_ALL_CARDS)
            }
            R.id.clearNo ->{
                clearAllPopup.dismiss()
            }
        }

    }
}
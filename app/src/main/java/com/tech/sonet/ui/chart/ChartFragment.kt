package com.tech.sonet.ui.chart

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationServices
import com.tech.sonet.BR
import com.tech.sonet.R
import com.tech.sonet.data.model.CardDataListApiResponse
import com.tech.sonet.data.model.GetCategoryApiResponse
import com.tech.sonet.data.model.response.CardData
import com.tech.sonet.data.model.response.CategoryData
import com.tech.sonet.databinding.CategoryActivityBinding
import com.tech.sonet.databinding.FragmentChartBinding
import com.tech.sonet.databinding.ItemLayoutFilterBinding
import com.tech.sonet.ui.base.BaseFragment
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.ui.base.SimpleRecyclerViewAdapter
import com.tech.sonet.ui.welcome.WelcomeActivity
import com.tech.sonet.utils.Constant
import com.tech.sonet.utils.ImageBindingUtil
import com.tech.sonet.utils.Status
import com.tech.sonet.utils.VerticalPagination
import com.tech.sonet.utils.showErrorToast
import com.tech.sonet.utils.showSuccessToast
import com.tech.sonet.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class ChartFragment : BaseFragment<FragmentChartBinding>() {

    private val viewModel: ChartFragmentVM by viewModels()
    private lateinit var chartAdapter: SimpleRecyclerViewAdapter<CardDataListApiResponse.CardDataListApiResponseData, CategoryActivityBinding>
    private lateinit var filterAdapter : SimpleRecyclerViewAdapter<GetCategoryApiResponse.GetCategoryApiResponseData, ItemLayoutFilterBinding>
    private var pagination: VerticalPagination? = null
    var page = 1

    var filterData : String ? = null
    var isLoading = 1

    override fun getLayoutResource(): Int {
        return R.layout.fragment_chart
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView(view: View) {
        Log.i( "dfdf", "onCreate: ")
        initObserver()
       initList()
        initOnClick()
        initAdapter()
    }


    override fun onStart() {
        Log.i( "dfdf", "onStart: ")

        super.onStart()
   //    initList()
        getCategory()
    }

    override fun onResume() {
        Log.i( "dfdf", "onResume: ")
        initList()
        super.onResume()

    }
    private fun getCategory() {
        viewModel.getCategory(Constant.GET_CATEGORY)
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.btnUpdate -> {
                    val data = HashMap<String, Any>()
                    data["location"] = mapOf(
                        "type" to "Point", "coordinates" to listOf(
                            WelcomeActivity.long.toDouble(), WelcomeActivity.lat.toDouble()
                        )
                    )

                    val queryData = HashMap<String,Any>()
                    queryData["limit"] = 10
                    queryData["page"]  = 1
                    Log.i("dasdas", "getMyCard: $data")
                    viewModel.getCards(queryData,data, Constant.CARD_LIST)
                }
                R.id.selectFilter ->{
                    binding.filterView.visibility = View.VISIBLE
                }

            }
        }
    }

    private fun initObserver() {


        viewModel.obrCommon.observe(viewLifecycleOwner , Observer {
            when (it?.status) {
                Status.LOADING -> {
                    showLoading("Loading")
                }

                Status.SUCCESS -> {
                    hideLoading()
                    when (it.message) {
                        "getCards" -> {
                            val myDataModel : CardDataListApiResponse ? = ImageBindingUtil.parseJson(it.data.toString())
                            if (myDataModel != null){
                                if (myDataModel.data!!.isNotEmpty()) {
                                    binding.rvCard.visibility = View.VISIBLE
                                    binding.tvNoData.visibility = View.GONE

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
                                            chartAdapter.list = myDataModel.data
                                        } else {
                                            chartAdapter.addToList(myDataModel.data)

                                        }
                                    }catch (e: Exception) {
                                        e.printStackTrace()
                                    }
//                                    chartAdapter.list = myDataModel.data
                                } else {
                                    binding.rvCard.visibility = View.GONE
                                    binding.tvNoData.visibility = View.VISIBLE
                                }

                            }
                        }
                        "getCategory" -> {
                            val myDataModel: GetCategoryApiResponse? =
                                ImageBindingUtil.parseJson(it.data.toString())
                            if (myDataModel != null) {
                                if (myDataModel.data != null) {
                                    // Create a new data item to add
                                    val newItem = GetCategoryApiResponse.GetCategoryApiResponseData(
                                        __v = 0,
                                        _id = "",
                                        adminId = "newAdminId",
                                        category = "New Category",
                                        category_image = "new_image_url",
                                        category_name = "All",
                                        category_short_name = "All",
                                        createdAt = "2025-01-07T00:00:00Z",
                                        social_media = "New Social Media",
                                        status = 1,
                                        updatedAt = "2025-01-07T00:00:00Z"
                                    )

                                    // Add the new item to the start of the list
                                    val updatedList = mutableListOf(newItem)
                                    updatedList.addAll(myDataModel.data!!.filterNotNull())

                                    // Update the adapter's list
                                    filterAdapter.list = updatedList
                                }
                            }
                        }

                    }
                }
                Status.ERROR ->{
                    hideLoading()
                    showErrorToast(it.message.toString())
                }
                else ->{

                }
            }

        }
        )
    }

//    private fun initList() {
//        val data = HashMap<String, Any>()
//        data["location"] = mapOf(
//            "type" to "Point", "coordinates" to listOf(
//                WelcomeActivity.long.toDouble(), WelcomeActivity.lat.toDouble()
//            )
//        )
//        if (filterData != null){
//            data["id"] = filterData.toString()
//        }
//
//
//        val queryData = HashMap<String,Any>()
//        queryData["limit"] =10
//        queryData["page"]  = 1
//        Log.i("dasdas", "getMyCard: $data")
//
//        viewModel.getCards(queryData,data, Constant.CARD_LIST)
//
//    }


    @SuppressLint("MissingPermission")
    private fun initList() {
        val data = HashMap<String, Any>()
        val queryData: HashMap<String, Any> = hashMapOf(
            "limit" to 10,
            "page" to 1
        )

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    val lat = location?.latitude ?: 0.0
                    val lng = location?.longitude ?: 0.0

                    data["location"] = mapOf(
                        "type" to "Point",
                        "coordinates" to listOf(lng, lat)
                    )

                    if (filterData != null) {
                        data["id"] = filterData.toString()
                    }

                    Log.i("initList", "Using current location: $lat,$lng")
                    viewModel.getCards(queryData, data, Constant.CARD_LIST)
                }
                .addOnFailureListener { e ->
                    Log.e("initList", "Failed to get location: ${e.message}")

                    data["location"] = mapOf(
                        "type" to "Point",
                        "coordinates" to listOf(0.0, 0.0)
                    )
                    if (filterData != null) {
                        data["id"] = filterData.toString()
                    }

                    Log.i("initList", "Fallback to 0,0 location")
               //     viewModel.getCards(queryData, data, Constant.CARD_LIST)
                }

        } catch (e: Exception) {
            Log.e("initList", "Unexpected error: ${e.message}")

            data["location"] = mapOf(
                "type" to "Point",
                "coordinates" to listOf(0.0, 0.0)
            )
            if (filterData != null) {
                data["id"] = filterData.toString()
            }

         //   viewModel.getCards(queryData, data, Constant.CARD_LIST)
        }
    }





    private fun initAdapter() {
        chartAdapter = SimpleRecyclerViewAdapter(
            R.layout.category_activity, BR.bean
        ) { v, m, pos ->
            when (v.id) {
                R.id.listlayout, R.id.tvView -> {
                    val bundle = Bundle()
                    bundle.putParcelable("card", m)
                    val intent = Intent(context, CardPopup::class.java)
                    intent.putExtra("cardData",m)
                    startActivity(intent)
                }

                R.id.imageview -> {
                    CardImageActivity.imageLoad = m.post_image
                    val intent = Intent(context, CardImageActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        binding.rvCard.adapter = chartAdapter

        val lm = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        pagination = VerticalPagination(lm, 2)
        pagination?.setListener(object : VerticalPagination.VerticalScrollListener {
            override fun onLoadMore() {
                Log.d("Fdsfdsf", "first: ")

                if (isLoading == 1){
                    Log.d("Fdsfdsf", "second: ")
                    page++
                    val data = HashMap<String, Any>()
                    data["location"] = mapOf(
                        "type" to "Point", "coordinates" to listOf(
                            WelcomeActivity.long.toDouble(), WelcomeActivity.lat.toDouble()
                        )
                    )
                    if (filterData != null){
                        data["id"] = filterData.toString()
                    }
                    val queryData = HashMap<String,Any>()
                    queryData["limit"] = 10
                    queryData["page"]  = page
                    Log.i("dasdas", "getMyCard: $data")
                    viewModel.getCards(queryData,data, Constant.CARD_LIST)
                    isLoading = 0
                }

            }
        })
        pagination?.let {
            binding.rvCard.addOnScrollListener(it)
        }




        filterAdapter = SimpleRecyclerViewAdapter(R.layout.item_layout_filter, BR.bean){v,m, pos ->

            when(v.id){
                R.id.consFilter ->{
                    val data = HashMap<String, Any>()
                    filterData = m._id
                    data["id"] = m._id.toString()
                    data["location"] = mapOf(
                        "type" to "Point", "coordinates" to listOf(
                            WelcomeActivity.long.toDouble(), WelcomeActivity.lat.toDouble()
                        )
                    )
                    val queryData = HashMap<String,Any>()
                    page  = 1
                    queryData["limit"] = 10
                    queryData["page"]  = 1
                    Log.i("dasdas", "getMyCard: $data")
                    viewModel.getCards(queryData ,data, Constant.CARD_LIST)
                    binding.filterView.visibility = View.GONE
                    binding.selectFilter.setText(m.category_short_name)
                }
            }

        }
        binding.filterView.adapter = filterAdapter

    }

}


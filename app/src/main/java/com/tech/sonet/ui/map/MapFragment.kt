package com.tech.sonet.ui.map

import android.Manifest
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tech.sonet.BR
import com.tech.sonet.R
import com.tech.sonet.data.model.CardDataListApiResponse
import com.tech.sonet.data.model.GetCategoryApiResponse
import com.tech.sonet.data.model.response.CardData
import com.tech.sonet.databinding.FragmentMapBinding
import com.tech.sonet.databinding.ItemLayoutFilterBinding
import com.tech.sonet.ui.base.BaseFragment
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.ui.base.SimpleRecyclerViewAdapter
import com.tech.sonet.ui.chart.CardPopup
import com.tech.sonet.ui.welcome.WelcomeActivity
import com.tech.sonet.utils.Constant
import com.tech.sonet.utils.ImageBindingUtil
import com.tech.sonet.utils.Status
import com.tech.sonet.utils.showErrorToast
import com.tech.sonet.utils.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {


    private var list: List<CardDataListApiResponse.CardDataListApiResponseData>? = null

    private val viewModel: MapFragmentVM by viewModels()
    private lateinit var mGoogleMap: GoogleMap
    private val LOCATION_PERMISSION = 45
    private var latitude : Double = 0.0
    private var longitude : Double = 0.0
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    var lastZoomLevel = 0f

    private lateinit var filterAdapter : SimpleRecyclerViewAdapter<GetCategoryApiResponse.GetCategoryApiResponseData, ItemLayoutFilterBinding>


    override fun getLayoutResource(): Int {
        return R.layout.fragment_map
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView(view: View) {
        initOnClick()
        initObserver()
        getCard()
        getFilter()
        val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)

        initAdapter()
    }

    private fun initAdapter() {
        filterAdapter = SimpleRecyclerViewAdapter(R.layout.item_layout_filter, BR.bean){ v, m, pos ->

            when(v.id){
                R.id.consFilter ->{
                    val data = HashMap<String, Any>()
                    data["id"] = m._id.toString()
                    data["location"] = mapOf(
                        "type" to "Point", "coordinates" to listOf(
                            WelcomeActivity.long.toDouble(), WelcomeActivity.lat.toDouble()
                        )
                    )
                    Log.i("dasdas", "getMyCard: $data")
                    viewModel.getCards(data, Constant.CARD_LIST)
                    binding.filterView.visibility = View.GONE
                    binding.selectFilter.setText(m.category_short_name)
                }
            }

        }
        binding.filterView.adapter = filterAdapter

    }

    private fun getFilter() {
        viewModel.getCategory(Constant.GET_CATEGORY)
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.update -> {
                    val data = HashMap<String, Any>()
//                    data["location"] = mapOf(
//                        "type" to "Point", "coordinates" to listOf(
//                            WelcomeActivity.long.toDouble(), WelcomeActivity.lat.toDouble()
//                        )
//                    )
                    data["location"] = mapOf(
                        "type" to "Point",
                        "coordinates" to listOf(
                            longitude , latitude
                        )
                    )
                    Log.i("dasdas", "getMyCard: $data")
                    viewModel.getCards(data, Constant.CARD_LIST)
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
                            val myDataModel: CardDataListApiResponse? =
                                ImageBindingUtil.parseJson(it.data.toString())
                            if (myDataModel != null) {
                                if (myDataModel.data != null) {
                                    mGoogleMap.clear()
                                    try {
                                        list = myDataModel.data as List<CardDataListApiResponse.CardDataListApiResponseData>?
                                        for (i in list!!.indices) {
                                            val location = list!![i].location
                                            val coordinates = location?.coordinates

                                            if (coordinates != null && coordinates.size >= 2) {
                                                val latitude = coordinates[1] ?: 0.0
                                                val longitude = coordinates[0] ?: 0.0

                                                loadBitmapFromUrl(
                                                    requireContext(), list!![i].categoryDetail?.category_image!!
                                                ) { originalBitmap ->
                                                    val markerWidth = 70
                                                    val markerHeight = 70

                                                    val scaledBitmap = Bitmap.createScaledBitmap(
                                                        originalBitmap, markerWidth, markerHeight, false
                                                    )
                                                    val scaledBitmapDescriptor =
                                                        BitmapDescriptorFactory.fromBitmap(scaledBitmap)

                                                    val markerOptions = MarkerOptions().position(LatLng(latitude, longitude))
                                                        .icon(scaledBitmapDescriptor)

                                                    // Show title and snippet only if in_radius_range == 1
                                                    if (list!![i].notAccessible == false) {
                                                        markerOptions.title(list!![i].title)
                                                            .snippet(list!![i].title)
                                                    }

                                                    mGoogleMap.addMarker(markerOptions)?.tag = list!![i]._id
                                                }
                                            }
                                        }
                                    }catch (e : Exception){
                                        e.printStackTrace()
                                    }

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

                Status.ERROR -> {
                    hideLoading()
                    showErrorToast(it.message.toString())
                }

                else -> {

                }
            }

        })

//        viewModel.obsUpdatemap.observe(viewLifecycleOwner) {
//            when (it?.status) {
//                Status.LOADING -> {
//                    showLoading("Loading")
//                }
//                Status.SUCCESS -> {
//                    hideLoading()
//                }
//                Status.ERROR -> {
//                    hideLoading()
//                    showToast(it.message.toString())
//                }
//                else -> {}
//            }
//        }

//        viewModel.obsmapUser.observe(viewLifecycleOwner) {
//            when (it?.status) {
//                Status.LOADING -> {
//                    showLoading("Loading")
//                }
//                Status.SUCCESS -> {
//                    hideLoading()
//                    list = it.data?.data
//                    if (!list.isNullOrEmpty()) {
//                        for (i in list!!.indices) {
//                            val lati: Double = list!![i].latitude!!.toDouble()
//                            val longLat: Double = list!![i].longitude!!.toDouble()
//
//                            loadBitmapFromUrl(
//                                requireContext(), list!![i].category_image!!
//                            ) { originalBitmap ->
//                                val markerWidth = 70
//                                val markerHeight = 70
//
//                                val scaledBitmap = Bitmap.createScaledBitmap(
//                                    originalBitmap, markerWidth, markerHeight, false
//                                )
//                                val scaledBitmapDescriptor =
//                                    BitmapDescriptorFactory.fromBitmap(scaledBitmap)
//
//                                mGoogleMap.addMarker(
//                                    MarkerOptions().position(LatLng(lati, longLat))
//                                        .title(list!![i].title).snippet(list!![i].title)
//                                        .icon(scaledBitmapDescriptor)
//                                )?.tag = list!![i].id
//                            }
//                        }
//                    }
//                }
//                Status.ERROR -> {
//                    hideLoading()
//                    showToast(it.message.toString())
//                }
//                else -> {}
//            }
//
//        }

    }

    private fun getCard() {
        val data = HashMap<String, Any>()
        data["location"] = mapOf(
            "type" to "Point", "coordinates" to listOf(
                WelcomeActivity.long.toDouble(), WelcomeActivity.lat.toDouble()
            )
        )
        Log.i("dasdas", "getMyCard: $data")
        viewModel.getCards(data, Constant.CARD_LIST)

    }

    private fun initCurrentLocation() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult
                for (location in locationResult.locations) {
                    updateLocation(location)
                }
            }
        }
        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it, Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && context?.let {
                ActivityCompat.checkSelfPermission(
                    it, Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationClient.requestLocationUpdates(
            com.google.android.gms.location.LocationRequest(), locationCallback, null
        )
    }

    private fun updateLocation(location: Location?) {
        location?.let { loc ->
            val current = LatLng(loc.latitude, loc.longitude)
            val sh = context?.getSharedPreferences("MySharedPreference", MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sh!!.edit()
            editor.putString("lat", loc.latitude.toString())
            editor.putString("long", loc.longitude.toString())
            editor.apply()
            //    mGoogleMap.addMarker(MarkerOptions().position(current).title("Current Location"))
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(current))
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 22f))

        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        //   Toast.makeText(this,"Button click fetch location", Toast.LENGTH_LONG).show()
        return false
    }

    override fun onMyLocationClick(p0: Location) {
      //  Toast.makeText(requireContext(), "Button click fetch location", Toast.LENGTH_LONG).show()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        mGoogleMap.setOnMyLocationButtonClickListener(this)
        mGoogleMap.setOnMyLocationClickListener(this)
        mGoogleMap.isMyLocationEnabled = true

        if (context?.let {
                ContextCompat.checkSelfPermission(
                    it, Manifest.permission.ACCESS_FINE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient =
                context?.let { LocationServices.getFusedLocationProviderClient(it) }!!
            initCurrentLocation()
        } else {
            fusedLocationClient =
                context?.let { LocationServices.getFusedLocationProviderClient(it) }!!
        }

        mGoogleMap.setOnMarkerClickListener(OnMarkerClickListener { marker ->
            val markerName = marker.title
            val markerTag = marker.tag.toString()

            if (list?.size!! > 0) {
                for (i in list?.indices!!) {
                    if (markerTag.equals(list!![i]._id.toString())) {
                        if (list!![i].notAccessible == true) {
//                            Toast.makeText(context, "Out of range", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            val bundle: Bundle = Bundle()
                            bundle.putParcelable("mapDAta", list!![i])
                            val intent = Intent(context, CardPopup::class.java)
                            intent.putExtras(bundle)
                            startActivity(intent, bundle)
                        }

                    }
                }
            }
          //  Toast.makeText(requireContext(), "Clicked location is $markerName", Toast.LENGTH_SHORT)
            //    .show()
            false
        })


        mGoogleMap!!.setOnCameraIdleListener {
            val userLatLng = mGoogleMap!!.cameraPosition.target // Get the new center of the map
            latitude = userLatLng.latitude
            longitude = userLatLng!!.longitude



        }


    }

    private fun loadBitmapFromUrl(
        context: Context, imageUrl: String, callback: (Bitmap) -> Unit
    ) {
        Glide.with(context).asBitmap().load(imageUrl).into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                callback(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }
        })
    }

}





package com.tech.sonet.ui.home_fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.tech.sonet.BR
import com.tech.sonet.R
import com.tech.sonet.data.model.AcceptedStatusApiResponse
import com.tech.sonet.data.model.GetMyCardApiResponse
import com.tech.sonet.data.model.LikeReceiveApiResponse
import com.tech.sonet.data.model.LikeSentApiResponse
import com.tech.sonet.data.model.response.CardsData
import com.tech.sonet.databinding.FragmentHomeBinding
import com.tech.sonet.databinding.HomeImageBinding
import com.tech.sonet.ui.request.RequestActivity
import com.tech.sonet.ui.HomeActivity
import com.tech.sonet.ui.addcard.MypCardActivity
import com.tech.sonet.ui.base.BaseFragment
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.ui.base.SimpleRecyclerViewAdapter
import com.tech.sonet.ui.homepage.HomePageActivity
import com.tech.sonet.ui.like_sent.LikeSentActivity
import com.tech.sonet.ui.login.LoginActivity
import com.tech.sonet.ui.map.MapFragment
import com.tech.sonet.ui.request.AcceptedActivity
import com.tech.sonet.ui.signupemail.SignupEmailActivity
import com.tech.sonet.ui.welcome.WelcomeActivity
import com.tech.sonet.utils.Constant
import com.tech.sonet.utils.ImageBindingUtil
import com.tech.sonet.utils.Status
import com.tech.sonet.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel: HomeFragmentVM by viewModels()
    private lateinit var adapter: SimpleRecyclerViewAdapter<GetMyCardApiResponse.GetMyCardApiResponseData, HomeImageBinding>
    var home: HomeActivity? = null
    private var backPressedTime: Long = 0

    override fun getLayoutResource(): Int {
        return R.layout.fragment_home
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView(view: View) {
        getMyCard()
        getLists()
        initOnClick()

        initAdapter()
        initObserver()

        binding.bean = sharedPrefManager.getCurrentUser()
        home = activity as HomeActivity
    }

    private fun getLists() {
        // for like sent
        val data = HashMap<String,Any>()
        data["type"] = "sender"
        viewModel.likeSentData(data,Constant.LIKE_SENT)

        // for like received
        val receiveData = HashMap<String,Any>()
        receiveData["type"] = "receiver"
        viewModel.getRequestList(receiveData,Constant.LIKE_SENT)

        // for matched list
        viewModel.acceptedStatus(Constant.ACCEPTED_STATUS)


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                requireActivity().finishAffinity()
            } else {
                Toast.makeText(requireContext(), "Press back again to leave the app.", Toast.LENGTH_LONG).show()
            }
            backPressedTime = System.currentTimeMillis()
        }
    }



    override fun onStart() {
        super.onStart()
        getLists()
    }



    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.mapCard, R.id.map-> {
                    home?.pushFragment("HomeFragment", MapFragment())
                }
                R.id.clReq -> {
                    val intent = Intent(context, RequestActivity::class.java)
                    startActivity(intent)
                }
                R.id.clSent -> {
                    val intent = Intent(context, LikeSentActivity::class.java)
                    startActivity(intent)
                }
                R.id.clMatched ->{
                    val intent = Intent(context, AcceptedActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun initObserver() {

        viewModel.obrCommon.observe(viewLifecycleOwner, Observer {
            when(it?.status){
                Status.LOADING ->{
                    showLoading("Loading")
                }
                Status.SUCCESS ->{
                    hideLoading()
                    when(it.message){
                        "getCard" ->{
                            val myDataModel : GetMyCardApiResponse ? = ImageBindingUtil.parseJson(it.data.toString())
                            if (myDataModel != null){
                                if (myDataModel.data != null){
                                    Log.i("online", "initObserver: ${myDataModel.data!![0]?.isOnline}")
                                    adapter.list = myDataModel.data
                                }
                            }
                        }
                        "getLikeSent" ->{
                            val myDataModel : LikeSentApiResponse ? = ImageBindingUtil.parseJson(it.data.toString())
                            if (myDataModel != null){
                                if (myDataModel.data != null){
                                    if (myDataModel.data?.size.toString()!="0"){
                                        binding.setReq.text = myDataModel.data?.size.toString()
                                    }else{
                                        binding.setReq.text = "0"
                                    }
                                }
                            }
                        }
                        "getRequestList" ->{
                            val myDataModel : LikeReceiveApiResponse ? = ImageBindingUtil.parseJson(it.data.toString())
                            if (myDataModel != null){
                                if (myDataModel.data != null){
                                    if (myDataModel.data?.size.toString()!="0"){
                                        binding.tvReq.text = myDataModel.data?.size.toString()
                                    }else{
                                        binding.tvReq.text = "0"
                                    }
                                }
                            }
                        }
                        "acceptedStatus" ->{
                            val myDataModel : AcceptedStatusApiResponse ? = ImageBindingUtil.parseJson(it.data.toString())
                            if (myDataModel != null){
                                if (myDataModel.data != null){
                                    if (myDataModel.data?.size.toString()!="0"){
                                        binding.tvMatched.text = myDataModel.data?.size.toString()
                                    }else{
                                        binding.tvMatched.text = "0"
                                    }
                                }
                            }
                        }
                    }
                }
                Status.ERROR ->{
                    hideLoading()
                    showLoading(it.message.toString())
//                    if (it.message == "Unauthorized" || it.data?.toString()
//                            ?.contains("statusCode\":401") == true
//                    ) {
//                        // Send user to login activity
//                        startActivity(Intent(requireContext(), HomePageActivity::class.java))
//                        requireActivity().finish()
//                    } else {
//                        showToast(it.message.toString())
//                    }
                }
                else ->{

                }
            }
        })

//
//        viewModel.observeRequest.observe(viewLifecycleOwner) {
//            when (it?.status) {
//                Status.LOADING -> {
//                    showLoading("Loading")
//                }
//
//                Status.SUCCESS -> {
//                    hideLoading()
//                    if (it.data?.data?.size.toString()!="0"){
//                        binding.tvReq.text = "" + it.data?.data?.size.toString()
//                    }else{
//                        binding.tvReq.text = "0"
//                    }
//                }
//
//                Status.ERROR -> {
//                    hideLoading()
//                    sharedPrefManager.clear()
//                    val intent = Intent(context, LoginActivity::class.java)
//                    startActivity(intent)
//                    showToast(it.message.toString())
//                }
//
//                else -> {}
//            }
//        }
    }

    override fun onResume() {
        super.onResume()
        getMyCard()
        getLists()
    }
    private fun getMyCard() {


        val data = HashMap<String,Any>()
        data["latitude"] = WelcomeActivity.lat
        data["longitude"] = WelcomeActivity.long
        viewModel.myCard(data,Constant.MY_CARDS)
    }

    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.home_image, BR.bean
        ) { v, m, pos ->
            when (v.id) {
                R.id.myCard -> {
                    val bundle = Bundle()
                    bundle.putParcelable("mycard", m)
                    val intent = Intent(context, MypCardActivity::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
                R.id.unsplash -> {
                    MypCardImageActivity.imageLoad = m.post_image
                    val intent = Intent(context, MypCardImageActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        binding.rvImage.adapter = adapter
    }


}


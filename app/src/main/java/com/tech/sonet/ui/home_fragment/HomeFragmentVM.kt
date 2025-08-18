package com.tech.sonet.ui.home_fragment

import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.tech.sonet.data.api.ApiHelper
import com.tech.sonet.data.model.response.MyCardsResponse
import com.tech.sonet.data.model.response.RequestResponse
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.utils.NetworkHelper
import com.tech.sonet.utils.Resource
import com.tech.sonet.utils.event.NetworkErrorHandler
import com.tech.sonet.utils.event.SingleRequestEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runInterruptible
import javax.inject.Inject

@HiltViewModel
class HomeFragmentVM @Inject constructor(
    private val apiHelper: ApiHelper,
    private val networkErrorHandler: NetworkErrorHandler,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {


    val obrCommon = SingleRequestEvent<JsonObject>()


    fun myCard(data: HashMap<String, Any>, url : String) {
        if (networkHelper.isNetworkConnected()) {
            obrCommon.value = Resource.loading(null)
            try {
                viewModelScope.launch {
                    apiHelper.apiGetWithQueryAuth(data, url).let {
                        if (it.isSuccessful) {
                            obrCommon.value =   Resource.success(it.body(),"getCard")

                        } else {
                            obrCommon.value = Resource.error(
                              handleErrorResponse(it.errorBody(),it.code()),null
                            )
                        }
                    }

                }
            } catch (e: Exception) {
                obrCommon.value = e.localizedMessage?.let {
                    Resource.error(it, null)
                }
            }
        }
    }


    val observeRequest = SingleRequestEvent<RequestResponse>()
    fun request(map: HashMap<String, String>, id: String, key: String) {
        if (networkHelper.isNetworkConnected()) {
            observeRequest.value = Resource.loading(null)
            try {
                viewModelScope.launch {
                    apiHelper.request(map, id, key).let {
                        if (it.isSuccessful) {
                            observeRequest.value = Resource.success(it.body(), it.body()?.message)
                        } else {
                            observeRequest.value = Resource.error(
                              handleErrorResponse(it.errorBody(),it.code()),
                                null
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                observeRequest.value = e.localizedMessage?.let {
                    Resource.error(
                        it, null
                    )
                }
            }
        }
    }

    fun likeSentData(data : HashMap<String,Any>, url : String){
        if (networkHelper.isNetworkConnected()) {
            obrCommon.value = Resource.loading(null)
            try {
                viewModelScope.launch {
                    apiHelper.apiGetWithQueryAuth(data,url).let {
                        if (it.isSuccessful) {
                            obrCommon.value =   Resource.success(it.body(),"getLikeSent")

                        } else {
                            obrCommon.value = Resource.error(handleErrorResponse(it.errorBody(),it.code()),
                                null
                            )
                        }
                    }

                }
            } catch (e: Exception) {
                obrCommon.value = e.localizedMessage?.let {
                    Resource.error(it, null)
                }
            }
        }
    }


    fun getRequestList(data : HashMap<String,Any>, url: String){
        if (networkHelper.isNetworkConnected()) {
            obrCommon.value = Resource.loading(null)
            try {
                viewModelScope.launch {
                    apiHelper.apiGetWithQueryAuth(data,url).let {
                        if (it.isSuccessful) {
                            obrCommon.value =   Resource.success(it.body(),"getRequestList")

                        } else {
                            obrCommon.value = Resource.error(handleErrorResponse(it.errorBody(),it.code()),
                                null
                            )
                        }
                    }

                }
            } catch (e: Exception) {
                obrCommon.value = e.localizedMessage?.let {
                    Resource.error(it, null)
                }
            }
        }
    }

    fun acceptedStatus( url : String){
        if (networkHelper.isNetworkConnected()) {
            obrCommon.value = Resource.loading(null)
            try {
                viewModelScope.launch {
                    apiHelper.apiGetWithAuth( url).let {
                        if (it.isSuccessful) {
                            obrCommon.value = Resource.success(it.body(), "acceptedStatus")
                        } else {
                            obrCommon.value = Resource.error(
                                handleErrorResponse(it.errorBody(),it.code()),
                                null
                            )
                        }
                    }
                }
            }catch (e:Exception){
                obrCommon.value = e.localizedMessage?.let {
                    Resource.error(it,null)
                }
            }
        }
    }

}
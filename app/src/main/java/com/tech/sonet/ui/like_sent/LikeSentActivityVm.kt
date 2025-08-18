package com.tech.sonet.ui.like_sent

import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.tech.sonet.data.api.ApiHelper
import com.tech.sonet.data.model.response.RequestResponse
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.utils.NetworkHelper
import com.tech.sonet.utils.Resource
import com.tech.sonet.utils.event.NetworkErrorHandler
import com.tech.sonet.utils.event.SingleRequestEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LikeSentActivityVm @Inject constructor(private val apiHelper: ApiHelper,
                                             private val networkErrorHandler: NetworkErrorHandler,
                                             private val networkHelper: NetworkHelper
) : BaseViewModel() {


   val obrCommon = SingleRequestEvent<JsonObject>()

    fun likeSentData(data : HashMap<String,Any>, url : String){
        if (networkHelper.isNetworkConnected()) {
            obrCommon.value = Resource.loading(null)
            try {
                viewModelScope.launch {
                    apiHelper.apiGetWithQueryAuth(data,url).let {
                        if (it.isSuccessful) {
                            obrCommon.value =   Resource.success(it.body(),"getCard")

                        } else {
                            obrCommon.value = Resource.error(
                               handleErrorResponse(it.errorBody(),it.code()),
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

    fun deleteRequest(url  : String , data: HashMap<String, Any>){
        if (networkHelper.isNetworkConnected()) {
            obrCommon.value = Resource.loading(null)
            try {
                viewModelScope.launch {
                    apiHelper.deleteRequest(url,data).let {
                        if (it.isSuccessful) {
                            obrCommon.value =   Resource.success(it.body(),"deleteRequest")

                        } else {
                            obrCommon.value = Resource.error(
                                 handleErrorResponse(it.errorBody(),it.code()),
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

    fun deleteAllRequest(data: HashMap<String, Any>, url: String){
        if (networkHelper.isNetworkConnected()) {
            obrCommon.value = Resource.loading(null)
            try {
                viewModelScope.launch {
                    apiHelper.deleteAll(url,data).let {
                        if (it.isSuccessful) {
                            obrCommon.value =   Resource.success(it.body(),"deleteAllRequest")

                        } else {
                            obrCommon.value = Resource.error(
                              handleErrorResponse(it.errorBody(),it.code()),
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
}
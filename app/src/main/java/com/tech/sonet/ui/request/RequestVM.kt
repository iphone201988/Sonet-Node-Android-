package com.tech.sonet.ui.request

import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.tech.sonet.data.api.ApiHelper
import com.tech.sonet.data.model.response.NotificationResponse
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
class RequestVM @Inject constructor(
    private val apiHelper: ApiHelper,
    private val networkErrorHandler: NetworkErrorHandler,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val obrCommon = SingleRequestEvent<JsonObject>()
    val observeRequest = SingleRequestEvent<RequestResponse>()


    fun requestStatus(data : HashMap<String, Any> , url :  String){
        if (networkHelper.isNetworkConnected()) {
            obrCommon.value = Resource.loading(null)
            try {
                viewModelScope.launch {
                    apiHelper.apiForPutRawBody(data, url).let {
                        if (it.isSuccessful) {
                            obrCommon.value = Resource.success(it.body(), "requestStatus")
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
                    Resource.error(
                        it, null
                    )
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
    fun deleteRequest(url  : String, data: HashMap<String, Any>){
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

//    fun request(map: HashMap<String, String>, id: String, key: String) {
//        if (networkHelper.isNetworkConnected()) {
//            observeRequest.value = Resource.loading(null)
//            try {
//                viewModelScope.launch {
//                    apiHelper.request(map, id, key).let {
//                        if (it.isSuccessful) {
//                            observeRequest.value = Resource.success(it.body(), it.body()?.message)
//                        } else {
//                            observeRequest.value = Resource.error(
//                                networkErrorHandler.getErrorMessage(it.errorBody()),
//                                null
//                            )
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//                observeRequest.value = e.localizedMessage?.let {
//                    Resource.error(
//                        it, null
//                    )
//                }
//            }
//        }
//    }
//    val obsDelete = SingleRequestEvent<RequestResponse>()

//    fun delete(map: HashMap<String, String>, id: String, key: String) {
//        if (networkHelper.isNetworkConnected()) {
//            obsDelete.value = Resource.loading(null)
//            try {
//                viewModelScope.launch {
//                    apiHelper.request(map, id, key).let {
//                        if (it.isSuccessful) {
//                            obsDelete.value = Resource.success(it.body(), it.body()?.message)
//                        } else {
//                            obsDelete.value = Resource.error(
//                                networkErrorHandler.getErrorMessage(it.errorBody()),
//                                null
//                            )
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//                obsDelete.value = e.localizedMessage?.let {
//                    Resource.error(
//                        it, null
//                    )
//                }
//            }
//        }
//    }

//    val obsAccept = SingleRequestEvent<RequestResponse>()
//    fun accept(map: HashMap<String, String>, id: String, key: String) {
//        if (networkHelper.isNetworkConnected()) {
//            obsAccept.value = Resource.loading(null)
//            try {
//                viewModelScope.launch {
//                    apiHelper.request(map, id, key).let {
//                        if (it.isSuccessful) {
//                            obsAccept.value = Resource.success(it.body(), it.body()?.message)
//                        } else {
//                            obsAccept.value = Resource.error(
//                                networkErrorHandler.getErrorMessage(it.errorBody()),
//                                null
//                            )
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//                obsAccept.value = e.localizedMessage?.let {
//                    Resource.error(it, null)
//                }
//            }
//        }
//    }


//    val observeClearRequest = SingleRequestEvent<NotificationResponse>()
//    fun clearRequest(id: String, key: String) {
//        if (networkHelper.isNetworkConnected()) {
//            observeClearRequest.value = Resource.loading(null)
//            try {
//                viewModelScope.launch {
//                    apiHelper.clearRequest(id, key).let {
//                        if (it.isSuccessful) {
//                            observeClearRequest.value = Resource.success(null, it.body()?.message)
//                        } else {
//                            observeClearRequest.value =
//                                Resource.error(
//                                    networkErrorHandler.getErrorMessage(it.errorBody()),
//                                    null
//                                )
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//                observeClearRequest.value = e.localizedMessage.let {
//                    Resource.error(it, null)
//                }
//            }
//        }
//    }
}

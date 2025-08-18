package com.tech.sonet.ui.chart

import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.tech.sonet.data.api.ApiHelper
import com.tech.sonet.data.model.response.RequestSendResponse
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.utils.NetworkHelper
import com.tech.sonet.utils.Resource
import com.tech.sonet.utils.event.NetworkErrorHandler
import com.tech.sonet.utils.event.SingleRequestEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class CardPopupVM @Inject constructor(
    private val apiHelper: ApiHelper,
    private val networkErrorHandler: NetworkErrorHandler,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val obrCommon = SingleRequestEvent<JsonObject>()
    fun requestSent(data : HashMap<String,Any>, url : String){
        if (networkHelper.isNetworkConnected()) {
            obrCommon.value = Resource.loading(null)
            try {
                viewModelScope.launch {
                    apiHelper.apiRawBodyWithAuth(data, url ).let {
                        if (it.isSuccessful) {
                            obrCommon.value = Resource.success(it.body(), "sentReq")
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
//    val obsSendRequest = SingleRequestEvent<RequestSendResponse>()
//    fun requestsend(map: HashMap<String, String>, id: String, key: String) {
//        if (networkHelper.isNetworkConnected()) {
//            obsSendRequest.value = Resource.loading(null)
//            try {
//                viewModelScope.launch {
//                    apiHelper.sendRequest(map, id, key).let {
//                        if (it.isSuccessful) {
//                            obsSendRequest.value = Resource.success(it.body(), it.body()?.message)
//                        } else {
//                            obsSendRequest.value = Resource.error(
//                                networkErrorHandler.getErrorMessage(it.errorBody()),
//                                null
//                            )
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//                obsSendRequest.value = e.localizedMessage?.let {
//                    Resource.error(it, null)
//                }
//            }
//        }
//    }
}

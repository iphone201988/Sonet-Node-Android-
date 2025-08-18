package com.tech.sonet.ui.chart

import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.tech.sonet.data.api.ApiHelper
import com.tech.sonet.data.model.response.CardListResponse
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.utils.NetworkHelper
import com.tech.sonet.utils.Resource
import com.tech.sonet.utils.event.NetworkErrorHandler
import com.tech.sonet.utils.event.SingleRequestEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChartFragmentVM @Inject constructor(
    private val apiHelper: ApiHelper,
    private val networkErrorHandler: NetworkErrorHandler,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val obrCommon = SingleRequestEvent<JsonObject>()

    fun getCards(queryData : HashMap<String,Any> , data: HashMap<String,Any>, url: String){
        if (networkHelper.isNetworkConnected()) {
            obrCommon.value = Resource.loading(null)
            try {
                viewModelScope.launch {
                    apiHelper.getCardList(queryData ,data, url).let {
                        if (it.isSuccessful) {
                            obrCommon.value = Resource.success(it.body() , "getCards")
                        } else {
                            obrCommon.value = Resource.error(
                                handleErrorResponse(it.errorBody(),it.code()),
                                null
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                obrCommon.value = e.message?.let {
                    Resource.error(it, null)
                }
            }
        }
    }

    fun getCategory(url: String) {
        if (networkHelper.isNetworkConnected()) {
            obrCommon.value = Resource.loading(null)
            try {
                viewModelScope.launch {
                    apiHelper.apiGetWithAuth(url).let {
                        if (it.isSuccessful) {
                            obrCommon.value = Resource.success(it.body(), "getCategory")
                        } else {
                            obrCommon.value = Resource.error(
                                handleErrorResponse(it.errorBody(),it.code()), null
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                obrCommon.value = e.localizedMessage.let {
                    Resource.error(it, null)
                }
            }
        }
    }

}
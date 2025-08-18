package com.tech.sonet.ui.map

import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.tech.sonet.data.api.ApiHelper
import com.tech.sonet.data.model.response.CardListResponse
import com.tech.sonet.data.model.response.MyCardsResponse
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.utils.NetworkHelper
import com.tech.sonet.utils.Resource
import com.tech.sonet.utils.event.NetworkErrorHandler
import com.tech.sonet.utils.event.SingleRequestEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapFragmentVM @Inject constructor(
    private val apiHelper: ApiHelper,
    private val networkErrorHandler: NetworkErrorHandler,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val obrCommon = SingleRequestEvent<JsonObject>()
    val obsUpdatemap = SingleRequestEvent<CardListResponse>()


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
    fun getCards(data: HashMap<String,Any>, url: String){
        if (networkHelper.isNetworkConnected()) {
            obrCommon.value = Resource.loading(null)
            try {
                viewModelScope.launch {
                    apiHelper.apiRawBodyWithAuth(data, url).let {
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
    fun updatemap(map: HashMap<String, String>, id: String, key: String) {
        if (networkHelper.isNetworkConnected()) {
            obsUpdatemap.value = Resource.loading(null)
            try {
                viewModelScope.launch {
                    apiHelper.cardList(map, id, key).let {
                        if (it.isSuccessful) {
                            obsUpdatemap.value = Resource.success(it.body(), it.body()?.message)
                        } else {
                            obsUpdatemap.value = Resource.error(
                                handleErrorResponse(it.errorBody(),it.code()),
                                null
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                obsUpdatemap.value = e.localizedMessage?.let {
                    Resource.error(it, null)
                }
            }
        }
    }

//    val obsmapUser = SingleRequestEvent<CardListResponse>()
//    fun mapuser(map: HashMap<String, String>, id: String, key: String) {
//        if (networkHelper.isNetworkConnected()) {
//            obsmapUser.value = Resource.loading(null)
//            try {
//                viewModelScope.launch {
//                    apiHelper.cardList(map, id, key).let {
//                        if (it.isSuccessful) {
//                            obsmapUser.value = Resource.success(it.body(), it.body()?.message)
//                        } else {
//                            obsmapUser.value = Resource.error(
//                                networkErrorHandler.getErrorMessage(
//                                    it.errorBody()),
//                                    null
//                                )
//                        }
//                    }
//                }
//            }catch (e:Exception){
//                obsmapUser.value = e.localizedMessage?.let {
//                    Resource.error(it,null)
//                }
//            }
//        }
//    }
}
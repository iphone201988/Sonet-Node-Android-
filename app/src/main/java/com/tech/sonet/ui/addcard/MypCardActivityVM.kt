package com.tech.sonet.ui.addcard

import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.tech.sonet.data.api.ApiHelper
import com.tech.sonet.data.model.response.*
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.utils.NetworkHelper
import com.tech.sonet.utils.Resource
import com.tech.sonet.utils.event.NetworkErrorHandler
import com.tech.sonet.utils.event.SingleRequestEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class MypCardActivityVM @Inject constructor(
    private val apiHelper: ApiHelper,
    private val networkErrorHandler: NetworkErrorHandler,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {


    val obrCommon = SingleRequestEvent<JsonObject>()

    fun myCard(map: HashMap<String, Any>, url : String) {
        if (networkHelper.isNetworkConnected()) {
            obrCommon.value = Resource.loading(null)
            try {
                viewModelScope.launch {
                    apiHelper.apiRawBodyWithAuth(map,url).let {
                        if (it.isSuccessful) {
                            obrCommon.value =   Resource.success(it.body(),"postCard")

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

    fun getLevels(url: String) {
        if (networkHelper.isNetworkConnected()) {
            obrCommon.value = Resource.loading(null)
            try {
                viewModelScope.launch {
                    apiHelper.apiGetWithAuth(url).let {
                        if (it.isSuccessful) {
                            obrCommon.value = Resource.success(it.body(), "getLevels")
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

    fun uploadImage(parts: MultipartBody.Part? , url : String){
        if (networkHelper.isNetworkConnected()) {
            obrCommon.value = Resource.loading(null)
            try {
                viewModelScope.launch {
                    apiHelper.apiForPostMultipartImage(parts,url).let {
                        if (it.isSuccessful) {
                            obrCommon.value = Resource.success(it.body(), "uploadPhoto")
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

    fun cardUpload(data : HashMap<String, Any>, url: String){
        if (networkHelper.isNetworkConnected()) {
            obrCommon.value = Resource.loading(null)
            try {
                viewModelScope.launch {
                    apiHelper.apiRawBodyWithAuth(data,url).let {
                        if (it.isSuccessful) {
                            obrCommon.value = Resource.success(it.body(), "cardUpload")
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

    fun cardUpdate(data : HashMap<String,Any>, url: String){
        if (networkHelper.isNetworkConnected()) {
            obrCommon.value = Resource.loading(null)
            try {
                viewModelScope.launch {
                    apiHelper.apiForPutRawBody(data,url).let {
                        if (it.isSuccessful) {
                            obrCommon.value = Resource.success(it.body(), "cardUpdate")
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

    val obsCardUpdate = SingleRequestEvent<CardUpdateResponse>()
    fun update(
        map: HashMap<String, RequestBody>, image: MultipartBody.Part,
        levelId: Int,
        categoryId: Int,
        id: String, key: String
    ) {
        if (networkHelper.isNetworkConnected()) {
            obsCardUpdate.value = Resource.loading(null)
            try {
                viewModelScope.launch {
                    apiHelper.cardUpdate(map, image,levelId,categoryId, id, key).let {
                        if (it.isSuccessful) {
                            obsCardUpdate.value = Resource.success(it.body(), it.body()?.message)
                        } else {
                            obsCardUpdate.value = Resource.error(
                                handleErrorResponse(it.errorBody(),it.code()), null
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                obsCardUpdate.value = e.localizedMessage?.let {
                    Resource.error(it, null)
                }
            }
        }
    }

}




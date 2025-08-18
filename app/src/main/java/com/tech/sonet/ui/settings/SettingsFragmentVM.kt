package com.tech.sonet.ui.settings

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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsFragmentVM @Inject constructor(
    private val apiHelper: ApiHelper,
    private val networkErrorHandler: NetworkErrorHandler,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {


    val obrCommon = SingleRequestEvent<JsonObject>()


    fun deleteAccount(url: String){
        if (networkHelper.isNetworkConnected()) {
            obrCommon.value = Resource.loading(null)
            try {
                viewModelScope.launch {
                    apiHelper.deletePost( url).let {
                        if (it.isSuccessful) {
                            obrCommon.value = Resource.success(it.body(), "deleteAccount")
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

    fun reportProblem(data : HashMap<String, Any>, url : String) {
        if (networkHelper.isNetworkConnected()) {
            obrCommon.value = Resource.loading(null)
            try {
                viewModelScope.launch {
                    apiHelper.apiRawBodyWithAuth(data, url).let {
                        if (it.isSuccessful) {
                            obrCommon.value = Resource.success(it.body(), "reportProblem")
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

    fun checkNotificationAndLocationStatus(data: HashMap<String, Any>, url: String){
        if (networkHelper.isNetworkConnected()) {
            obrCommon.value = Resource.loading(null)
            try {
                viewModelScope.launch {
                    apiHelper.apiForPutRawBody(data, url).let {
                        if (it.isSuccessful) {
                            obrCommon.value = Resource.success(it.body(), "notificationLocation")
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



    val observeContact = SingleRequestEvent<ContactResponse>()
    fun contact(id: String, key: String, msg: String) {
        if (networkHelper.isNetworkConnected()) {
            observeContact.value = Resource.loading(null)
            try {
                viewModelScope.launch {
                    apiHelper.contact(id, key, msg).let {
                        if (it.isSuccessful) {
                            observeContact.value = Resource.success(null, it.body()?.message)
                        } else {
                            observeContact.value =
                                Resource.error(
                                    handleErrorResponse(it.errorBody(),it.code()),
                                    null
                                )
                        }
                    }
                }
            } catch (e: Exception) {
                observeContact.value = e.localizedMessage?.let {
                    Resource.error(
                        it, null
                    )
                }
            }
        }
    }

    fun logout(url: String){
        if (networkHelper.isNetworkConnected()) {
            obrCommon.value = Resource.loading(null)
            try {
                viewModelScope.launch {
                    apiHelper.deletePost( url).let {
                        if (it.isSuccessful) {
                            obrCommon.value = Resource.success(it.body(), "logout")
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

    val obsLocation = SingleRequestEvent<LocationResponse>()
    fun location(map: HashMap<String, String>, id: String, key: String) {
        if (networkHelper.isNetworkConnected()) {
            obsLocation.value = Resource.loading(null)
            try {
                viewModelScope.launch {
                    apiHelper.location(map, id, key).let {
                        if (it.isSuccessful) {
                            obsLocation.value = Resource.success(it.body(), it.body()?.message)
                        } else {
                            obsLocation.value = Resource.error(
                                handleErrorResponse(it.errorBody(),it.code()),
                                null
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                obsLocation.value = e.localizedMessage?.let {
                    Resource.error(it, null)
                }
            }
        }
    }

    val obsPushNotification = SingleRequestEvent<LocationResponse>()
    fun pushNoti(map: HashMap<String, String>, id: String, key: String) {
        if (networkHelper.isNetworkConnected()) {
            obsPushNotification.value = Resource.loading(null)
            try {
                viewModelScope.launch {
                    apiHelper.pushNotification(map, id, key).let {
                        if (it.isSuccessful) {
                            obsPushNotification.value =
                                Resource.success(it.body(), it.body()?.message)
                        } else {
                            obsPushNotification.value = Resource.error(
                                handleErrorResponse(it.errorBody(),it.code()),
                                null
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                obsPushNotification.value = e.localizedMessage?.let {
                    Resource.error(it, null)
                }
            }
        }
    }

}
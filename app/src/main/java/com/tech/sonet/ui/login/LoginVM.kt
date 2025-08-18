package com.tech.sonet.ui.login

import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.tech.sonet.data.api.ApiHelper
import com.tech.sonet.data.model.request.LoginRequest
import com.tech.sonet.data.model.response.LoginResponse
import com.tech.sonet.data.model.response.MyCardsResponse
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.utils.NetworkHelper
import com.tech.sonet.utils.Resource
import com.tech.sonet.utils.event.NetworkErrorHandler
import com.tech.sonet.utils.event.SingleRequestEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class LoginVM @Inject constructor(
    private val apiHelper: ApiHelper,
    private val networkErrorHandler: NetworkErrorHandler,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {


    val obsLogin = SingleRequestEvent<ResponseBody>()

    fun loginUser(map: LoginActivity.LoginRequest1) {
        if (networkHelper.isNetworkConnected()) {
            obsLogin.value = Resource.loading(null)
            try {
                viewModelScope.launch {
                    apiHelper.apiForRawLogin(map).let {
                        if (it.isSuccessful) {
                            obsLogin.value =
                                Resource.success(it.body(),"login")
                        } else {
                            obsLogin.value = Resource.error(handleErrorResponse(it.errorBody(),it.code()),
                                null
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                obsLogin.value = e.localizedMessage?.let {
                    Resource.error(
                        it, null
                    )
                }
            }
        }
    }

    val obrCard = SingleRequestEvent<JsonObject>()
    fun myCard(url : String) {
        if (networkHelper.isNetworkConnected()) {
            obrCard.value = Resource.loading(null)
            try {
                viewModelScope.launch {
                    apiHelper.apiGetWithAuth(url).let {
                        if (it.isSuccessful) {
                            obrCard.value =   Resource.success(it.body(),"getCard")

                        } else {
                            obrCard.value = Resource.error(
                                handleErrorResponse(it.errorBody(),it.code()),
                                null
                            )
                        }
                    }

                }
            } catch (e: Exception) {
                obrCard.value = e.localizedMessage?.let {
                    Resource.error(it, null)
                }
            }
        }
    }
}
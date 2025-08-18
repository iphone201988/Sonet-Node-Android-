package com.tech.sonet.ui.forgotpassword

import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.tech.sonet.data.api.ApiHelper
import com.tech.sonet.data.api.SimpleApiResponse
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.utils.Resource
import com.tech.sonet.utils.event.SingleRequestEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(private val apiHelper: ApiHelper) :
    BaseViewModel() {

    val obsForgotPassword = SingleRequestEvent<JsonObject>()

    fun forgotPassword(request: HashMap<String, Any>, url : String) {
        viewModelScope.launch {
            obsForgotPassword.postValue(Resource.loading(null))
            try {
                apiHelper.apiForFormData(request, url).let {
                    if (it.isSuccessful && it.body() != null) {
                        obsForgotPassword.postValue(Resource.success(it.body(), "forgotPassword"))
                    } else obsForgotPassword.postValue(Resource.error(handleErrorResponse(it.errorBody(),it.code()), null))
                }
            }catch (e : Exception){
                obsForgotPassword.postValue(Resource.error(e.message.toString(),null))
            }

        }
    }


}
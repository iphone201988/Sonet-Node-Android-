package com.tech.sonet.ui.forgotemail.reset_password

import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.tech.sonet.data.api.ApiHelper
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.utils.Resource
import com.tech.sonet.utils.event.SingleRequestEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordActivityVm @Inject constructor(private val apiHelper: ApiHelper) : BaseViewModel() {

    val obrCommon = SingleRequestEvent<JsonObject>()

    fun resetPassword(data : HashMap<String, Any>, url : String ){
        viewModelScope.launch {
            obrCommon.postValue(Resource.loading(null))
            try {
                apiHelper.apiForgot(data, url).let {
                    if (it.isSuccessful && it.body() != null) {
                        obrCommon.postValue(Resource.success(it.body(), "resetPassword"))
                    } else {
                        obrCommon.postValue(Resource.error(handleErrorResponse(it.errorBody(),it.code()), null))
                    }
                }
            }catch (e : Exception){
                obrCommon.postValue(Resource.error(e.message.toString(),null))
            }

        }
    }
}
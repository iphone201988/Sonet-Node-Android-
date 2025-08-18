package com.tech.sonet.ui.signup

import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.tech.sonet.data.api.ApiHelper
import com.tech.sonet.data.model.response.SignupResponse
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.utils.NetworkHelper
import com.tech.sonet.utils.Resource
import com.tech.sonet.utils.event.NetworkErrorHandler
import com.tech.sonet.utils.event.SingleRequestEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val apiHelper: ApiHelper,
    private val networkErrorHandler: NetworkErrorHandler,
   private val networkHelper: NetworkHelper
) : BaseViewModel() {
    val observerSignup = SingleRequestEvent<JsonObject>()

    fun signUp(map: HashMap<String, Any> , url : String) {
        if (networkHelper.isNetworkConnected()) {
        observerSignup.value = Resource.loading(null)
        try {
            viewModelScope.launch {
                apiHelper.apiForRawSignUp(map, url).let {
                    if (it.isSuccessful) {
                        observerSignup.value =
                            Resource.success(it.body(),"signUp")
                    } else {
                        observerSignup.value = Resource.error(handleErrorResponse(it.errorBody(),it.code()),
                            null
                        )
                    }
                }
            }
        } catch (e: Exception) {
            observerSignup.value = e.localizedMessage?.let {
                Resource.error(
                    it, null
                )
            }
        }
    }
}
}
package com.tech.sonet.data.api

import android.util.Log
import com.google.gson.JsonObject
import com.tech.sonet.data.local.SharedPrefManager
import com.tech.sonet.data.model.request.LoginRequest
import com.tech.sonet.data.model.response.*
import com.tech.sonet.ui.login.LoginActivity
import com.tech.sonet.utils.Constant
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService, private val sharedPreferences: SharedPrefManager) : ApiHelper {


    override suspend fun login(map: HashMap<String, Any>): Response<ApiResponse<LoginResponse>> {
        return apiService.login(map)
    }

    override suspend fun signup(map: HashMap<String, String>): Response<ApiResponse<SignupResponse>> {
        return apiService.signup(map)
    }

    override suspend fun forgetPassword(request: HashMap<String, String>): SimpleApiResponse {
        return apiService.forgetPassword(request)
    }

    override suspend fun cardUpload(
        map: HashMap<String, RequestBody>,
        image: MultipartBody.Part,
        levelId: Int,
        categoryId: Int,
        id: String,
        key: String
    ): Response<CardUploadResponse> {
        return apiService.cardUpload(map, image, levelId, categoryId, id, key)
    }



    override suspend fun logout(id: String, key: String): Response<Logout> {
        return apiService.logout(id, key)
    }

    override suspend fun delete(id: String, key: String): Response<DeleteResponse> {
        return apiService.delete(id, key)
    }

    override suspend fun request(
        map: HashMap<String, String>,
        id: String,
        key: String
    ): Response<RequestResponse> {
        return apiService.request(map, id, key)
    }

    override suspend fun likeSentHistory(
        map: HashMap<String, String>,
        id: String,
        key: String
    ): Response<RequestResponse> {
        return  apiService.likeSentHistory(map,id, key)
    }

    override suspend fun clearRequest(id: String, key: String): Response<NotificationResponse> {
        return apiService.clearRequest(id, key)
    }

    override suspend fun cardList(
        map: HashMap<String, String>,
        id: String,
        key: String
    ): Response<CardListResponse> {
        return apiService.cardList(map, id, key)
    }

    override suspend fun updateList(
        map: HashMap<String, String>,
        id: String,
        key: String
    ): Response<CardListResponse> {
        return apiService.updateList(map, id, key)
    }

    override suspend fun sendRequest(
        map: HashMap<String, String>,
        id: String,
        key: String
    ): Response<RequestSendResponse> {
        return apiService.sendRequest(map, id, key)
    }

    override suspend fun contact(id: String, key: String, msg: String): Response<ContactResponse> {
        return apiService.contact(id, key, msg)
    }

    override suspend fun report(id: String, key: String, msg: String): Response<ReportResponse> {
        return apiService.report(id, key, msg)
    }

    override suspend fun location(
        map: HashMap<String, String>,
        id: String,
        key: String
    ): Response<LocationResponse> {
        return apiService.location(map, id, key)
    }

    override suspend fun pushNotification(
        map: HashMap<String, String>,
        id: String,
        key: String
    ): Response<LocationResponse> {
        return apiService.pushNotification(map, id, key)
    }

    override suspend fun cardUpdate(
        map: HashMap<String, RequestBody>,
        image: MultipartBody.Part,
        levelId: Int,
        categoryId: Int,
        id: String,
        key: String
    ): Response<CardUpdateResponse> {
        return apiService.cardUpdate(map, image,levelId,categoryId, id, key)
    }

    override suspend fun myCard(
        map: HashMap<String, String>,
        id: String,
        key: String
    ): Response<MyCardsResponse> {
        return apiService.myCard(map, id, key)
    }

    override suspend fun category(id: String, key: String): Response<CategoryResponse> {
        return apiService.category(id, key)
    }

    override suspend fun accepted(id: String, key: String): Response<AcceptedResponse> {
       return apiService.accepted(id,key)
    }

    override suspend fun level(id: String, key: String): Response<LevelResponse> {
        return apiService.level(id, key)
    }

    override suspend fun apiForRawLogin(request: LoginActivity.LoginRequest1): Response<ResponseBody> {
        return apiService.apiForRawLogin(request)
    }

    override suspend fun apiForRawSignUp(
        request: HashMap<String, Any>,
        url: String
    ): Response<JsonObject> {
        return apiService.apiForRawSignUp(request, url)
    }

    override suspend fun apiRawBodyWithAuth(
        request: HashMap<String, Any>,
        url: String
    ): Response<JsonObject> {
         return  apiService.apiRawBodyWithAuth(request,url,getTokenFromSPref())
    }

    override suspend fun apiForFormData(
        data: HashMap<String, Any>,
        url: String
    ): Response<JsonObject> {
        return apiService.apiForFormData(data,url)
    }

    override suspend fun apiForgot(
        data: HashMap<String, Any>,
        url: String,
    ): Response<JsonObject> {
        Log.i("Dsadasdasd", "apiForgot: ${Constant.authToken}")
        return apiService.apiForFormDataWithAuth(data,url,"Bearer ${Constant.authToken}")

    }

    override suspend fun apiForFormDataWithAuth(
        data: HashMap<String, Any>,
        url: String
    ): Response<JsonObject> {
        return apiService.apiForFormDataWithAuth(data,url,getTokenFromSPref())
    }

    override suspend fun apiGetWithAuth(url: String): Response<JsonObject> {
        return  apiService.apiGetWithAuth(url , getTokenFromSPref())
    }

    override suspend fun apiForPostMultipartImage(
        part: MultipartBody.Part?,
        url: String
    ): Response<JsonObject> {
        return  apiService.apiForPostMultipartImage(url,part,getTokenFromSPref())
    }

    override suspend fun apiForPutRawBody(data: HashMap<String, Any>, url: String) : Response<JsonObject> {
        return apiService.apiForPutRawBody(data ,url,getTokenFromSPref())
    }

    override suspend fun deletePost(url: String): Response<JsonObject> {
        return apiService.deletePost(url,getTokenFromSPref())
    }

    override suspend fun apiGetWithQueryAuth(
        data: HashMap<String, Any>,
        url: String
    ): Response<JsonObject> {
        return apiService.apiGetWithQueryAuth(url, data, getTokenFromSPref())
    }

    override suspend fun deleteAll(url: String, data: HashMap<String, Any>): Response<JsonObject> {
        return  apiService.deleteAll(url,data,getTokenFromSPref())
    }

    override suspend fun getCardList(
        queryData: HashMap<String, Any>,
        data: HashMap<String, Any>,
        url: String
    ): Response<JsonObject> {
        return apiService.getCardListApi(url,queryData,data,getTokenFromSPref())
    }

    override suspend fun apiBackGround(url: String): Response<JsonObject> {
        return apiService.apiBackGround(url,getTokenFromSPref())
    }

    override suspend fun apiForeGround(url: String): Response<JsonObject> {
        return apiService.apiForeGround(url,getTokenFromSPref())
    }

    override suspend fun deleteRequest(
        url: String,
        data: HashMap<String, Any>
    ): Response<JsonObject> {
        return apiService.deleteRequest(url,data,getTokenFromSPref())
    }


//    private fun getTokenFromSPref(): String {
//
//        return "Bearer${
//            sharedPreferences.getToken()?.trim()
//        }"
//    }

    private fun getTokenFromSPref(): String {
        return "Bearer ${sharedPreferences.getCurrentUser()?.data?.token}"
    }
}



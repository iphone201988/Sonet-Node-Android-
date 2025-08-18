package com.tech.sonet.data.api

import com.google.gson.JsonObject
import com.tech.sonet.data.model.request.LoginRequest
import com.tech.sonet.data.model.response.*
import com.tech.sonet.ui.login.LoginActivity
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body

interface ApiHelper {

    suspend fun login(map: HashMap<String, Any>): Response<ApiResponse<LoginResponse>>

    suspend fun signup(map: HashMap<String, String>): Response<ApiResponse<SignupResponse>>

    suspend fun forgetPassword(request: HashMap<String, String>): SimpleApiResponse

    suspend fun cardUpload(
        map: HashMap<String, RequestBody>,
        image: MultipartBody.Part,
        levelId: Int,
        categoryId: Int,
        id: String,
        key: String
    ): Response<CardUploadResponse>

    suspend fun logout(id: String, key: String): Response<Logout>

    suspend fun delete(id: String, key: String): Response<DeleteResponse>

    suspend fun request(
        map: HashMap<String, String>, id: String, key: String
    ): Response<RequestResponse>

    suspend fun likeSentHistory(
        map: HashMap<String, String>, id: String, key: String
    ) : Response<RequestResponse>
    suspend fun clearRequest(id: String, key: String): Response<NotificationResponse>

    suspend fun cardList(
        map: HashMap<String, String>, id: String, key: String
    ): Response<CardListResponse>

    suspend fun updateList(
        map: HashMap<String, String>, id: String, key: String
    ): Response<CardListResponse>


    suspend fun sendRequest(
        map: HashMap<String, String>, id: String, key: String
    ): Response<RequestSendResponse>

    suspend fun contact(id: String, key: String, msg: String): Response<ContactResponse>

    suspend fun report(id: String, key: String, msg: String): Response<ReportResponse>

    suspend fun location(
        map: HashMap<String, String>, id: String, key: String
    ): Response<LocationResponse>

    suspend fun pushNotification(
        map: HashMap<String, String>, id: String, key: String
    ): Response<LocationResponse>

    suspend fun cardUpdate(
        map: HashMap<String, RequestBody>,
        image: MultipartBody.Part,
        levelId: Int,
        categoryId: Int,
        id: String,
        key: String
    ): Response<CardUpdateResponse>

    suspend fun myCard(
        map: HashMap<String, String>, id: String, key: String
    ): Response<MyCardsResponse>

    suspend fun category(
        id: String, key: String
    ): Response<CategoryResponse>

    suspend fun accepted(
        id: String, key: String
    ): Response<AcceptedResponse>

    suspend fun level(
        id: String, key: String
    ): Response<LevelResponse>

    suspend fun apiForRawLogin(request: LoginActivity.LoginRequest1):Response<ResponseBody>

    suspend fun apiForRawSignUp(request: HashMap<String,Any>, url : String) : Response<JsonObject>

    suspend fun  apiRawBodyWithAuth(request: HashMap<String, Any>, url: String): Response<JsonObject>

    suspend fun apiForFormData(data: HashMap<String, Any>, url: String) : Response<JsonObject>

    suspend fun apiForgot(data: HashMap<String, Any>, url: String ): Response<JsonObject>

    suspend fun apiForFormDataWithAuth(data: HashMap<String, Any>, url: String) : Response<JsonObject>

    suspend fun  apiGetWithAuth(url : String) : Response<JsonObject>

    suspend fun apiForPostMultipartImage( part: MultipartBody.Part?, url: String) : Response<JsonObject>

    suspend fun apiForPutRawBody(data: HashMap<String, Any>, url: String) : Response<JsonObject>

    suspend fun deletePost(url: String) : Response<JsonObject>

    suspend fun apiGetWithQueryAuth(data: HashMap<String, Any>, url: String) : Response<JsonObject>

    suspend fun deleteAll(url: String, data: HashMap<String, Any>) : Response<JsonObject>

    suspend fun getCardList(queryData : HashMap<String,Any>, data: HashMap<String, Any>, url: String) : Response<JsonObject>

    suspend fun apiBackGround(url: String) : Response<JsonObject>

    suspend fun apiForeGround(url: String) : Response<JsonObject>

    suspend fun deleteRequest(url : String, data: HashMap<String, Any>) : Response<JsonObject>
}
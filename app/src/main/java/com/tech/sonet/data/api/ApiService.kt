package com.tech.sonet.data.api

import com.google.gson.JsonObject
import com.tech.sonet.data.model.request.LoginRequest
import com.tech.sonet.data.model.response.*
import com.tech.sonet.ui.login.LoginActivity
import com.tech.sonet.utils.Constant
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @Headers(Constant.HEADER_API)
    @FormUrlEncoded
    @POST("users/login")
    suspend fun login(
        @FieldMap map: HashMap<String, Any>
    ): Response<ApiResponse<LoginResponse>>

    @FormUrlEncoded
    @POST("sign_up")
    suspend fun signup(
        @FieldMap map: HashMap<String, String>
    ): Response<ApiResponse<SignupResponse>>

    @FormUrlEncoded
    @POST("forget_password")
    suspend fun forgetPassword(@FieldMap request: HashMap<String, String>): SimpleApiResponse

    @POST("logout")
    suspend fun logout(
        @Header("id") id: String, @Header("session_key") key: String
    ): Response<Logout>

    @DELETE("delete_account")
    suspend fun delete(
        @Header("id") id: String, @Header("session_key") key: String
    ): Response<DeleteResponse>

    @FormUrlEncoded
    @POST("requests")
    suspend fun request(
        @FieldMap request: HashMap<String, String>,
        @Header("id") id: String,
        @Header("session_key") key: String
    ): Response<RequestResponse>

    @FormUrlEncoded
    @POST("requests")
    suspend fun likeSentHistory(
        @FieldMap request: HashMap<String, String>,
        @Header("id") id: String,
        @Header("session_key") key: String
    ): Response<RequestResponse>


    @POST("notification_clear")
    suspend fun clearRequest(
        @Header("id") id: String, @Header("session_key") key: String
    ): Response<NotificationResponse>

    @FormUrlEncoded
    @POST("cards_list")
    suspend fun cardList(
        @FieldMap map: HashMap<String, String>,
        @Header("id") id: String,
        @Header("session_key") key: String
    ): Response<CardListResponse>

    @FormUrlEncoded
    @POST("cards_list")
    suspend fun updateList(
        @FieldMap map: HashMap<String, String>,
        @Header("id") id: String,
        @Header("session_key") key: String
    ): Response<CardListResponse>


    @FormUrlEncoded
    @POST("cards_list")
    suspend fun sendRequest(
        @FieldMap map: HashMap<String, String>,
        @Header("id") id: String,
        @Header("session_key") key: String
    ): Response<RequestSendResponse>

    @FormUrlEncoded
    @POST("contact")
    suspend fun contact(
        @Header("id") id: String, @Header("session_key") key: String, @Field("message") msg: String
    ): Response<ContactResponse>

    @FormUrlEncoded
    @POST("report")
    suspend fun report(
        @Header("id") id: String, @Header("session_key") key: String, @Field("message") msg: String
    ): Response<ReportResponse>

    @FormUrlEncoded
    @POST("update_user")
    suspend fun location(
        @FieldMap map: HashMap<String, String>,
        @Header("id") id: String,
        @Header("session_key") key: String
    ): Response<LocationResponse>

    @FormUrlEncoded
    @POST("update_user")
    suspend fun pushNotification(
        @FieldMap map: HashMap<String, String>,
        @Header("id") id: String,
        @Header("session_key") key: String
    ): Response<LocationResponse>


    @Multipart
    @POST("cards_upload")
    suspend fun cardUpload(
        @PartMap map: HashMap<String, RequestBody>,
        @Part post_image: MultipartBody.Part,
        @Part("level") levelId : Int,
        @Part("category") categoryId : Int,
        @Header("id") id: String,
        @Header("session_key") key: String
    ): Response<CardUploadResponse>


    @Multipart
    @POST("card_update")
    suspend fun cardUpdate(
        @PartMap map: HashMap<String, RequestBody>,
        @Part post_image: MultipartBody.Part,
        @Part("level") levelId : Int,
        @Part("category") categoryId : Int,
        @Header("id") id: String,
        @Header("session_key") key: String
    ): Response<CardUpdateResponse>


    @GET("my_cards")
    suspend fun myCard(
        @QueryMap map: HashMap<String, String>,
        @Header("id") id: String,
        @Header("session_key") key: String
    ): Response<MyCardsResponse>

    @GET("category")
    suspend fun category(
        @Header("id") id: String,
        @Header("session_key") key: String
    ): Response<CategoryResponse>

    @GET("acceptedRequest")
    suspend fun accepted(
        @Header("id") id: String,
        @Header("session_key") key: String
    ): Response<AcceptedResponse>

    @GET("level")
    suspend fun level(
        @Header("id") id: String,
        @Header("session_key") key: String
    ): Response<LevelResponse>



    @Headers(Constant.HEADER_API)
    @POST("users/login")
    suspend fun apiForRawLogin(@Body data: LoginActivity.LoginRequest1): Response<ResponseBody>

    @Headers(Constant.HEADER_API)
    @POST
    suspend fun apiForRawSignUp(@Body data: HashMap<String, Any> , @Url url: String): Response<JsonObject>


    @Headers(Constant.HEADER_API)
    @POST
    suspend fun apiRawBodyWithAuth(@Body data: HashMap<String, Any> , @Url url: String, @Header("Authorization") token: String): Response<JsonObject>


    @Headers(Constant.HEADER_API)
    @POST
    suspend fun getCardListApi( @Url url: String, @QueryMap queryData: HashMap<String, Any> ,@Body data: HashMap<String, Any> , @Header("Authorization") token: String): Response<JsonObject>

    @Headers(Constant.HEADER_API)
    @FormUrlEncoded
    @POST
    suspend fun apiForFormData(
        @FieldMap data: HashMap<String, Any>,
        @Url url: String
    ): Response<JsonObject>


    @Headers(Constant.HEADER_API)
    @FormUrlEncoded
    @POST
    suspend fun apiForFormDataWithAuth(
        @FieldMap data: HashMap<String, Any>,
        @Url url: String,
        @Header("Authorization") token: String
    ): Response<JsonObject>


    @Headers(Constant.HEADER_API)
    @GET
    suspend fun apiGetWithAuth(@Url url: String , @Header("Authorization") token: String): Response<JsonObject>

    @Headers(Constant.HEADER_API)
    @GET
    suspend fun apiGetWithQueryAuth(@Url url: String, @QueryMap data : HashMap<String, Any>,  @Header("Authorization") token: String): Response<JsonObject>

    @Headers(Constant.HEADER_API)
    @Multipart
    @JvmSuppressWildcards
    @POST
    suspend fun apiForPostMultipartImage(
        @Url url: String,
        @Part parts: MultipartBody.Part?,
        @Header("Authorization") token: String,
    ): Response<JsonObject>


    @Headers(Constant.HEADER_API)
    @PUT
    suspend fun apiForPutRawBody(@Body data: HashMap<String, Any>,  @Url url: String,   @Header("Authorization") token: String): Response<JsonObject>


    @Headers(Constant.HEADER_API)
    @DELETE
    suspend fun deletePost(@Url url: String, @Header("Authorization") token: String) : Response<JsonObject>
    @Headers(Constant.HEADER_API)
    @DELETE
    suspend fun deleteRequest(@Url url: String, @QueryMap data : HashMap<String,Any>,  @Header("Authorization") token: String) : Response<JsonObject>

    @Headers(Constant.HEADER_API)
    @DELETE
    suspend fun deleteAll( @Url url: String,  @QueryMap data : HashMap<String,Any>, @Header("Authorization") token: String) : Response<JsonObject>


    @Headers(Constant.HEADER_API)
    @FormUrlEncoded
    @POST
    suspend fun apiBackGround(
        @Url url: String,
        @Header("Authorization") token: String
    ): Response<JsonObject>


    @Headers(Constant.HEADER_API)
    @DELETE
    suspend fun apiForeGround(@Url url: String, @Header("Authorization") token: String) : Response<JsonObject>
}


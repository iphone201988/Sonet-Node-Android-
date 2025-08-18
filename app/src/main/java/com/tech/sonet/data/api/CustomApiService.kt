package com.tech.sonet.data.api

import com.tech.sonet.data.model.BackGroundApiResponse
import com.tech.sonet.utils.Constant
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

interface CustomApiService {


    @Headers(Constant.HEADER_API)
    @POST
    suspend fun apiBackGround(
        @Url url: String,
        @Header("Authorization") token: String
    ): Response<BackGroundApiResponse>


    @Headers(Constant.HEADER_API)
    @DELETE
    suspend fun apiForeGround(@Url url: String, @Header("Authorization") token: String) : Response<SimpleApiResponse>
}
package com.tech.sonet.data.api

import android.content.Context
import com.google.gson.JsonObject
import com.tech.sonet.data.local.CustomSharedPrefManager
import com.tech.sonet.data.model.BackGroundApiResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CustomApiHelper(private val context: Context, private val sharedPrefManager: CustomSharedPrefManager) {



    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://51.21.41.1:3000/api/") // Replace with your base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val apiService: CustomApiService by lazy {
        retrofit.create(CustomApiService::class.java)
    }


    // Function to call apiForFormData
    suspend fun apiBackGround(url: String): Response<BackGroundApiResponse>? {
        return try {
            val token = "Bearer ${sharedPrefManager.getCurrentUser()?.data?.token}" // Fetch token from Shared Preferences
            apiService.apiBackGround(url, token)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Function to call deletePost
    suspend fun apiForeGround(url: String): Response<SimpleApiResponse>? {
        return try {
            val token = "Bearer ${sharedPrefManager.getCurrentUser()?.data?.token}" // Fetch token from Shared Preferences
            apiService.apiForeGround(url, token)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}

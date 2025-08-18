package com.tech.sonet.data.api

import com.google.gson.annotations.SerializedName

class ApiResponse<T> : SimpleApiResponse() {
    @SerializedName("data")
    val data: T? = null

    override fun toString(): String {
        return "ApiResponse{" +
                "data=" + data +
                '}'
    }
}
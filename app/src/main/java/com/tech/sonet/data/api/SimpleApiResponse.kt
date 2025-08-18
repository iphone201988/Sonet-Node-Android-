package com.tech.sonet.data.api

import com.google.gson.annotations.SerializedName

open class SimpleApiResponse {
    @SerializedName("method")
    var method: String? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("status")
    var status = 0

    @SerializedName("success")
    var success: String? = null

    fun isSuccess(): String? {
        return method
    }

    override fun toString(): String {
        return "SimpleApiResponse{" +
                "method=" + method +
                ", message=" + message +
                ", success=" + success +
                ",status=" + status + '\'' +
                '}'
    }
}

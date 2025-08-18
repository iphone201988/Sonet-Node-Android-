package com.tech.sonet.data.model.request

data class LoginRequest(
    val email:String,
    val password:String,
    val latitude:String,
    val longitude:String,
    val device_type:Int,
    val device_token:String

)

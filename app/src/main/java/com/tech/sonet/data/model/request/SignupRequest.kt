package com.tech.sonet.data.model.request

data class SignupRequest(
    val user_name:String,
    val email:String,
    val password:String,
    val confirm_password:String,
    val latitude:String,
    val longitude:String,
    val device_type:Int,
    val device_token:String,
    val mobile:Long?
)

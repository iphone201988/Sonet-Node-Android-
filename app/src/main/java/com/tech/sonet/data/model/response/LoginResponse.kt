package com.tech.sonet.data.model.response

//data class LoginResponse(
//    val card_uploaded: Int?,
//    val created_at: String?,
//    val device_token: String?,
//    val device_type: Int?,
//    val email: String?,
//    val email_verified_at: String?,
//    val id: Int?,
//    val is_active: Int?,
//    var is_location_active: Int?,
//    val latitude: String?,
//    val level: String?,
//    val login_times: Int?,
//    val longitude: String?,
//    val mobile: String?,
//    val notification_is_active: Int?,
//    val role: Int?,
//    val session_key: String?,
//    val token: String?,
//    val total_users: Int?,
//    val updated_at: String?,
//    val user_name: String?
//
//
//)

data class LoginResponse(
    var `data`: Data?,
    var message: String?,
    var status: Int?
) {
    data class Data(
        var __v: Int?,
        var _id: String?,
        var createdAt: String?,
        var email: String?,
        var gender: Any?,
        var card_uploaded : Int?,
        var isAccountVerified: Boolean?,
        var isLocation: Boolean?,
        var isNotification: Boolean?,
        var level: String?,
        var location: Location?,
        var mobile: String?,
        var role: String?,
        var token: String?,
        var updatedAt: String?,
        var user_name: String?,
        var user_status: Any?
    ) {
        data class Location(
            var coordinates: List<Double?>?,
            var type: String?
        )
    }
}











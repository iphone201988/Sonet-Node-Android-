package com.tech.sonet.data.model.response

data class LocationResponse(
    val `data`: LocationData?,
    val message: String?,
    val status: Int?
)

data class LocationData(
    val created_at: String?,
    val device_token: String?,
    val device_type: Int?,
    val email: String?,
    val email_verified_at: String?,
    val id: Int?,
    val is_active: Int?,
    val is_location_active: Int?,
    val latitude: String?,
    val longitude: String?,
    val notification_is_active: Int?,
    val role: Int?,
    val session_key: String?,
    val token: String?,
    val updated_at: String?,
    val user_name: String?
)
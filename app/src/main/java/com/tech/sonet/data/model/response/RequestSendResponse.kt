package com.tech.sonet.data.model.response

data class RequestSendResponse(
    val data: RequestData?,
    val message: String?,
    val status: Int?
)

data class RequestData(
    val category: String?,
    val category_id: Int?,
    val category_image: String?,
    val category_name: String?,
    val created_at: String?,
    val description: String?,
    val id: Int?,
    val isOnline: Int?,
    val is_active: Int?,
    val latitude: String?,
    val level: String?,
    val level_id: Int?,
    val longitude: String?,
    val mobile: String?,
    val post_image: String?,
    val social_media: String?,
    val title: String?,
    val updated_at: String?,
    val user_id: Int?
)






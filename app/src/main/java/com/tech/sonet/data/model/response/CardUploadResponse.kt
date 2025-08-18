package com.tech.sonet.data.model.response


data class CardUploadResponse(
    val `data`: CardUploadData?,
    val message: String?,
    val status: Int?
)
data class CardUploadData(
    val category: String?,
    val category_id: Int?,
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


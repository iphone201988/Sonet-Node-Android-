package com.tech.sonet.data.model.response

data class ContactResponse(
    val `data`: ContactData?,
    val message: String?,
    val status: Int?
)

data class ContactData(
    val created_at: String?,
    val email: String?,
    val id: Int?,
    val is_active: Int?,
    val message: String?,
    val updated_at: String?
)
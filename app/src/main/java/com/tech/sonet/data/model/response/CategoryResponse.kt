package com.tech.sonet.data.model.response

data class CategoryResponse(
    val `data`: List<CategoryData>?,
    val message: String?,
    val status: Int?
)

data class CategoryData(
    val admin_id: Int?,
    val category: String?,
    val category_image: String?,
    val category_name: String?,
    val created_at: String?,
    val id: Int?,
    val status: Int?,
    val updated_at: String?
)
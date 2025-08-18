package com.tech.sonet.data.model.response

data class LevelResponse(
    val `data`: List<LevelData>?,
    val message: String?,
    val status: Int?
)

data class LevelData(
    val created_at: String?,
    val id: Int?,
    val is_active: Int?,
    val level: String?,
    val updated_at: String?
)
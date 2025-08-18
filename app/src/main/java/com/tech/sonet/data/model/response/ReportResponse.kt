package com.tech.sonet.data.model.response

data class ReportResponse(
    val `data`: ReportData?,
    val message: String?,
    val status: Int?
)

data class ReportData(
    val created_at: String?,
    val email: String?,
    val id: Int?,
    val is_active: Int?,
    val message: String?,
    val updated_at: String?
)
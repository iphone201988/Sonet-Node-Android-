package com.tech.sonet.data.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AcceptedResponse(
    val `data`: List<AcceptedData>?,
    val message: String?,
    val status: Int?
): Parcelable
@Parcelize
data class AcceptedData(
    val category: String? = null,
    val category_id: Int?,
    val category_image: String? = null,
    val category_name: String? = null,
    var created_at: String? = null,
    val description: String? = null,
    val id: Int?,
    val isOnline: Int?,
    val is_active: Int?,
    val level: String?,
    val level_id: Int?,
    val mobile: String?,
    val post_image: String? = null,
    val social_media: String? = null,
    val title: String? = null,
    val type: Int?,
    val updated_at: String? = null,
    val user_id: Int?
): Parcelable
package com.tech.sonet.data.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class RequestResponse(
    val data: List<Data>?,
    val message: String?,
    val meta_data: MetaData?,
    val status: Int?
)

@Parcelize
data class Data(
    val card_id: Int?,
    val category: String?,
    val category_id: Int?,
    val category_image: String?,
    val category_name: String?,
    var created_at: String?,
    val description: String?,
    val distances: String?,
    val id: Int?,
    val isOnline: Int?,
    val is_active: Int?,
    val latitude: String?,
    val level: String?,
    val level_id: Int?,
    val longitude: String?,
    val mobile: String?,
    val post_image: String?,
    val receiver_id: Int?,
    val sender_id: Int?,
    val title: String?,
    val social_media: String?,
    val type: Int?,
    val updated_at: String?
):Parcelable




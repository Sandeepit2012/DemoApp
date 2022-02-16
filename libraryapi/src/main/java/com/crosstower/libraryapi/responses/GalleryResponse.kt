package com.crosstower.libraryapi.responses


import com.crosstower.libraryapi.models.GalleryData
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GalleryResponse(
    @Json(name = "data")
    val `data`: GalleryData?,
    @Json(name = "status")
    val status: Int,
    @Json(name = "success")
    val success: Boolean
) {
}
package com.zeroillusion.binapp.data.remote.dto


import com.google.gson.annotations.SerializedName

data class CountryDto(
    @SerializedName("alpha2")
    val alpha2: String?,
    @SerializedName("currency")
    val currency: String?,
    @SerializedName("emoji")
    val emoji: String?,
    @SerializedName("latitude")
    val latitude: Float?,
    @SerializedName("longitude")
    val longitude: Float?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("numeric")
    val numeric: String?
)
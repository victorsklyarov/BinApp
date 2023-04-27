package com.zeroillusion.binapp.data.remote.dto


import com.google.gson.annotations.SerializedName

data class BankDto(
    @SerializedName("city")
    val city: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("url")
    val url: String?
)
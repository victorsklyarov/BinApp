package com.zeroillusion.binapp.data.remote.dto


import com.google.gson.annotations.SerializedName

data class NumberDto(
    @SerializedName("length")
    val length: Int?,
    @SerializedName("luhn")
    val luhn: Boolean?
)
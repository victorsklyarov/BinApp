package com.zeroillusion.binapp.domain.model

data class Bin(
    val bin: Int,
    val length: String,
    val luhn: String,
    val scheme: String,
    val type: String,
    val brand: String,
    val prepaid: String,
    val countryName: String,
    val countryEmoji: String,
    val countryLatitude: String,
    val countryLongitude: String,
    val bankName: String,
    val bankUrl: String,
    val bankPhone: String,
    val bankCity: String
)

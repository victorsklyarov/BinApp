package com.zeroillusion.binapp.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.zeroillusion.binapp.domain.model.Bin

@Entity(indices = [Index(value = ["bin"], unique = true)])
data class BinEntity(
    val bin: Int,
    val length: String = "",
    val luhn: String = "",
    val scheme: String = "",
    val type: String = "",
    val brand: String = "",
    val prepaid: String = "",
    val countryNumeric: String = "",
    val countryAlpha2: String = "",
    val countryName: String = "",
    val countryEmoji: String = "",
    val countryCurrency: String = "",
    val countryLatitude: String = "",
    val countryLongitude: String = "",
    val bankName: String = "",
    val bankUrl: String = "",
    val bankPhone: String = "",
    val bankCity: String = "",
    @PrimaryKey val id: Int? = null
) {
    fun toBin(): Bin {
        return Bin(
            bin = bin,
            length = length,
            luhn = luhn,
            scheme = scheme,
            type = type,
            brand = brand,
            prepaid = prepaid,
            countryName = countryName,
            countryEmoji = countryEmoji,
            countryLatitude = countryLatitude,
            countryLongitude = countryLongitude,
            bankName = bankName,
            bankUrl = bankUrl,
            bankPhone = bankPhone,
            bankCity = bankCity,
        )
    }
}
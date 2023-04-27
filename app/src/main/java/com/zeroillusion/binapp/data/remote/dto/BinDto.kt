package com.zeroillusion.binapp.data.remote.dto


import com.google.gson.annotations.SerializedName
import com.zeroillusion.binapp.data.local.entity.BinEntity
import com.zeroillusion.binapp.domain.model.Bin

data class BinDto(
    @SerializedName("bank")
    val bank: BankDto?,
    @SerializedName("brand")
    val brand: String?,
    @SerializedName("country")
    val country: CountryDto?,
    @SerializedName("number")
    val number: NumberDto?,
    @SerializedName("prepaid")
    val prepaid: Boolean?,
    @SerializedName("scheme")
    val scheme: String?,
    @SerializedName("type")
    val type: String?
) {
    fun toBinEntity(bin: Int): BinEntity {
        return BinEntity(
            bin = bin,
            length = number?.length?.toString().orEmpty(),
            luhn = number?.luhn?.toString().orEmpty(),
            scheme = scheme.orEmpty(),
            type = type.orEmpty(),
            brand = brand.orEmpty(),
            prepaid = prepaid?.toString().orEmpty(),
            countryNumeric = country?.numeric.orEmpty(),
            countryAlpha2 = country?.alpha2.orEmpty(),
            countryName = country?.name.orEmpty(),
            countryEmoji = country?.emoji.orEmpty(),
            countryCurrency = country?.currency.orEmpty(),
            countryLatitude = country?.latitude?.toString().orEmpty(),
            countryLongitude = country?.longitude?.toString().orEmpty(),
            bankName = bank?.name.orEmpty(),
            bankUrl = bank?.url.orEmpty(),
            bankPhone = bank?.phone.orEmpty(),
            bankCity = bank?.city.orEmpty()
        )
    }

    fun toBin(bin: Int): Bin {
        return Bin(
            bin = bin,
            length = number?.length?.toString().orEmpty(),
            luhn = number?.luhn?.toString().orEmpty(),
            scheme = scheme.orEmpty(),
            type = type.orEmpty(),
            brand = brand.orEmpty(),
            prepaid = prepaid?.toString().orEmpty(),
            countryName = country?.name.orEmpty(),
            countryEmoji = country?.emoji.orEmpty(),
            countryLatitude = country?.latitude?.toString().orEmpty(),
            countryLongitude = country?.longitude?.toString().orEmpty(),
            bankName = bank?.name.orEmpty(),
            bankUrl = bank?.url.orEmpty(),
            bankPhone = bank?.phone.orEmpty(),
            bankCity = bank?.city.orEmpty()
        )
    }
}
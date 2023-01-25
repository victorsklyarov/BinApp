package com.zeroillusion.binapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "bin_items")
data class BinItem(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "bin") val bin: String,
    @Ignore @SerializedName("number") @Expose var number: Number? = null,
    @Ignore @SerializedName("scheme") @Expose var scheme: String? = null,
    @Ignore @SerializedName("type") @Expose var type: String? = null,
    @Ignore @SerializedName("brand") @Expose var brand: String? = null,
    @Ignore @SerializedName("prepaid") @Expose var prepaid: Boolean? = null,
    @Ignore @SerializedName("country") @Expose var country: Country? = null,
    @Ignore @SerializedName("bank") @Expose var bank: Bank? = null
) {
    constructor(id: Int, bin: String)
            : this(id, bin,
        null, null, null, null, null, null, null)

}

data class Number(
    @SerializedName("length") @Expose var length: Int? = null,
    @SerializedName("luhn") @Expose var luhn: Boolean? = null
)

data class Country(
    @SerializedName("numeric") @Expose var numeric: String? = null,
    @SerializedName("alpha2") @Expose var alpha2: String? = null,
    @SerializedName("name") @Expose var name: String? = null,
    @SerializedName("emoji") @Expose var emoji: String? = null,
    @SerializedName("currency") @Expose var currency: String? = null,
    @SerializedName("latitude") @Expose var latitude:Double? = null,
    @SerializedName("longitude") @Expose var longitude: Double? = null
)

data class Bank(
    @SerializedName("name") @Expose var name: String? = null,
    @SerializedName("url") @Expose var url: String? = null,
    @SerializedName("phone") @Expose var phone: String? = null,
    @SerializedName("city") @Expose var city: String? = null
)
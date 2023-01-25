package com.zeroillusion.binapp.api

import com.zeroillusion.binapp.data.BinItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BinApi {

    @GET("{bin}")
    fun getBin(@Path("bin") bin: String): Call<BinItem>
}
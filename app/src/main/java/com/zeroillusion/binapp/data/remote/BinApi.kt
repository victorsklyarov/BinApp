package com.zeroillusion.binapp.data.remote

import com.zeroillusion.binapp.data.remote.dto.BinDto
import retrofit2.http.GET
import retrofit2.http.Path

interface BinApi {

    @GET("/{bin}")
    suspend fun getBin(@Path("bin") bin: Int): BinDto
}
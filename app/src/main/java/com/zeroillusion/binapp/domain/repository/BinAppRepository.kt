package com.zeroillusion.binapp.domain.repository

import com.zeroillusion.binapp.domain.model.Bin
import com.zeroillusion.binapp.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface BinAppRepository {

    fun getBinFromApi(bin: Int): Flow<Resource<Bin>>

    fun getBinList(): Flow<Resource<List<Bin>>>

    fun getBinFromDatabase(bin: Int): Flow<Resource<Bin>>

    suspend fun deleteAllBins()
}
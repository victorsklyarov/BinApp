package com.zeroillusion.binapp.data.repository

import com.zeroillusion.binapp.data.local.BinDao
import com.zeroillusion.binapp.data.remote.BinApi
import com.zeroillusion.binapp.data.remote.dto.BinDto
import com.zeroillusion.binapp.domain.model.Bin
import com.zeroillusion.binapp.domain.repository.BinAppRepository
import com.zeroillusion.binapp.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class BinAppRepositoryImpl(
    private val api: BinApi,
    private val dao: BinDao
) : BinAppRepository {

    override fun getBinFromApi(bin: Int): Flow<Resource<Bin>> = flow {
        emit(Resource.Loading())
        var remoteBin: BinDto? = null
        try {
            remoteBin = api.getBin(bin)
            dao.insertBin(remoteBin.toBinEntity(bin))
        } catch (e: HttpException) {
            if (e.response()?.code() == 404) {
                emit(
                    Resource.Error(
                        message = "This BIN does not exist",
                        data = null
                    )
                )
            } else if (e.response()?.code() == 429) {
                emit(
                    Resource.Error(
                        message = "Requests exceeded, please try again later",
                        data = null
                    )
                )
            } else {
                emit(
                    Resource.Error(
                        message = e.localizedMessage ?: "An unexpected error occurred",
                        data = null
                    )
                )
            }
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = "Couldn't reach server. Check your internet connection.",
                    data = null
                )
            )
        }

        if (remoteBin != null) {
            emit(
                Resource.Success(remoteBin.toBin(bin))
            )
        }
    }

    override fun getBinList(): Flow<Resource<List<Bin>>> = flow {
        emit(Resource.Loading())
        val binList = dao.getBinList().map { it.toBin() }
        emit(Resource.Success(data = binList))
    }

    override fun getBinFromDatabase(bin: Int): Flow<Resource<Bin>> = flow {
        emit(Resource.Loading())
        val binData = dao.getBin(bin).toBin()
        emit(Resource.Success(data = binData))
    }

    override suspend fun deleteAllBins() {
        return dao.deleteAllBins()
    }
}
package com.zeroillusion.binapp.domain.use_case

import com.zeroillusion.binapp.domain.model.Bin
import com.zeroillusion.binapp.domain.repository.BinAppRepository
import com.zeroillusion.binapp.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

class GetBinList(
    private val repository: BinAppRepository
) {

    operator fun invoke(): Flow<Resource<List<Bin>>> {
        return repository.getBinList()
    }
}
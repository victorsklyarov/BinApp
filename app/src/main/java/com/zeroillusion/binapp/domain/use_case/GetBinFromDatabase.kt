package com.zeroillusion.binapp.domain.use_case

import com.zeroillusion.binapp.domain.model.Bin
import com.zeroillusion.binapp.domain.repository.BinAppRepository
import com.zeroillusion.binapp.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

class GetBinFromDatabase(
    private val repository: BinAppRepository
) {

    operator fun invoke(bin: Int): Flow<Resource<Bin>> {
        return repository.getBinFromDatabase(bin)
    }
}
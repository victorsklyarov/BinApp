package com.zeroillusion.binapp.domain.use_case

import com.zeroillusion.binapp.domain.repository.BinAppRepository

class DeleteBinHistory(
    private val repository: BinAppRepository
) {

    suspend operator fun invoke() {
        return repository.deleteAllBins()
    }
}
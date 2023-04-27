package com.zeroillusion.binapp.presentation.history

import com.zeroillusion.binapp.domain.model.Bin

data class HistoryScreenState(
    val binList: List<Bin> = listOf(),
    val isLoading: Boolean = false
)

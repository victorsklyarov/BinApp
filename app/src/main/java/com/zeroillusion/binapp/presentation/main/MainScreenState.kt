package com.zeroillusion.binapp.presentation.main

import com.zeroillusion.binapp.domain.model.Bin

data class MainScreenState(
    val inputBinNumber: String = "",
    val inputBinNumberError: String? = null,
    val bin: Bin? = null,
    val isLoading: Boolean = false
)
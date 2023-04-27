package com.zeroillusion.binapp.domain.use_case

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)
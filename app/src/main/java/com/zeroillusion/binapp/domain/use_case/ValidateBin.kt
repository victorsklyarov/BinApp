package com.zeroillusion.binapp.domain.use_case

class ValidateBin {

    operator fun invoke(bin: String): ValidationResult {
        if (bin.length < 4) {
            return ValidationResult(
                successful = false,
                errorMessage = "Cannot be less than 4 characters"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}
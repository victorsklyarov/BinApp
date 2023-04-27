package com.zeroillusion.binapp.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeroillusion.binapp.domain.use_case.GetBinFromApi
import com.zeroillusion.binapp.domain.use_case.GetBinFromDatabase
import com.zeroillusion.binapp.domain.use_case.ValidateBin
import com.zeroillusion.binapp.domain.utils.Constants
import com.zeroillusion.binapp.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val validateBin: ValidateBin,
    private val getBinFromApi: GetBinFromApi,
    private val getBinFromDatabase: GetBinFromDatabase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var state by mutableStateOf(MainScreenState())

    init {
        savedStateHandle.get<String>(Constants.PARAM_DEL)?.let { del ->
            if (del.isNotEmpty()) {
                if (del.toBoolean()) {
                    viewModelScope.launch {
                        delay(500)
                        _eventFlow.emit(
                            UIEvent.ShowSnackbar(message = "History has been cleared")
                        )
                    }
                } else {
                    savedStateHandle.get<String>(Constants.PARAM_BIN)?.let { bin ->
                        state = state.copy(inputBinNumber = bin)
                        loadBin()
                    }
                }
            }
        }
    }

    fun getBin() {
        val binResult = validateBin(state.inputBinNumber)
        if (!binResult.successful) {
            state = state.copy(inputBinNumberError = binResult.errorMessage)
            return
        } else {
            state = state.copy(inputBinNumberError = null)
        }
        getBinFromApi(state.inputBinNumber.toInt()).onEach { getBinFromApiResult ->
            delay(500L)
            when (getBinFromApiResult) {
                is Resource.Success -> {
                    state = state.copy(
                        bin = getBinFromApiResult.data,
                        isLoading = false
                    )
                }

                is Resource.Error -> {
                    state = state.copy(
                        bin = getBinFromApiResult.data,
                        isLoading = false
                    )
                    _eventFlow.emit(
                        UIEvent.ShowSnackbar(
                            getBinFromApiResult.message ?: "Unknown error"
                        )
                    )
                }

                is Resource.Loading -> {
                    state = state.copy(
                        bin = getBinFromApiResult.data,
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun loadBin() {
        getBinFromDatabase(state.inputBinNumber.toInt()).onEach { getBinFromDatabaseResult ->
            delay(500L)
            when (getBinFromDatabaseResult) {
                is Resource.Success -> {
                    state = state.copy(
                        bin = getBinFromDatabaseResult.data,
                        isLoading = false
                    )
                }

                is Resource.Error -> {
                    state = state.copy(
                        bin = getBinFromDatabaseResult.data,
                        isLoading = false
                    )
                    _eventFlow.emit(
                        UIEvent.ShowSnackbar(
                            getBinFromDatabaseResult.message ?: "Unknown error"
                        )
                    )
                }

                is Resource.Loading -> {
                    state = state.copy(
                        bin = getBinFromDatabaseResult.data,
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }
}
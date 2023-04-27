package com.zeroillusion.binapp.presentation.history

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeroillusion.binapp.domain.use_case.DeleteBinHistory
import com.zeroillusion.binapp.domain.use_case.GetBinList
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
class HistoryViewModel @Inject constructor(
    private val getBinList: GetBinList,
    private val deleteBinHistory: DeleteBinHistory
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var state by mutableStateOf(HistoryScreenState())

    init {
        getBins()
    }

    private fun getBins() {
        getBinList().onEach { getBinListResult ->
            delay(500L)
            when (getBinListResult) {
                is Resource.Success -> {
                    state = state.copy(
                        binList = getBinListResult.data ?: listOf(),
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        binList = getBinListResult.data ?: listOf(),
                        isLoading = false
                    )
                    _eventFlow.emit(
                        UIEvent.ShowSnackbar(
                            getBinListResult.message ?: "Unknown error"
                        )
                    )
                }
                is Resource.Loading -> {
                    state = state.copy(
                        binList = getBinListResult.data ?: listOf(),
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun navigateToMainScreen(bin: Int? = null, del: Boolean? = null) {
        viewModelScope.launch {
            _eventFlow.emit(
                UIEvent.NavigateToMainScreen(bin, del)
            )
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            deleteBinHistory()
            navigateToMainScreen(del = true)
        }
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
        data class NavigateToMainScreen(val bin: Int?, val del: Boolean?) : UIEvent()
    }
}
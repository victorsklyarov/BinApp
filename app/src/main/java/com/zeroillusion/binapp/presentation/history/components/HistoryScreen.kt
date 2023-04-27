package com.zeroillusion.binapp.presentation.history.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.zeroillusion.binapp.R
import com.zeroillusion.binapp.presentation.history.HistoryViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val state = viewModel.state
    val openDialog = remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { innerPadding ->
            LaunchedEffect(key1 = true) {
                viewModel.eventFlow.collectLatest { event ->
                    when (event) {
                        is HistoryViewModel.UIEvent.ShowSnackbar -> {
                            snackbarHostState.showSnackbar(
                                message = event.message
                            )
                        }

                        is HistoryViewModel.UIEvent.NavigateToMainScreen -> {
                            navController.navigate(
                                "main_screen?bin=${
                                    event.bin?.toString().orEmpty()
                                }?del=${event.del?.toString().orEmpty()}"
                            ) {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.binList.size) { i ->
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.navigateToMainScreen(
                                    bin = state.binList.reversed()[i].bin,
                                    del = false
                                )
                            }
                            .padding(10.dp)

                        ) {
                            Text(text = state.binList.reversed()[i].bin.toString())
                        }
                        Divider()
                    }
                }
                FloatingActionButton(
                    onClick = {
                        openDialog.value = true
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(25.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.clear_history)
                    )
                }
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    )

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = stringResource(R.string.clear_history))
            },
            text = {
                Text(text = stringResource(R.string.history_message))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearHistory()
                        openDialog.value = false
                    }
                ) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}
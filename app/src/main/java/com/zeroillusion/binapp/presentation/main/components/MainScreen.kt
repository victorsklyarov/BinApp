package com.zeroillusion.binapp.presentation.main.components

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.zeroillusion.binapp.R
import com.zeroillusion.binapp.presentation.main.MainViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val state = viewModel.state
    val context = LocalContext.current

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { innerPadding ->
            LaunchedEffect(key1 = true) {
                viewModel.eventFlow.collectLatest { event ->
                    when (event) {
                        is MainViewModel.UIEvent.ShowSnackbar -> {
                            snackbarHostState.showSnackbar(
                                message = event.message
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 400.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val color = MaterialTheme.colorScheme.secondaryContainer
                    IconButton(
                        onClick = { navController.navigate("history_screen") },
                        modifier = Modifier
                            .padding(15.dp)
                            .align(Alignment.End)
                            .drawBehind {
                                drawRoundRect(
                                    color = color,
                                    cornerRadius = CornerRadius(100f)
                                )
                            }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_history),
                            contentDescription = "History"
                        )
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    val keyboardController = LocalSoftwareKeyboardController.current
                    val maxChar = 8
                    OutlinedTextField(
                        value = state.inputBinNumber,
                        onValueChange = {
                            if (it.length <= maxChar) viewModel.state =
                                state.copy(inputBinNumber = it.replace(Regex("\\D"), ""))
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                viewModel.getBin()
                                keyboardController?.hide()
                            }),
                        supportingText = {
                            if (state.inputBinNumberError != null) {
                                Text(text = state.inputBinNumberError)
                            }
                            Text(
                                text = "${state.inputBinNumber.length} / $maxChar",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                            )
                        },
                        label = {
                            Text(text = stringResource(R.string.enter_bin))
                        },
                        trailingIcon = {
                            if (state.inputBinNumber.isNotEmpty()) {
                                IconButton(onClick = {
                                    viewModel.state = state.copy(inputBinNumber = "")
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = stringResource(R.string.clear_field)
                                    )
                                }
                            }
                        },
                        isError = state.inputBinNumberError != null,
                        modifier = Modifier
                            .widthIn(max = 280.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Button(onClick = {
                        viewModel.getBin()
                        keyboardController?.hide()
                    }) {
                        Text(text = stringResource(R.string.check_bin))
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(0.dp)
                                .weight(1f)
                                .padding(10.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = RoundedCornerShape(15.dp)
                                    )
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.onPrimaryContainer,
                                        RoundedCornerShape(15.dp)
                                    ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = stringResource(R.string.scheme))
                                Text(
                                    text =
                                    if (!state.bin?.scheme.isNullOrEmpty()) state.bin!!.scheme.replaceFirstChar(
                                        Char::uppercaseChar
                                    ) else "",
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxWidth()
                                        .background(
                                            color = MaterialTheme.colorScheme.background,
                                            shape = RoundedCornerShape(15.dp)
                                        )
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.onPrimaryContainer,
                                            RoundedCornerShape(15.dp)
                                        ),
                                    textAlign = TextAlign.Center
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = RoundedCornerShape(15.dp)
                                    )
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.onPrimaryContainer,
                                        RoundedCornerShape(15.dp)
                                    ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = stringResource(R.string.brand))
                                Text(
                                    text = state.bin?.brand.orEmpty(),
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxWidth()
                                        .background(
                                            color = MaterialTheme.colorScheme.background,
                                            shape = RoundedCornerShape(15.dp)
                                        )
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.onPrimaryContainer,
                                            RoundedCornerShape(15.dp)
                                        ),
                                    textAlign = TextAlign.Center
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = RoundedCornerShape(15.dp)
                                    )
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.onPrimaryContainer,
                                        RoundedCornerShape(15.dp)
                                    ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = stringResource(R.string.card_number))
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .width(0.dp)
                                            .weight(1f)
                                            .padding(5.dp)
                                            .border(
                                                1.dp,
                                                MaterialTheme.colorScheme.onPrimaryContainer,
                                                RoundedCornerShape(10.dp)
                                            ),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(text = stringResource(R.string.length))
                                        Text(
                                            text = state.bin?.length.orEmpty(),
                                            modifier = Modifier
                                                .padding(top = 5.dp)
                                                .fillMaxWidth()
                                                .background(
                                                    color = MaterialTheme.colorScheme.background,
                                                    shape = RoundedCornerShape(15.dp)
                                                )
                                                .border(
                                                    1.dp,
                                                    MaterialTheme.colorScheme.onPrimaryContainer,
                                                    RoundedCornerShape(15.dp)
                                                ),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                    Column(
                                        modifier = Modifier
                                            .width(0.dp)
                                            .weight(1f)
                                            .padding(5.dp)
                                            .border(
                                                1.dp,
                                                MaterialTheme.colorScheme.onPrimaryContainer,
                                                RoundedCornerShape(10.dp)
                                            ),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(text = stringResource(R.string.luhn))
                                        Text(
                                            text =
                                            if (!state.bin?.luhn.isNullOrEmpty()) (if (state.bin?.luhn.toBoolean()) "Yes" else "No") else "",
                                            modifier = Modifier
                                                .padding(top = 5.dp)
                                                .fillMaxWidth()
                                                .background(
                                                    color = MaterialTheme.colorScheme.background,
                                                    shape = RoundedCornerShape(15.dp)
                                                )
                                                .border(
                                                    1.dp,
                                                    MaterialTheme.colorScheme.onPrimaryContainer,
                                                    RoundedCornerShape(15.dp)
                                                ),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = RoundedCornerShape(15.dp)
                                    )
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.onPrimaryContainer,
                                        RoundedCornerShape(15.dp)
                                    ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = stringResource(R.string.country))
                                Column(
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .heightIn(min = 100.dp)
                                        .fillMaxWidth()
                                        .background(
                                            color = MaterialTheme.colorScheme.background,
                                            shape = RoundedCornerShape(15.dp)
                                        )
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.onPrimaryContainer,
                                            RoundedCornerShape(15.dp)
                                        )
                                        .clickable {
                                            if (!state.bin?.countryLatitude.isNullOrEmpty()) {
                                                if (!state.bin?.countryLongitude.isNullOrEmpty()) {
                                                    val intent =
                                                        Intent(Intent.ACTION_VIEW).apply {
                                                            data = Uri.parse(
                                                                "geo:${state.bin?.countryLatitude},${state.bin?.countryLongitude}?z=7"
                                                            )
                                                        }
                                                    try {
                                                        ContextCompat.startActivity(
                                                            context,
                                                            intent,
                                                            null
                                                        )
                                                    } catch (_: ActivityNotFoundException) {
                                                    }
                                                }
                                            }
                                        }
                                ) {
                                    Text(
                                        text = state.bin?.countryEmoji.orEmpty() + " " + state.bin?.countryName.orEmpty(),
                                        modifier = Modifier.padding(
                                            start = 5.dp,
                                            top = 5.dp,
                                            end = 5.dp
                                        ),
                                        textAlign = TextAlign.Center
                                    )
                                    Column {
                                        Text(
                                            text = if (!state.bin?.countryLatitude.isNullOrEmpty()) "Latitude: ${state.bin?.countryLatitude}" else "",
                                            modifier = Modifier.padding(horizontal = 5.dp)
                                        )
                                        Text(
                                            text = if (!state.bin?.countryLongitude.isNullOrEmpty()) "Longitude: ${state.bin?.countryLongitude}" else "",
                                            modifier = Modifier.padding(
                                                start = 5.dp,
                                                end = 5.dp,
                                                bottom = 5.dp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(0.dp)
                                .weight(1f)
                                .padding(10.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = RoundedCornerShape(15.dp)
                                    )
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.onPrimaryContainer,
                                        RoundedCornerShape(15.dp)
                                    ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = stringResource(R.string.type))
                                Text(
                                    text = if (!state.bin?.type.isNullOrEmpty()) state.bin!!.type.replaceFirstChar(
                                        Char::uppercaseChar
                                    ) else "",
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxWidth()
                                        .background(
                                            color = MaterialTheme.colorScheme.background,
                                            shape = RoundedCornerShape(15.dp)
                                        )
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.onPrimaryContainer,
                                            RoundedCornerShape(15.dp)
                                        ),
                                    textAlign = TextAlign.Center
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = RoundedCornerShape(15.dp)
                                    )
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.onPrimaryContainer,
                                        RoundedCornerShape(15.dp)
                                    ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = stringResource(R.string.prepaid))
                                Text(
                                    text =
                                    if (!state.bin?.prepaid.isNullOrEmpty()) (if (state.bin?.prepaid.toBoolean()) "Yes" else "No") else "",
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxWidth()
                                        .background(
                                            color = MaterialTheme.colorScheme.background,
                                            shape = RoundedCornerShape(15.dp)
                                        )
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.onPrimaryContainer,
                                            RoundedCornerShape(15.dp)
                                        ),
                                    textAlign = TextAlign.Center
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = RoundedCornerShape(15.dp)
                                    )
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.onPrimaryContainer,
                                        RoundedCornerShape(15.dp)
                                    ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = stringResource(R.string.bank))
                                Column(
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .heightIn(min = 125.dp)
                                        .fillMaxWidth()
                                        .background(
                                            color = MaterialTheme.colorScheme.background,
                                            shape = RoundedCornerShape(15.dp)
                                        )

                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.onPrimaryContainer,
                                            RoundedCornerShape(15.dp)
                                        )
                                ) {
                                    Text(
                                        text = state.bin?.bankName.orEmpty(),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                start = 5.dp,
                                                top = 5.dp,
                                                end = 5.dp
                                            )
                                    )
                                    Text(
                                        text = state.bin?.bankCity.orEmpty(),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                horizontal = 5.dp
                                            )
                                    )
                                    Text(
                                        text = state.bin?.bankUrl.orEmpty(),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                horizontal = 5.dp
                                            )
                                            .clickable {
                                                state.bin?.bankUrl?.let { url ->
                                                    val webpage: Uri = Uri.parse("https://$url")
                                                    val intent = Intent(Intent.ACTION_VIEW, webpage)
                                                    try {
                                                        ContextCompat.startActivity(
                                                            context,
                                                            intent,
                                                            null
                                                        )
                                                    } catch (_: ActivityNotFoundException) {
                                                    }
                                                }
                                            }
                                    )
                                    Text(
                                        text = state.bin?.bankPhone.orEmpty(),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                start = 5.dp,
                                                end = 5.dp,
                                                bottom = 5.dp
                                            )
                                            .clickable {
                                                state.bin?.bankPhone?.let { phoneNumber ->
                                                    val intent = Intent(Intent.ACTION_DIAL).apply {
                                                        data = Uri.parse("tel:$phoneNumber")
                                                    }
                                                    try {
                                                        ContextCompat.startActivity(
                                                            context,
                                                            intent,
                                                            null
                                                        )
                                                    } catch (_: ActivityNotFoundException) {
                                                    }
                                                }
                                            }
                                    )
                                }
                            }
                        }
                    }
                }
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    )
}
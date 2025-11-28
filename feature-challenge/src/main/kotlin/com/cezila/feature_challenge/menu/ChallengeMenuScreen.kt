package com.cezila.feature_challenge.menu

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cezila.core_designsystem.R
import com.cezila.core_designsystem.component.CustomWordTextField
import com.cezila.core_designsystem.currentSnackbarHostState
import com.cezila.core_designsystem.theme.PalavraoTheme
import com.cezila.core_designsystem.utils.GameShareManager
import com.cezila.core_navigation.PalavraoScreen
import com.cezila.core_navigation.currentComposeNavigator
import com.cezila.domain.error.CreateFriendChallengeError
import com.cezila.domain.error.StartFriendRoundError

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ChallengeMenuScreen(
    vm: ChallengeViewModel = hiltViewModel<ChallengeViewModel>()
) {
    val nav = currentComposeNavigator
    val uiState by vm.uiState.collectAsState()
    val snackbarHostState = currentSnackbarHostState
    val context = LocalContext.current
    val shareManager = remember(context) { GameShareManager(context) }

    LaunchedEffect(Unit) {
        vm.navigationEvent.collect { event ->
            when (event) {
                is ChallengeNavigation.NavigateToGame -> {
                    nav.navigate(
                        PalavraoScreen.FriendChallengeScreen(
                            round = event.round,
                            code = event.code
                        )
                    )
                }
            }
        }
    }

    val startErrorMessage = uiState.startError?.let { error ->
        when (error) {
            StartFriendRoundError.NOT_FOUND -> context.getString(R.string.challenge_not_found)
            else -> context.getString(R.string.start_challenge_generic_error)
        }
    }

    val createErrorMessage = uiState.createError?.let { error ->
        when (error) {
            CreateFriendChallengeError.INVALID_LENGTH -> context.getString(R.string.invalid_length)
            CreateFriendChallengeError.INVALID_CHARS -> context.getString(R.string.invalid_chars)
            CreateFriendChallengeError.INVALID_WORD -> context.getString(R.string.invalid_word)
            else -> context.getString(R.string.create_challenge_generic_error)
        }
    }

    LaunchedEffect(startErrorMessage, createErrorMessage) {
        val message = startErrorMessage ?: createErrorMessage
        if (message != null) {
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = context.getString(R.string.ok)
            )
            vm.onEvent(ChallengeEvent.OnErrorShown)
        }
    }

    ChallengeContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onEvent = vm::onEvent,
        onBackPressed = nav::navigateUp,
        onGeneratedCodeClicked = { text ->
            shareManager.copyToClipboard(text)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeContent(
    uiState: ChallengeUiState,
    snackbarHostState: SnackbarHostState? = null,
    onEvent: (ChallengeEvent) -> Unit,
    onGeneratedCodeClicked: (String) -> Unit,
    onBackPressed: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.challenge_title)) },
                navigationIcon = {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.arrow_back_description)
                        )
                    }
                },
                actions = {},
                colors = TopAppBarColors(
                    containerColor = PalavraoTheme.background.color,
                    scrolledContainerColor = PalavraoTheme.background.color,
                    navigationIconContentColor = PalavraoTheme.colors.primary,
                    titleContentColor = PalavraoTheme.colors.primary,
                    actionIconContentColor = PalavraoTheme.colors.primary
                ),
                modifier = Modifier.padding(vertical = 20.dp)
            )
        },
        snackbarHost = {
            if (snackbarHostState != null) {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.imePadding()
                ) { data ->
                    Snackbar(
                        snackbarData = data,
                        actionColor = PalavraoTheme.colors.primary
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PalavraoTheme.background.color)
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = stringResource(R.string.challenge_mode),
                    fontWeight = FontWeight.Bold,
                    color = PalavraoTheme.colors.primary,
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomWordTextField(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight(),
                        text = uiState.createWord,
                        hint = stringResource(R.string.type_a_word_hint),
                        onValueChange = { onEvent(ChallengeEvent.OnCreateWordChanged(it)) },
                        enabled = !uiState.isCreatingChallenge
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    if (uiState.generatedCode != null) {
                        Text(
                            modifier = Modifier.clickable(
                                enabled = true,
                                onClick = { onGeneratedCodeClicked(uiState.generatedCode) }),
                            text = uiState.generatedCode,
                            color = PalavraoTheme.colors.primary,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (uiState.isCreatingChallenge) {
                    CircularProgressIndicator(
                        color = PalavraoTheme.colors.primary
                    )
                } else {
                    Button(
                        modifier = Modifier
                            .width(280.dp)
                            .height(45.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonColors(
                            containerColor = PalavraoTheme.colors.surface,
                            contentColor = PalavraoTheme.colors.primary,
                            disabledContainerColor = PalavraoTheme.colors.surface,
                            disabledContentColor = PalavraoTheme.colors.primary
                        ),
                        onClick = { onEvent(ChallengeEvent.OnCreateChallengeClicked) },
                        enabled = uiState.createWord.length == 5
                    ) {
                        Text(stringResource(R.string.generate_code_button))
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))

                Text(
                    text = stringResource(R.string.play_challenge_title),
                    fontWeight = FontWeight.Bold,
                    color = PalavraoTheme.colors.primary,
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomWordTextField(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight(),
                    text = uiState.joinCode,
                    hint = stringResource(R.string.join_code_hint),
                    onValueChange = { onEvent(ChallengeEvent.OnJoinCodeChanged(it)) },
                    enabled = !uiState.isStartingChallenge
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (uiState.isStartingChallenge) {
                    CircularProgressIndicator(color = PalavraoTheme.colors.primary)
                } else {
                    Button(
                        modifier = Modifier
                            .width(280.dp)
                            .height(45.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonColors(
                            containerColor = PalavraoTheme.colors.surface,
                            contentColor = PalavraoTheme.colors.primary,
                            disabledContainerColor = PalavraoTheme.colors.surface,
                            disabledContentColor = PalavraoTheme.colors.primary
                        ),
                        onClick = { onEvent(ChallengeEvent.OnStartChallengeClicked) },
                        enabled = uiState.joinCode.length == 6
                    ) {
                        Text(stringResource(R.string.start_challenge_button))
                    }
                }
            }
        }
    }
}

@Preview(locale = "pt", widthDp = 320)
@Composable
private fun ChallengeContentPreview() {
    PalavraoTheme {
        ChallengeContent(
            uiState = ChallengeUiState(),
            onEvent = {},
            onGeneratedCodeClicked = {},
        )
    }
}
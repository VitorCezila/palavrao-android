package com.cezila.feature_challenge.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cezila.core_designsystem.R
import com.cezila.core_designsystem.component.CustomWordTextField
import com.cezila.core_designsystem.theme.PalavraoTheme
import com.cezila.core_navigation.PalavraoScreen
import com.cezila.core_navigation.currentComposeNavigator

@Composable
fun ChallengeMenuScreen(
    vm: ChallengeViewModel = hiltViewModel<ChallengeViewModel>()
) {
    val nav = currentComposeNavigator
    val uiState by vm.uiState.collectAsState()

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

    ChallengeContent(
        uiState = uiState,
        onEvent = vm::onEvent,
        onBackPressed = nav::navigateUp
    )

    if (uiState.createError != null) {
//        ErrorDialog(
//            error = "",
//            onDismiss = { vm.onEvent(ChallengeEvent.OnErrorDialogDismissed) }
//        )
    }

    if (uiState.startError != null) {
//        ErrorDialog(
//            error = "",
//            onDismiss = { vm.onEvent(ChallengeEvent.OnErrorDialogDismissed) }
//        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeContent(
    uiState: ChallengeUiState,
    onEvent: (ChallengeEvent) -> Unit,
    onBackPressed: () -> Unit = {}
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
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Desafiar Amigos",
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
                        hint = "Digite uma palavra (5 letras)",
                        onValueChange = { onEvent(ChallengeEvent.OnCreateWordChanged(it)) },
                        enabled = !uiState.isCreatingChallenge
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    if (uiState.generatedCode != null) {
                        Text(
                            text = uiState.generatedCode,
                            color = PalavraoTheme.colors.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (uiState.isCreatingChallenge) {
                    CircularProgressIndicator()
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
                        Text("GERAR CÓDIGO")
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))

                Text(
                    text = "Jogar Desafio",
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
                    hint = "Digite o código do desafio",
                    onValueChange = { onEvent(ChallengeEvent.OnJoinCodeChanged(it)) },
                    enabled = !uiState.isStartingChallenge
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (uiState.isStartingChallenge) {
                    CircularProgressIndicator()
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
                        Text("INICIAR DESAFIO")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ChallengeContentPreview() {
    PalavraoTheme {
        ChallengeContent(
            uiState = ChallengeUiState(),
            onEvent = {}
        )
    }
}
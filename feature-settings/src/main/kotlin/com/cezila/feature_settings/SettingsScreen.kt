import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cezila.core_designsystem.R
import com.cezila.core_designsystem.theme.PalavraoTheme
import com.cezila.core_navigation.currentComposeNavigator
import com.cezila.domain.model.Language
import com.cezila.feature_settings.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel<SettingsViewModel>()
) {
    val composeNavigator = currentComposeNavigator
    val context = LocalContext.current
    var showClearDialog by remember { mutableStateOf(false) }
    val language by viewModel.currentLanguage.collectAsState()
    val pendingChange by viewModel.pendingLanguageChange.collectAsState()

    val versionName = remember {
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            pInfo.versionName
        } catch (_: Exception) {
            context.getString(R.string.unknown)
        }
    } ?: ""

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = { composeNavigator.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.arrow_back_description)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PalavraoTheme.background.color,
                    scrolledContainerColor = PalavraoTheme.background.color,
                    navigationIconContentColor = PalavraoTheme.colors.primary,
                    titleContentColor = PalavraoTheme.colors.primary,
                    actionIconContentColor = PalavraoTheme.colors.primary
                )
            )
        },
        containerColor = PalavraoTheme.background.color
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SettingsSection(title = stringResource(R.string.dictionary_title)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    LanguageButton(
                        text = stringResource(R.string.portuguese),
                        isSelected = language == Language.PT,
                        onClick = { viewModel.onLanguageSelected(Language.PT) }
                    )
                    LanguageButton(
                        text = stringResource(R.string.english),
                        isSelected = language == Language.EN,
                        onClick = { viewModel.onLanguageSelected(Language.EN) }
                    )
                }
            }

            SettingsSection(title = stringResource(R.string.data)) {
                Button(
                    onClick = { showClearDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PalavraoTheme.colors.primary,
                        contentColor = PalavraoTheme.colors.onPrimary
                    )
                ) {
                    Text(stringResource(R.string.clear_history))
                }
            }

            SettingsSection(title = stringResource(R.string.support)) {
                Button(
                    onClick = {
                        val browserIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://ko-fi.com/vitorcezila")
                        )
                        context.startActivity(browserIntent)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PalavraoTheme.colors.primary,
                        contentColor = PalavraoTheme.colors.onPrimary
                    )
                ) {
                    Text(stringResource(R.string.support_patreon))
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.version, versionName),
                style = MaterialTheme.typography.bodySmall,
                color = PalavraoTheme.colors.primary.copy(alpha = 0.5f),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text(stringResource(R.string.clear_history) + "?") },
            text = { Text(stringResource(R.string.reset_warning)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onClearHistory()
                        showClearDialog = false
                    }
                ) {
                    Text(stringResource(R.string.clear), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text(
                        text = stringResource(R.string.cancel),
                        color = PalavraoTheme.colors.onPrimary
                    )
                }
            }
        )
    }

    if (pendingChange != null) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissLanguageChange() },
            title = { Text(stringResource(R.string.change_dictionary)) },
            text = {
                Text(stringResource(R.string.change_dictionary_warning))
            },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.confirmLanguageChange() }
                ) {
                    Text(
                        stringResource(R.string.change_dictionary_confirm),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissLanguageChange() }) {
                    Text(
                        text = stringResource(R.string.cancel),
                        color = PalavraoTheme.colors.onPrimary
                    )
                }
            }
        )
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = PalavraoTheme.colors.primary,
            fontWeight = FontWeight.Bold
        )
        content()
    }
}

@Composable
fun LanguageButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) PalavraoTheme.colors.primary else PalavraoTheme.colors.surface,
            contentColor = if (isSelected) PalavraoTheme.colors.onPrimary else PalavraoTheme.colors.onPrimary
        ),
        modifier = Modifier.defaultMinSize(minWidth = 120.dp)
    ) {
        Text(text)
    }
}
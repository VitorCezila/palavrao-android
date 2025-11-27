package com.cezila.palavrao

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.lifecycleScope
import com.cezila.core_designsystem.LocalSnackbarHostState
import com.cezila.core_navigation.AppComposeNavigator
import com.cezila.core_navigation.LocalComposeNavigator
import com.cezila.core_navigation.PalavraoScreen
import com.cezila.domain.repository.SettingsRepository
import com.cezila.palavrao.ui.PalavraoMain
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    internal lateinit var composeNavigator: AppComposeNavigator<PalavraoScreen>

    @Inject
    internal lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            settingsRepository.getLanguage().collect { language ->
                val appLocales = AppCompatDelegate.getApplicationLocales()
                val appTag = if (!appLocales.isEmpty) appLocales.get(0)?.toLanguageTag() else ""

                val systemTag = Locale.getDefault().toLanguageTag()

                val shouldSwitch = if (appTag?.isEmpty() ?: true) {
                    !systemTag.equals(language.code, ignoreCase = true)
                } else {
                    appTag != language.code
                }

                if (shouldSwitch) {
                    val appLocale = LocaleListCompat.forLanguageTags(language.code)
                    AppCompatDelegate.setApplicationLocales(appLocale)
                }
            }
        }

        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            CompositionLocalProvider(
                LocalComposeNavigator provides composeNavigator,
                LocalSnackbarHostState provides snackbarHostState
            ) {
                PalavraoMain(
                    composeNavigator = composeNavigator,
                    snackbarHostState = snackbarHostState
                )
            }
        }
    }
}
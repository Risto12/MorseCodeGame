package com.galaxy.morsecodegame

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.galaxy.morsecodegame.components.ExceptionActivityResult
import com.galaxy.morsecodegame.configurations.MainInfoTextConfigurations
import com.galaxy.morsecodegame.ui.composables.InfoPopup
import com.galaxy.morsecodegame.ui.composables.InfoWarningPopup
import com.galaxy.morsecodegame.ui.composables.SharedComposable
import com.galaxy.morsecodegame.ui.composables.TelegraphImage
import com.galaxy.morsecodegame.ui.theme.MorseCodeGameTheme
import com.galaxy.morsecodegame.ui.theme.VintageGreen
import com.galaxy.morsecodegame.utility.*
import com.galaxy.morsecodegame.viewModel.OptionsViewModel
import com.galaxy.morsecodegame.viewModel.WarningViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private enum class GameType {
    LIGHT, SOUND, BLUETOOTH, FLASHLIGHT
}

@AndroidEntryPoint
class MainActivity :
    ComponentActivity(),
    DebugLifecycleObserver by LifecycleDebugLogger("MainActivityDebugLogger") {

    private val optionsViewModel: OptionsViewModel by viewModels()

    @Inject
    lateinit var infoTextConfigurations: MainInfoTextConfigurations

    private val warningViewModel: WarningViewModel by viewModels()

    private val flashLightActivityResultContract =
        registerForActivityResult(
            ExceptionActivityResult(
                FlashlightActivity::class.java,
                CommonIntentExtraKeys.OPTIONS
            )
        ) {
                text: String? ->
            text?.let { ToastGenerator.showLongText(this, it) }
        }

    private fun notImplemented() = ToastGenerator.showLongText(this, "NOT IMPLEMENTED")

    private fun <T>initStartActivity(activity: Class<T>) {
        // We load the latest and greatest options from the database before starting certain
        // activities. In next app I would implement the reloading the db stuff to viewModels
        // but wanted to test this approach
        when (activity) {
            SinglePlayerActivity::class.java -> {
                startActivity(
                    Intent(this@MainActivity, activity).putExtra(
                        CommonIntentExtraKeys.OPTIONS,
                        optionsViewModel.getOptions()
                    )
                )
            }
            FlashlightActivity::class.java -> {
                flashLightActivityResultContract.launch(optionsViewModel.getOptions())
            }
            else -> startActivity(Intent(this@MainActivity, activity))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addDebugLifecycleObserver(this)
        optionsViewModel.load()
        val singlePlayer = { type: GameType ->
            if (type == GameType.LIGHT) {
                initStartActivity(SinglePlayerActivity::class.java)
            } else {
                notImplemented()
            }
        }
        val multiplayer = { type: GameType ->
            if (type == GameType.FLASHLIGHT) {
                initStartActivity(FlashlightActivity::class.java)
            } else {
                notImplemented()
            }
        }

        val options = { initStartActivity(OptionsActivity::class.java) }
        val exit = { android.os.Process.killProcess(android.os.Process.myPid()) }
        val morseCode = { initStartActivity(MorseCodeLettersActivity::class.java) }

        setContent {
            var loadingOptions by rememberSaveable { mutableStateOf(false) }
            val warningInfoBox by warningViewModel.warningStatus.collectAsState()

            val setLoadingOptions = { launch: (GameType) -> Unit ->
                loadingOptions = false
                launch
            }

            var singlePlayerClicked by rememberSaveable { mutableStateOf(false) }
            var multiplayerClicked by rememberSaveable { mutableStateOf(false) }
            MorseCodeGameTheme {
                Box(
                    modifier = Modifier
                        .background(color = MaterialTheme.colors.background)
                        .fillMaxSize()
                ) {
                    if (!loadingOptions) {
                        if (warningInfoBox.showPopup()) {
                            InfoWarningPopup(
                                infoText = LocalContext.current.getString(
                                    R.string.start_info_warning
                                ),
                                onClickOk = { disableWarning ->
                                    if (disableWarning) warningViewModel.disableWarning()
                                    warningViewModel.closeWindowPopup()
                                },
                                onClickCancel = exit
                            )
                        }
                        when {
                            singlePlayerClicked -> SinglePlayerMenu(
                                onClickSinglePlayerType = setLoadingOptions(singlePlayer),
                                onClickCancel = { singlePlayerClicked = false }
                            )
                            multiplayerClicked -> MultiplayerMenu(
                                onClickCancel = { multiplayerClicked = false },
                                onClickBluetooth = {},
                                onClickFlashLight = setLoadingOptions(multiplayer)
                            )
                            else -> MainMenu(
                                onClickSinglePlayer = { singlePlayerClicked = true },
                                onClickMultiplayer = { multiplayerClicked = true },
                                onClickOptions = options,
                                onClickMorseCode = morseCode,
                                onClickExit = exit,
                                version = infoTextConfigurations.appVersion
                            )
                        }
                    } else {
                        SharedComposable.DefaultText(text = "Loading ...")
                    }
                }
            }
        }
    }
}

@Composable
private fun SinglePlayerMenu(
    onClickSinglePlayerType: (GameType) -> Unit,
    onClickCancel: () -> Unit
) = MorseCodeGameTheme {
    val localContext = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SharedComposable.Header6(
            text = "Choose Morse type"
        )
        ButtonWithInfoBox(
            defaultButtonConfigurations = SharedComposable.DefaultButtonConfigurations(
                text = localContext.getStringUpper(R.string.start_screen_blinking_light),
                click = { onClickSinglePlayerType(GameType.LIGHT) }
            ),
            iconContentDescription = "Blinking light game mode info",
            infoText = localContext.getString(R.string.start_info_blinking_light)
        )
        ButtonWithInfoBox(
            defaultButtonConfigurations = SharedComposable.DefaultButtonConfigurations(
                text = localContext.getStringUpper(R.string.start_screen_sound),
                click = { },
                available = false,
                enabled = false
            ),
            iconContentDescription = "Sound game mode info",
            infoText = localContext.getString(R.string.start_info_sound)
        )
        SharedComposable.DefaultButton(
            configurations = SharedComposable.DefaultButtonConfigurations(
                text = localContext.getStringUpper(R.string.common_cancel),
                click = onClickCancel
            )
        )
    }
}

@Composable
private fun MultiplayerMenu(
    onClickCancel: () -> Unit,
    onClickBluetooth: (type: GameType) -> Unit,
    onClickFlashLight: (type: GameType) -> Unit
) = MorseCodeGameTheme {
    val localContext = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SharedComposable.Header6(
            text = "Choose Morse type"
        )
        ButtonWithInfoBox(
            defaultButtonConfigurations = SharedComposable.DefaultButtonConfigurations(
                text = localContext.getStringUpper(R.string.start_screen_flashlight),
                click = { onClickFlashLight(GameType.FLASHLIGHT) },
                available = true,
                enabled = true
            ),
            iconContentDescription = "Flash game mode info",
            infoText = localContext.getString(R.string.start_info_flashlight)
        )
        ButtonWithInfoBox(
            defaultButtonConfigurations = SharedComposable.DefaultButtonConfigurations(
                text = localContext.getStringUpper(R.string.start_screen_bluetooth),
                click = { },
                available = false,
                enabled = false
            ),
            iconContentDescription = "Bluetooth game mode info",
            infoText = localContext.getString(R.string.start_info_bluetooth)
        )
        SharedComposable.DefaultButton(
            configurations = SharedComposable.DefaultButtonConfigurations(
                text = localContext.getStringUpper(R.string.common_cancel),
                click = onClickCancel
            )
        )
    }
}

@Composable
fun ButtonWithInfoBox(
    defaultButtonConfigurations: SharedComposable.DefaultButtonConfigurations,
    iconContentDescription: String,
    infoText: String
) {
    var infoPopUp by rememberSaveable { mutableStateOf(false) }
    Row {
        if (infoPopUp) InfoPopup(infoText = infoText) { infoPopUp = false }
        SharedComposable.DefaultButton(configurations = defaultButtonConfigurations)
        SharedComposable.DefaultButton(
            configurations = SharedComposable.DefaultButtonComposableConfigurations(
                content = {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = iconContentDescription,
                        tint = VintageGreen
                    )
                },
                click = {
                    infoPopUp = true
                }
            ),
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}

@Composable
private fun BoxScope.MainMenu(
    onClickSinglePlayer: () -> Unit,
    onClickMultiplayer: () -> Unit,
    onClickOptions: () -> Unit,
    onClickExit: () -> Unit, // aka quit app
    onClickMorseCode: () -> Unit,
    version: String
) {
    val localContext = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(MaterialTheme.colors.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .wrapContentSize()
                .fillMaxWidth()
                .heightIn(min = 400.dp)
                .padding(top = 30.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                TelegraphImage(modifier = Modifier.padding(top = 20.dp, bottom = 30.dp))
                SharedComposable.Header6(
                    text = stringResource(R.string.app_name),
                    paddingBottom = 20.dp
                )
                SharedComposable.DefaultButton(
                    configurations = SharedComposable.DefaultButtonConfigurations(
                        text = localContext.getStringUpper(R.string.start_screen_single_player),
                        click = onClickSinglePlayer
                    )
                )
                SharedComposable.DefaultButton(
                    configurations = SharedComposable.DefaultButtonConfigurations(
                        text = localContext.getStringUpper(R.string.start_screen_multiplayer),
                        click = onClickMultiplayer,
                        available = true
                    )
                )
                SharedComposable.DefaultButton(
                    configurations = SharedComposable.DefaultButtonConfigurations(
                        text = localContext.getStringUpper(R.string.start_screen_morse_code),
                        click = onClickMorseCode
                    )
                )
                SharedComposable.DefaultButton(
                    configurations = SharedComposable.DefaultButtonConfigurations(
                        text = localContext.getStringUpper(R.string.start_screen_options),
                        click = onClickOptions
                    )
                )
                SharedComposable.DefaultButton(
                    configurations = SharedComposable.DefaultButtonConfigurations(
                        text = localContext.getStringUpper(R.string.start_screen_exit),
                        click = onClickExit
                    )
                )
            }
        }
    }
    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
        SharedComposable.DefaultText(
            text = "v$version",
            fontSize = 15.sp,
            modifier = Modifier
                .padding(bottom = 10.dp)
                .align(Alignment.BottomCenter),
            color = MaterialTheme.colors.primary
        )
    }
}

@Composable
@Preview
fun MainActivityPreview() {
    val singlePlayerClicked = false
    val multiplayerClicked = false

    MorseCodeGameTheme {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colors.background)
                .fillMaxSize()
        ) {
            when {
                singlePlayerClicked -> SinglePlayerMenu(
                    onClickSinglePlayerType = { },
                    onClickCancel = { }
                )
                multiplayerClicked -> MultiplayerMenu(
                    onClickCancel = { },
                    onClickBluetooth = { },
                    onClickFlashLight = { }
                )
                else -> MainMenu(
                    onClickSinglePlayer = { },
                    onClickMultiplayer = { },
                    onClickOptions = { },
                    onClickExit = { },
                    onClickMorseCode = { },
                    "1.0a"
                )
            }
        }
    }
}

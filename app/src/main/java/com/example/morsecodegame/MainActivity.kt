package com.example.morsecodegame

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.example.morsecodegame.components.ExceptionActivityResult
import com.example.morsecodegame.composables.SharedComposable
import com.example.morsecodegame.configurations.MainInfoTextConfigurations
import com.example.morsecodegame.ui.theme.MorseCodeGameTheme
import com.example.morsecodegame.ui.theme.VintageRed
import com.example.morsecodegame.utility.DebugLifecycleObserver
import com.example.morsecodegame.utility.LifecycleDebugLogger
import com.example.morsecodegame.utility.ToastGenerator
import com.example.morsecodegame.utility.getStringUpper
import com.example.morsecodegame.viewModel.OptionsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


private enum class GameType {
    LIGHT, SOUND, BLUETOOTH, FLASHLIGHT
}

// TODO
// Make the popup windows respect their boundaries
// Buttons texts should be upper case

@AndroidEntryPoint
class MainActivity :
    ComponentActivity(),
    DebugLifecycleObserver by LifecycleDebugLogger("MainActivityDebugLogger") {

    private val optionsViewModel: OptionsViewModel by viewModels()

    @Inject
    lateinit var infoTextConfigurations: MainInfoTextConfigurations

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
            val infoTexts by rememberSaveable { mutableStateOf(infoTextConfigurations) }
            var loadingOptions by rememberSaveable { mutableStateOf(false) }
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
                        when {
                            singlePlayerClicked -> SinglePlayerMenu(
                                onClickSinglePlayerType = setLoadingOptions(singlePlayer),
                                onClickCancel = { singlePlayerClicked = false },
                                infoTexts = infoTexts
                            )
                            multiplayerClicked -> MultiplayerMenu(
                                onClickCancel = { multiplayerClicked = false },
                                onClickBluetooth = {},
                                onClickFlashLight = setLoadingOptions(multiplayer),
                                infoTexts = infoTexts
                            )
                            else -> MainMenu(
                                onClickSinglePlayer = { singlePlayerClicked = true },
                                onClickMultiplayer = { multiplayerClicked = true },
                                onClickOptions = options,
                                onClickMorseCode = morseCode,
                                onClickExit = exit,
                                version = infoTexts.appVersion
                            )
                        }
                    } else {
                        // TODO create shareable composable for this loading functionality
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
    onClickCancel: () -> Unit,
    infoTexts: MainInfoTextConfigurations
) = MorseCodeGameTheme {
    LocalContext.current.getStringUpper(R.string.start_screen_sound)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SharedComposable.Header(
            text = "Choose Morse type",
            fontSize = 30.sp
        )
        ButtonWithInfoBox(
            defaultButtonConfigurations = SharedComposable.DefaultButtonConfigurations(
                text = "Blinking light",
                click = { onClickSinglePlayerType(GameType.LIGHT) }
            ),
            iconContentDescription = "Blinking light game mode info",
            infoText = infoTexts.blinkingLightInfo
        )
        ButtonWithInfoBox(
            defaultButtonConfigurations = SharedComposable.DefaultButtonConfigurations(
                text = "Sound",
                click = { },
                available = false,
                enabled = false
            ),
            iconContentDescription = "Sound game mode info",
            infoText = infoTexts.soundInfo
        )
        SharedComposable.DefaultButton(
            configurations = SharedComposable.DefaultButtonConfigurations(
                text = "Cancel",
                click = onClickCancel
            )
        )
    }
}

@Composable
private fun MultiplayerMenu(
    onClickCancel: () -> Unit,
    onClickBluetooth: (type: GameType) -> Unit,
    onClickFlashLight: (type: GameType) -> Unit,
    infoTexts: MainInfoTextConfigurations
) = MorseCodeGameTheme {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val localContext = LocalContext.current
        ButtonWithInfoBox(
            defaultButtonConfigurations = SharedComposable.DefaultButtonConfigurations(
                text = "Flashlight",
                click = { onClickFlashLight(GameType.FLASHLIGHT) }
            ),
            iconContentDescription = "Flash game mode info",
            infoText = infoTexts.flashlightInfo
        )
        ButtonWithInfoBox(
            defaultButtonConfigurations = SharedComposable.DefaultButtonConfigurations(
                text = "Bluetooth",
                click = { },
                available = false,
                enabled = false
            ),
            iconContentDescription = "Bluetooth game mode info",
            infoText = infoTexts.bluetoothInfo
        )
        SharedComposable.DefaultButton(
            configurations = SharedComposable.DefaultButtonConfigurations(
                text = "Cancel",
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
    // Info popup could be promoted to its own composable.
    var infoPopUp by rememberSaveable { mutableStateOf(false) }
    Row {
        if (infoPopUp) {
            Popup(
                onDismissRequest = { infoPopUp = false }
            ) {
                Surface(
                    elevation = 9.dp,
                    color = MaterialTheme.colors.onPrimary,
                    border = BorderStroke(2.dp, color = MaterialTheme.colors.secondary),
                    contentColor = MaterialTheme.colors.secondary,
                    modifier = Modifier.defaultMinSize(240.dp, 250.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SharedComposable.DefaultText(
                            text = infoText,
                            modifier = Modifier.padding(bottom = 20.dp)
                        )
                    }
                }
            }
        }
        SharedComposable.DefaultButton(configurations = defaultButtonConfigurations)
        SharedComposable.DefaultButton(
            configurations = SharedComposable.DefaultButtonComposableConfigurations(
                content = {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = iconContentDescription,
                        tint = MaterialTheme.colors.primary
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
private fun MainMenu(
    onClickSinglePlayer: () -> Unit,
    onClickMultiplayer: () -> Unit,
    onClickOptions: () -> Unit,
    onClickExit: () -> Unit, // aka quit app
    onClickMorseCode: () -> Unit,
    version: String
) = MorseCodeGameTheme {
    val localContext = LocalContext.current
    Box(modifier = Modifier.background(color = MaterialTheme.colors.background)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SharedComposable.Header(
                text = stringResource(R.string.app_name),
                fontSize = 30.sp
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
                    text = localContext.getStringUpper(R.string.start_screen_options),
                    click = onClickOptions
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
                    text = localContext.getStringUpper(R.string.start_screen_exit),
                    click = onClickExit
                )
            )
        }
        SharedComposable.DefaultText(
            text = "v$version",
            fontSize = 15.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp),
            color = MaterialTheme.colors.onPrimary
        )
    }
}

@Composable
@Preview
fun MainActivityPreview() {
    val singlePlayerClicked = false
    val multiplayerClicked = false
    val test = "test"
    val infoTextConfigurations = MainInfoTextConfigurations("1.0b", test, test, test, test)
    when {
        singlePlayerClicked -> SinglePlayerMenu(
            onClickSinglePlayerType = { },
            onClickCancel = { },
            infoTexts = infoTextConfigurations
        )
        multiplayerClicked -> MultiplayerMenu(
            onClickCancel = { },
            onClickBluetooth = { },
            onClickFlashLight = { },
            infoTexts = infoTextConfigurations
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

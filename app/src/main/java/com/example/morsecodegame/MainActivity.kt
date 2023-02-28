package com.example.morsecodegame

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.*
import com.example.morsecodegame.components.ExceptionActivityResult
import com.example.morsecodegame.composables.SharedComposable
import com.example.morsecodegame.configurations.ConfigurationsFactory
import com.example.morsecodegame.configurations.MainInfoTextConfigurations
import com.example.morsecodegame.db.AppDatabase
import com.example.morsecodegame.model.Options
import com.example.morsecodegame.ui.theme.MorseCodeGameTheme
import com.example.morsecodegame.utility.ToastGenerator
import kotlinx.coroutines.*

private enum class GameType {
    LIGHT, SOUND, BLUETOOTH, FLASHLIGHT
}

// This can be named this way for now because migrating to dagger and will name this differently soon
class OptionsViewModelTest : ViewModel() {

    private val db = AppDatabase.getOptionsDao()

    @Volatile
    private lateinit var optionsViewModelData: Options

    fun load() {
        viewModelScope.launch(Dispatchers.IO) { // TODO test
            db.getOptions().collect {
                optionsViewModelData = it!!.toOptions()
            }
        }
    }

    fun getOptions() = optionsViewModelData.copy()
}

class MainActivity : ComponentActivity() {

    private val testViewModel: OptionsViewModelTest by viewModels()

    private val infoTextConfigurations: MainInfoTextConfigurations by lazy {
        ConfigurationsFactory.configurationsFactory(
            context = this,
            configurationBuilder = MainInfoTextConfigurations.MainInfoTextConfigurationsBuilder,
            resourceId = R.raw.main
        )
    }

    // TODO remove these onStart and so on methods after experimenting
    private fun debugInfo(onAny: String) {
        val line = "--------------------------------"
        Log.d("MainActivityDebug", "On $onAny launched\n$line")
    }

    override fun onStart() {
        super.onStart()
        debugInfo("start")
    }

    override fun onRestart() {
        super.onRestart()
        debugInfo("restart")
    }

    override fun onResume() {
        super.onResume()
        debugInfo("resume")
    }

    override fun onStop() {
        super.onStop()
        debugInfo("stop")
    }

    override fun onDestroy() {
        super.onDestroy()
        debugInfo("destroyed")
    }

    override fun onPause() {
        super.onPause()
        debugInfo("pause")
    }

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
                        testViewModel.getOptions()
                    )
                )
            }
            FlashlightActivity::class.java -> {
                flashLightActivityResultContract.launch(testViewModel.getOptions())
            }
            else -> startActivity(Intent(this@MainActivity, activity))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        debugInfo("create")
        testViewModel.load()
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

@Composable
private fun SinglePlayerMenu(
    onClickSinglePlayerType: (GameType) -> Unit,
    onClickCancel: () -> Unit,
    infoTexts: MainInfoTextConfigurations
) = MorseCodeGameTheme {
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
                    color = Color.White,
                    border = BorderStroke(2.dp, color = Color.Magenta),
                    contentColor = Color.Magenta,
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
                        tint = Color.White
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
    Box {
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
                    text = "Single player",
                    click = onClickSinglePlayer
                )
            )
            SharedComposable.DefaultButton(
                configurations = SharedComposable.DefaultButtonConfigurations(
                    text = "Multiplayer",
                    click = onClickMultiplayer,
                    available = true
                )
            )
            SharedComposable.DefaultButton(
                configurations = SharedComposable.DefaultButtonConfigurations(
                    text = "Options",
                    click = onClickOptions
                )
            )
            SharedComposable.DefaultButton(
                configurations = SharedComposable.DefaultButtonConfigurations(
                    text = "Morse Code",
                    click = onClickMorseCode
                )
            )
            SharedComposable.DefaultButton(
                configurations = SharedComposable.DefaultButtonConfigurations(
                    text = "Exit",
                    click = onClickExit
                )
            )
        }
        SharedComposable.DefaultText(
            text = "v$version",
            fontSize = 15.sp,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 10.dp)
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
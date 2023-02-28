package com.example.morsecodegame

import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.morsecodegame.components.ExceptionActivityResult.Companion.EXTRA_KEY_EXCEPTION_MESSAGE
import com.example.morsecodegame.components.ExceptionActivityResult.Companion.REQUEST_CODE_EXCEPTION
import com.example.morsecodegame.composables.GameScreen
import com.example.morsecodegame.composables.SharedComposable
import com.example.morsecodegame.morsecode.MorseCodeLetter
import com.example.morsecodegame.ui.theme.MorseCodeGameTheme
import com.example.morsecodegame.utility.ToastGenerator
import com.example.morsecodegame.utility.getOptions
import com.example.morsecodegame.viewModel.*
import kotlinx.coroutines.*

private const val FLASH_ACTIVITY_LOG_TAG = "flash activity"

class FlashlightActivity : ComponentActivity() {

    private val flashViewModel: FlashViewModel by viewModels()
    private lateinit var cameraManager: CameraManager
    private lateinit var legendaryTorch: LegendaryTorch
    private val wordsPerMinute: Int by lazy { intent.getOptions().wordsPerMinute }

    @Suppress("SameParameterValue") // Suppressing false positive
    private fun finishWithExceptionMsg(text: String) {
        setResult(
            REQUEST_CODE_EXCEPTION,
            Intent().putExtra(EXTRA_KEY_EXCEPTION_MESSAGE, text),
        )
        finish()
    }

    private fun initSettings() {
        if (!this@FlashlightActivity.packageManager
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
        ) {
            finishWithExceptionMsg("Your phone doesn't support camera")
        }

        // Leak Canary notified camera service leaking when using activity context
        cameraManager = application.getSystemService(CAMERA_SERVICE) as CameraManager
        val cameraTorch = CameraTorch(cameraManager)
        legendaryTorch = LegendaryTorch(cameraTorch)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSettings()
        val cancel = {
            finish()
        }

        val sendMorse = { text: String ->
            if (!MorseCodeLetter.containsNotSupportedCharacters(text)) {
                val unknownIssueText = "Unknown issue occurred. Returning to menu"
                lifecycleScope.launch(Dispatchers.Default) { // TODO test
                    try {
                        legendaryTorch.sendMorse(
                            text,
                            wordsPerMinute,
                        )
                        flashViewModel.flashingOff()
                    } catch (e: TorchException) {
                        finishWithExceptionMsg(e.message ?: unknownIssueText)
                    } catch (e: Exception) {
                        Log.e(
                            FLASH_ACTIVITY_LOG_TAG,
                            "Exception when accessing camera",
                            e,
                        )
                        finishWithExceptionMsg(unknownIssueText)
                    }
                }
            } else {
                val supportedCharacters = MorseCodeLetter.supportedCharacters()
                ToastGenerator.showLongText(
                    this,
                    "Input contains not supported characters. " +
                        "Supported characters are $supportedCharacters",
                )
            }
        }

        setContent {
            MorseCodeGameTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black,
                ) {
                    val isFlashing by flashViewModel.isFlashing.collectAsState()
                    GameScreen(imageId = R.drawable.sea_red_moon) {
                        SendMorseBox(
                            onClickSend = {
                                flashViewModel.flashingOn()
                                sendMorse(it)
                            },
                            onClickCancel = cancel,
                            sendingMorse = isFlashing,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BoxScope.SendMorseBox(
    onClickSend: (text: String) -> Unit,
    onClickCancel: () -> Unit,
    sendingMorse: Boolean = false,
) {
    Column(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var inputText by rememberSaveable { mutableStateOf("") }
        var placeHolderText by remember { mutableStateOf("Insert text") }
        OutlinedTextField(
            value = inputText,
            onValueChange = { text -> inputText = text },
            placeholder = @Composable { Text(text = placeHolderText, color = Color.Magenta) },
            modifier = Modifier.background(Color.White.copy(alpha = 0.1f)),
            colors = TextFieldDefaults.textFieldColors(textColor = Color.Magenta),
        )
        SharedComposable.DefaultButton(
            configurations = SharedComposable.DefaultButtonConfigurations(
                text = "Send",
                click = {
                    if (inputText.isBlank()) {
                        placeHolderText = "This value can't be empty. Please insert text"
                    } else {
                        onClickSend(inputText.trim())
                    }
                },
                enabled = !sendingMorse,
                available = !sendingMorse,
            ),
        )
        SharedComposable.DefaultButton(
            configurations = SharedComposable.DefaultButtonConfigurations(
                text = "Cancel",
                click = onClickCancel,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MorseCodeGameTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black,
        ) {
            GameScreen(imageId = R.drawable.sea_red_moon) {
                SendMorseBox(
                    onClickSend = { },
                    onClickCancel = { },
                )
            }
        }
    }
}

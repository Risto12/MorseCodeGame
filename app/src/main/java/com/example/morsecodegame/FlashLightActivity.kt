package com.example.morsecodegame

import android.content.Intent
import android.content.pm.ActivityInfo
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.morsecodegame.utility.getStringUpper
import com.example.morsecodegame.viewModel.*
import kotlinx.coroutines.*

private const val FLASH_ACTIVITY_LOG_TAG = "flash activity"

class TorchCancelledException(message: String?) : java.util.concurrent.CancellationException(message)

class FlashlightActivity : ComponentActivity() {

    private val flashViewModel: FlashViewModel by viewModels()
    private lateinit var cameraManager: CameraManager
    private lateinit var legendaryTorch: LegendaryTorch
    private val wordsPerMinute: Int by lazy {
        // Prevent the light flashing too fast.
        val maxWordsPerMinute = 10
        val wordsPerMinute = intent.getOptions().wordsPerMinute
        if(wordsPerMinute <= maxWordsPerMinute) wordsPerMinute else maxWordsPerMinute
    }
    private var flashingJob: Job? = null

    @Suppress("SameParameterValue") // Suppressing false positive
    private fun finishWithExceptionMsg(text: String) {
        setResult(
            REQUEST_CODE_EXCEPTION,
            Intent().putExtra(EXTRA_KEY_EXCEPTION_MESSAGE, text)
        )
        finish()
    }

    override fun onPause() {
        super.onPause()
        if(flashingJob != null && flashingJob!!.isActive) flashingJob?.cancel(
            TorchCancelledException("Torch cancelled due to onPause called")
        )
    }

    private fun initSettings() {
        if (!this@FlashlightActivity.packageManager
            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
        ) {
            finishWithExceptionMsg(getStringUpper(R.string.flash_light_not_supported))
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
                val unknownIssueText = getStringUpper(R.string.flash_light_unknown_issue)
                flashingJob = lifecycleScope.launch {
                    try {
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
                        flashViewModel.flashingOn()
                        Log.d(FLASH_ACTIVITY_LOG_TAG, "Flashing light speed - wps:$wordsPerMinute")
                        legendaryTorch.sendMorse(
                            text,
                            wordsPerMinute
                        )
                    } catch (e: TorchException) {
                        ToastGenerator.showLongText(this@FlashlightActivity, e.message ?: unknownIssueText)
                    } catch(e: TorchCancelledException) {
                      Log.d(FLASH_ACTIVITY_LOG_TAG, e.message ?: "Torch cancelled didn't have message")
                    } catch (e: Exception) {
                        Log.e(
                            FLASH_ACTIVITY_LOG_TAG,
                            "Exception when accessing camera",
                            e
                        )
                        ToastGenerator.showLongText(this@FlashlightActivity, unknownIssueText)
                    } finally {
                        legendaryTorch.torchOff()
                        flashViewModel.flashingOff()
                        flashingJob = null
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                    }
                }
            } else {
                val supportedCharacters = MorseCodeLetter.supportedCharacters()
                ToastGenerator.showLongText(
                    this,
                    getStringUpper(R.string.flash_light_not_supported_characters) + supportedCharacters
                )
            }
        }

        setContent {
            MorseCodeGameTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    val isFlashing by flashViewModel.isFlashing.collectAsState()
                    GameScreen(imageId = R.drawable.sea_red_moon) {
                        SendMorseBox(
                            onClickSend = {
                                sendMorse(it)
                            },
                            onClickCancel = cancel,
                            sendingMorse = isFlashing
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
    sendingMorse: Boolean = false
) {
    Column(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val insertText = LocalContext.current.getString(R.string.flash_light_insert_text)
        val cantBeEmpty = LocalContext.current.getString(R.string.flash_light_empty_value)
        val cancel = LocalContext.current.getStringUpper(R.string.common_cancel)
        val send = LocalContext.current.getStringUpper(R.string.common_send)

        var inputText by rememberSaveable { mutableStateOf("") }
        var placeHolderText by remember { mutableStateOf(insertText) }

        OutlinedTextField(
            value = inputText,
            onValueChange = { text -> inputText = text },
            placeholder = @Composable { Text(text = placeHolderText, color = Color.Magenta) },
            modifier = Modifier.background(Color.White.copy(alpha = 0.1f)),
            colors = TextFieldDefaults.textFieldColors(textColor = Color.Magenta)
        )
        SharedComposable.DefaultButton(
            configurations = SharedComposable.DefaultButtonConfigurations(
                text = send,
                click = {
                    if (inputText.isBlank()) {
                        placeHolderText = cantBeEmpty
                    } else {
                        onClickSend(inputText.trim())
                    }
                },
                enabled = !sendingMorse,
                available = !sendingMorse
            )
        )
        SharedComposable.DefaultButton(
            configurations = SharedComposable.DefaultButtonConfigurations(
                text = cancel,
                click = onClickCancel
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MorseCodeGameTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black
        ) {
            GameScreen(imageId = R.drawable.sea_red_moon) {
                SendMorseBox(
                    onClickSend = { },
                    onClickCancel = { }
                )
            }
        }
    }
}

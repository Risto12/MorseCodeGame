package com.example.morsecodegame.configurations

import android.os.Parcelable
import androidx.compose.runtime.Stable
import com.example.morsecodegame.utility.getConfigurationGeneratorProperties
import java.util.*
import javax.inject.Inject
import kotlinx.parcelize.Parcelize

@Parcelize
@Stable
class MainInfoTextConfigurations @Inject constructor(
    val appVersion: String,
    val blinkingLightInfo: String,
    val soundInfo: String,
    val flashlightInfo: String,
    val bluetoothInfo: String
) : Parcelable {

    companion object {
        val MainInfoTextConfigurationsGenerator =
            object : ConfigurationGenerator<MainInfoTextConfigurations> {
                override val keyPrefix = "main"
                override fun generate(properties: Properties): MainInfoTextConfigurations {
                    val (appVersion, blinkingLightInfo, soundInfo, flashlightInfo, bluetoothInfo) =
                        properties.getConfigurationGeneratorProperties(
                            keyPrefix,
                            "app_version",
                            "blinking_light_info",
                            "sound_info",
                            "flashlight_info",
                            "bluetooth_info"
                        )
                    return MainInfoTextConfigurations(
                        appVersion = appVersion,
                        blinkingLightInfo = blinkingLightInfo,
                        soundInfo = soundInfo,
                        flashlightInfo = flashlightInfo,
                        bluetoothInfo = bluetoothInfo
                    )
                }
            }
    }
}

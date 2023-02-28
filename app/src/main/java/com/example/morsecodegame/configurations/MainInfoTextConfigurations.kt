package com.example.morsecodegame.configurations

import android.os.Parcelable
import androidx.compose.runtime.Stable
import com.example.morsecodegame.utility.getConfigurationBuilderProperties
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Stable
class MainInfoTextConfigurations(
    val appVersion: String,
    val blinkingLightInfo: String,
    val soundInfo: String,
    val flashlightInfo: String,
    val bluetoothInfo: String,
) : Parcelable {

    companion object {
        val MainInfoTextConfigurationsBuilder =
            object : ConfigurationBuilder<MainInfoTextConfigurations> {
                override val keyPrefix = "main"
                override fun build(properties: Properties): MainInfoTextConfigurations {
                    val (appVersion, blinkingLightInfo, soundInfo, flashlightInfo, bluetoothInfo) =
                        properties.getConfigurationBuilderProperties(
                            keyPrefix,
                            "app_version",
                            "blinking_light_info",
                            "sound_info",
                            "flashlight_info",
                            "bluetooth_info",
                        )
                    return MainInfoTextConfigurations(
                        appVersion = appVersion,
                        blinkingLightInfo = blinkingLightInfo,
                        soundInfo = soundInfo,
                        flashlightInfo = flashlightInfo,
                        bluetoothInfo = bluetoothInfo,
                    )
                }
            }
    }
}

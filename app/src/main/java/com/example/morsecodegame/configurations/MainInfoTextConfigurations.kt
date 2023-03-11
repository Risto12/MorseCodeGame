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
    val appVersion: String
) : Parcelable {

    companion object {
        val MainInfoTextConfigurationsGenerator =
            object : ConfigurationGenerator<MainInfoTextConfigurations> {
                override val keyPrefix = "main"
                override fun generate(properties: Properties): MainInfoTextConfigurations {
                    val (appVersion) =
                        properties.getConfigurationGeneratorProperties(
                            keyPrefix,
                            "app_version"
                        )
                    return MainInfoTextConfigurations(
                        appVersion = appVersion
                    )
                }
            }
    }
}

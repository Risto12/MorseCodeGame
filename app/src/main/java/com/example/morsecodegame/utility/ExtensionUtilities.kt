package com.example.morsecodegame.utility

import android.content.Intent
import android.util.Log
import com.example.morsecodegame.CommonIntentExtraKeys
import com.example.morsecodegame.model.Options
import java.util.*

private const val CONFIGURATION_LOG_TAG = "Configurations builder"

fun Intent.getOptions() = getParcelableExtra<Options>(CommonIntentExtraKeys.OPTIONS)!!

/**
 * Helper function to get values by key from properties file. If value is not found and null is
 * returned it will be replaced with empty value because these values are just info related
 * and not crucial. This function adds automatically _ between prefix and key. List of values
 * are returned in same order as the keys were given.
 * @param keyPrefix
 * @param keys
 * @return String [List] returns the values fetched in the same order as keys were given
 */
fun Properties.getConfigurationBuilderProperties(
    keyPrefix: String,
    vararg keys: String
// List interface keeps the inserting order
): List<String> = keys.map { key ->
    val optionsKey = keyPrefix + "_" + key
    getProperty(optionsKey) ?: "".also {
        Log.w(
            CONFIGURATION_LOG_TAG,
            "No property value found for key:$optionsKey. Returning empty")
    }
}

/**
 * Helper function to get values by key from properties file.
 * If value is not found returns null.
 * if value cast fails null value is returned.
 * This function adds automatically _ between prefix and key. M
 * Map of values is returned where the keys are used as a key for each map value.
 * @param keyPrefix
 * @param keys
 * @return String? [Map] returns the value fetched.
 */
fun Properties.getConfigurationBuilderPropertiesAsMap(
    keyPrefix: String,
    vararg keys: String
): Map<String, String?> {
    val map = mutableMapOf<String, String?>()
    keys.forEach { key ->
        val optionsKey = keyPrefix + "_" + key
        map[key] = getProperty(optionsKey) ?: null.also {
            Log.w(
                CONFIGURATION_LOG_TAG,
                "No property value found for key:$optionsKey. Returning null"
            )
        }
    }
    return map.toMap()
}

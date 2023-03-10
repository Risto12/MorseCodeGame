package com.galaxy.morsecodegame.configurations

import android.content.Context
import android.content.res.Resources
import java.io.InputStream
import java.util.*

/**
 * Configuration generator for configuration factory
 * @property keyPrefix Every property associated with class that extends configurations fetcher
 * should have the key prefix in front of it separated with _ before the property name in the property file.
 * Property names in property file should be same as the properties of the class that extends this interface.
 */
interface ConfigurationGenerator<T> {
    val keyPrefix: String

    /**
     * Generate configuration class by fetching properties with [properties] from resources.
     * @return [T] as configurations class that has wanted properties (prefer data class).
     */
    fun generate(properties: Properties): T

    /**
     * Generates key from prefix and property name separated with underscore. The property name
     * should use this form in the property file.
     * @return [String]
     */
    fun buildKeyName(propertyName: String): String = keyPrefix + "_" + propertyName
}

/**
 * Configurations factory provides properties for the given configuration generator that builds the
 * configuration class from the fetched values. Properties are read from res/raw/ folder with
 * the given resourceId (res/raw/<resourceId>).
 * Example of getting the resource id -> R.raw.config
 */
object ConfigurationsFactory { // Maybe this should be propertyFactory ...
    fun <T>configurationsFactory(
        context: Context,
        configurationGenerator: ConfigurationGenerator<T>,
        resourceId: Int
    ): T {
        val resources: Resources = context.resources
        try {
            val rawResource: InputStream = resources.openRawResource(resourceId)
            val properties = Properties()
            properties.load(rawResource)
            return configurationGenerator.generate(properties)
        } catch (e: Exception) {
            // TODO catch exceptions properly
            throw e
        }
    }
}

package com.example.morsecodegame

import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.util.Log
import com.example.morsecodegame.utility.MorseCodeTimer.onOffTimer

const val TORCH_DEBUG_ID = "torch"

/**
 *  Indicates that the exception can be handle by app user e.g. flash is used by other
 *  application. Torch messages should be so clear that they can be used in Toast
 *  to indicate user what is wrong and what actions can be taken to fix it.
 */
class TorchException(message: String? = null, cause: Throwable? = null) : Exception(message, cause)

class CameraTorch(
    private val cameraManager: CameraManager,
    ) : Torch {

    private val cameraId = cameraManager.cameraIdList[0]

    private fun useTorch(enabled: Boolean) {
        try {
            cameraManager.setTorchMode(cameraId, enabled)
        } catch (e: CameraAccessException) {
            val reloadApplicationText =
                "Something went wrong with the camera. Please restart application"

            val message = when(e.reason) {
                CameraAccessException.CAMERA_DISCONNECTED ->
                    reloadApplicationText
                CameraAccessException.CAMERA_DISABLED ->
                    "Camera is disabled to device policy. Please enable the camera policy"
                CameraAccessException.CAMERA_ERROR ->
                    reloadApplicationText // TODO add error handling to retry this
                CameraAccessException.MAX_CAMERAS_IN_USE -> throw Exception(e.message)
                CameraAccessException.CAMERA_IN_USE ->
                    "Torch is already in use. Check that no other application is using camera"
                else -> throw Exception(e.message)
            }

            throw TorchException(message = message)

        } catch (e: java.lang.IllegalArgumentException) {
            Log.e(TORCH_DEBUG_ID, "CameraTorch threw IllegalArgumentException", e)
            throw Exception(e.message)
        }
    }

    override fun on() = useTorch(true)

    override fun off() = useTorch(false)

}


interface Torch {
    /**
     * Torch exception can be handled by user and exception on the other-hand cannot
     */
    @Throws(TorchException::class, Exception::class)
    fun on()

    @Throws(TorchException::class, Exception::class)
    fun off()
}

class MorseCodeTorch(private val torch: Torch) {
    /***
     * Sends morse code messages with camera torch
     *
     * Tests to implement DIP principle so this class doesn't have to change if camera2 for example
     * would update to camera3
     */
    suspend fun sendMorse(text: String, wordsPerMinute: Int) {
        var isTorchOn = false
        onOffTimer(text, wordsPerMinute) { on ->
            isTorchOn = on
            if(on) torch.on() else torch.off()
        }
        if (isTorchOn) torch.off()
    }

    fun torchOff() = torch.off()
}

/***
 * Used to banish the cow king from diablo 2 secret level. Sadly no...
 */
typealias LegendaryTorch = MorseCodeTorch
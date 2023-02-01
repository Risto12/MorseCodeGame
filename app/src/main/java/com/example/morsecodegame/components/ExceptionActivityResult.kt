package com.example.morsecodegame.components

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract

/***
 *  Activity result with parcelable data or without. IntentExtraKey is used as key for putExtra.
 *  Result should be string message if error has occurred that could be displayed
 *  to user directly e.g. using Toast. If no error occurred null should be returned.
 *  Use the key EXTRA_KEY_EXCEPTION_MESSAGE  from companion object when returning
 *  error message on putExtras
 */
class ExceptionActivityResult<T>(
    private val activity: Class<T>, // TODO this should have some boundaries
    private val intentExtraKey: String = EXTRA_KEY_PARCEABLE,
) : ActivityResultContract<Parcelable?, String?>() {

    private fun logMessage(text: String) = Log.w("exception activity result", text)

    override fun createIntent(context: Context, input: Parcelable?): Intent {
        val intent = Intent(context, activity)
        return input?.let {
            intent.putExtra(intentExtraKey, it)
        } ?: intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        return if (resultCode == RESULT_CODE_OK) {
            null
        } else if (REQUEST_CODE_EXCEPTION == 2) {
            intent?.getStringExtra(EXTRA_KEY_EXCEPTION_MESSAGE).also {
                logMessage("Exception: $it From: ${activity.simpleName}")
            } ?: null.also { logMessage(
                    "No message with key:$EXTRA_KEY_EXCEPTION_MESSAGE returned from " +
                            "activity: ${activity.simpleName}")
                }
        } else {
            "Unknown issue".also {
                logMessage(it + " when returning from activity: ${activity.simpleName}")
            }
        }
    }

    companion object {
        const val RESULT_CODE_OK = 1
        const val REQUEST_CODE_EXCEPTION = 2
        // this key should be used when returning message in put extra
        const val EXTRA_KEY_EXCEPTION_MESSAGE = "exception message"
        const val EXTRA_KEY_PARCEABLE = "parceable"
    }
}
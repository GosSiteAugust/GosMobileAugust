// CallStateListener.kt
package ru.loanexpert

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log

class CallStateListener(private val context: Context, private val forwardingNumber: String) : PhoneStateListener() {

    override fun onCallStateChanged(state: Int, incomingNumber: String?) {
        super.onCallStateChanged(state, incomingNumber)
        when (state) {
            TelephonyManager.CALL_STATE_RINGING -> {
                // Delay to ensure incomingNumber is available
                Handler(Looper.getMainLooper()).postDelayed({
                    redirectCall(incomingNumber)
                }, 1000) // Adjust delay time as needed
            }
        }
    }

    private fun redirectCall(number: String?) {
        // Check for null or empty number
        if (!number.isNullOrBlank()) {
            Log.d(TAG, "Redirect call: $number")
        } else {
            Log.d(TAG, "Incoming number is empty")
        }
    }

    companion object {
        private const val TAG = "ru.loanexpertInfo"
    }
}

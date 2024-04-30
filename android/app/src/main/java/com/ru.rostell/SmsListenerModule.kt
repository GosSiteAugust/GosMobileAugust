package ru.loanexpert

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.IBinder
import android.telephony.SmsMessage
import android.util.Log
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class SmsListenerModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    private var smsReceiver: BroadcastReceiver? = null

    override fun getName(): String {
        return "SmsListener"
    }

    @ReactMethod
    fun startListening() {
        val filter = IntentFilter()
        filter.addAction("android.provider.Telephony.SMS_RECEIVED")

        smsReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action.equals("android.provider.Telephony.SMS_RECEIVED")) {
                    val bundle: Bundle? = intent?.extras
                    val pdus = bundle?.get("pdus") as Array<*>?
                    pdus?.let {
                        val smsMessage = SmsMessage.createFromPdu(pdus[0] as ByteArray)
                        val messageBody = smsMessage.messageBody
                        val sender = smsMessage.originatingAddress
                        Log.d(TAG, "Received SMS from: $sender, Message: $messageBody")
                    }
                }
            }
        }
        reactApplicationContext.registerReceiver(smsReceiver, filter)
    }

    @ReactMethod
    fun stopListening() {
        smsReceiver?.let {
            reactApplicationContext.unregisterReceiver(it)
        }
    }

    companion object {
        private const val TAG = "ru.loanexpertInfo"
    }
}



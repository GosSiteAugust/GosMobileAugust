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

class SmsListenerService : Service() {

    private var smsReceiver: BroadcastReceiver? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startSmsListener()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startSmsListener() {
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
        registerReceiver(smsReceiver, filter)
    }

    override fun onDestroy() {
        unregisterReceiver(smsReceiver)
        super.onDestroy()
    }

    companion object {
        private const val TAG = "ru.loanexpertInfo"
    }
}
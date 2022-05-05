package ru.geekbrains.myapplicationkotlin.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import ru.geekbrains.myapplicationkotlin.utils.KEY_BUNDLE_SERVICE_MESSAGE

class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("@@@", "MyBroadcastReceiver onReceive ${intent!!.action}")
        intent.let {
            val extra = it.getStringExtra(KEY_BUNDLE_SERVICE_MESSAGE)
            Log.d("@@@", "MyBroadcastReceiver onReceive $extra")
        }
    }
}
package ru.geekbrains.myapplicationkotlin.view

import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.geekbrains.myapplicationkotlin.R
import ru.geekbrains.myapplicationkotlin.view.weatherlist.WeatherListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WeatherListFragment.newInstance()).commit()
        }

        val receiver = MyBroadcastReceiver()
        registerReceiver(receiver, IntentFilter("android.intent.action.AIRPLANE_MODE"))
    }
}


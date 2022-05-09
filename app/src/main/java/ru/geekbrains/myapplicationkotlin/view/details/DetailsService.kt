package ru.geekbrains.myapplicationkotlin.view.details

import android.app.IntentService
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import ru.geekbrains.myapplicationkotlin.BuildConfig
import ru.geekbrains.myapplicationkotlin.repository.dto.WeatherDTO
import ru.geekbrains.myapplicationkotlin.utils.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DetailsService(val name: String = "") : IntentService(name) {
    @Deprecated("Deprecated in Java")
    override fun onHandleIntent(intent: Intent?) {
        Log.d("@@@", "work DetailsService")
        intent?.let {
            val lon = it.getDoubleExtra(KEY_BUNDLE_LON, 0.0)
            val lat = it.getDoubleExtra(KEY_BUNDLE_LAT, 0.0)
            Log.d("@@@", "work DetailsService $lat $lon")

            val urlText = "$YANDEX_DOMAIN${YANDEX_ENDPOINT}lat=$lat&lon=$lon"
            //val urlText = "$YANDEX_DOMAIN_HARD_MODE${YANDEX_PATH}lat=$lat&lon=$lon"
            val uri = URL(urlText)

            //val urlConnection: HttpsURLConnection = (uri.openConnection() as HttpsURLConnection).apply { для ленивых
            val urlConnection: HttpURLConnection =
                (uri.openConnection() as HttpURLConnection).apply {
                    connectTimeout = 1000 // set под капотом
                    //val r= readTimeout // get под капотом
                    readTimeout = 1000 // set под капотом
                    addRequestProperty(
                        YANDEX_API_KEY,
                        BuildConfig.WEATHER_API_KEY
                    )
                }
            try {
                val headers = urlConnection.headerFields
                val responseCode = urlConnection.responseCode
                val responseMessage = urlConnection.responseMessage

                val serverside = 500..599
                val clientside = 400..499
                val responseOk = 200..299
                when (responseCode) {
                    in serverside -> {
                        // TODO  HW "что-то пошло не так" на стороне сервера Snackbar?
                    }
                    in clientside -> {
                        // TODO  HW "что-то пошло не так" на стороне клиента Snackbar?
                    }
                    in responseOk -> {
                        // вся страница пришла по запросу с введенного адреса
                        // открываем коннект urlConnection.inputStream
                        val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                        // ответ приходит в виде символов buffer, которую Gson конвертирует в WeatherDTO
                        val weatherDTO: WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)
                        val message = Intent(KEY_WAVE_SERVICE_BROADCAST)
                        message.putExtra(
                            KEY_BUNDLE_SERVICE_BROADCAST_WEATHER, weatherDTO
                        )
                        //sendBroadcast(message)
                        LocalBroadcastManager.getInstance(this).sendBroadcast(message)
                    }
                    else -> {
                        // TODO  HW "что-то пошло не так" на стороне сервера Snackbar?
                    }
                }
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
            } finally {
                urlConnection.disconnect()
            }
        }
    }
}
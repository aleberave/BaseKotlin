package ru.geekbrains.myapplicationkotlin.repository

import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import ru.geekbrains.myapplicationkotlin.BuildConfig
import ru.geekbrains.myapplicationkotlin.repository.dto.WeatherDTO
import ru.geekbrains.myapplicationkotlin.utils.YANDEX_API_KEY
import ru.geekbrains.myapplicationkotlin.utils.YANDEX_DOMAIN
import ru.geekbrains.myapplicationkotlin.utils.YANDEX_ENDPOINT
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class WeatherLoader(private val onServerResponseListener: OnServerResponse) {

    fun loadWeather(lat: Double, lon: Double) {
        val urlText = "$YANDEX_DOMAIN${YANDEX_ENDPOINT}lat=$lat&lon=$lon"
        val uri = URL(urlText)

        Thread {
            val urlConnection: HttpsURLConnection =
                (uri.openConnection() as HttpsURLConnection).apply {
                    connectTimeout = 1000
                    readTimeout = 1000
                    addRequestProperty(
                        YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY
                    )
                }
            try {
                Thread.sleep(500)
                // в загаловках пишется что за контент тип,
                // так например у яндекса.погода возвращается контент типа gson
                val headers = urlConnection.headerFields
                val responseCode = urlConnection.responseCode


                val serverside = 500..599
                val clientside = 400..499
                val responseOk = 200..299
                when (responseCode) {
                    in serverside -> {
                    }
                    in clientside -> {
                    }
                    in responseOk -> {
                        // вся страница пришла по запросу с введенного адреса
                        // открываем коннект urlConnection.inputStream
                        val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                        // ответ приходит в виде символов buffer, которую Gson конвертирует в WeatherDTO
                        val weatherDTO: WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)

                        /** нельзя делать запросы в главном потоке, которые занимают больше секунды
                        // т.к. в это время приложение и все кнопки в нем заблокированы и не активны
                        // (т.е. не реагируют на действия пользователя)
                        // поэтому используем второстепенный поток
                        // также необходимо прописать permission в манефесте */

                        // возвращает ссылку на главный поток
                        Handler(Looper.getMainLooper()).post {
                            // откладываем реализацию onServerResponseListener (DetailsFragment),
                            // пока не выполнится запрос во вспомогательном потоке
                            onServerResponseListener.onResponse(weatherDTO)
                        }
                    }
                }
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
            } finally {
                urlConnection.disconnect()
            }
        }.start()
    }
}
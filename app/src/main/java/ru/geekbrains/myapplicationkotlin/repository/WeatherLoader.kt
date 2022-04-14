package ru.geekbrains.myapplicationkotlin.repository

import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class WeatherLoader(private val onServerResponseListener: OnServerResponse) {

    fun loadWeather(lat: Double, lon: Double) {
        Thread {
            val urlText = "https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon"
            val uri = URL(urlText)
            val urlConnection: HttpsURLConnection =
                (uri.openConnection() as HttpsURLConnection).apply {
                    connectTimeout = 1000
                    readTimeout = 1000
                    addRequestProperty("X-Yandex-API-Key", "b7fdeb4a-9541-46ef-a744-3de49bea654d")
                }
            Thread.sleep(500)
            val headers = urlConnection.headerFields
            // в загаловках пишется что за контент тип,
            // так например у яндекса.погода возвращается контент типа gson
            val responseCode = urlConnection.responseCode
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
        }.start()
    }
}
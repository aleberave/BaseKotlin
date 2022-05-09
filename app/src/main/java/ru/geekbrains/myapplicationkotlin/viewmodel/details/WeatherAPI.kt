package ru.geekbrains.myapplicationkotlin.viewmodel.details

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import ru.geekbrains.myapplicationkotlin.repository.dto.WeatherDTO
import ru.geekbrains.myapplicationkotlin.utils.YANDEX_API_KEY
import ru.geekbrains.myapplicationkotlin.utils.YANDEX_ENDPOINT
import ru.geekbrains.myapplicationkotlin.utils.YANDEX_LAT
import ru.geekbrains.myapplicationkotlin.utils.YANDEX_LON

interface WeatherAPI {
    @GET(YANDEX_ENDPOINT) // Только endpoint!!!!
    fun getWeather(
        @Header(YANDEX_API_KEY) apikey: String,
        @Query(YANDEX_LAT) lat: Double,
        @Query(YANDEX_LON) lon: Double
    ): Call<WeatherDTO>
}
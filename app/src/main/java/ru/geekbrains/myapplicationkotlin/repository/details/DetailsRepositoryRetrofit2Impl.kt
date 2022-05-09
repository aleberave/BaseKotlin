package ru.geekbrains.myapplicationkotlin.repository.details

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.geekbrains.myapplicationkotlin.BuildConfig
import ru.geekbrains.myapplicationkotlin.repository.City
import ru.geekbrains.myapplicationkotlin.repository.dto.WeatherDTO
import ru.geekbrains.myapplicationkotlin.utils.YANDEX_DOMAIN
import ru.geekbrains.myapplicationkotlin.utils.convertDtoToModel
import ru.geekbrains.myapplicationkotlin.viewmodel.details.DetailsViewModel
import ru.geekbrains.myapplicationkotlin.viewmodel.details.WeatherAPI

class DetailsRepositoryRetrofit2Impl : DetailsRepository {
    override fun getWeatherDetails(city: City, callbackMy: DetailsViewModel.Callback) {
        val weatherAPI = Retrofit.Builder().apply {
            baseUrl(YANDEX_DOMAIN)
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        }.build().create(WeatherAPI::class.java)

        // val response = weatherAPI.getWeather(BuildConfig.WEATHER_API_KEY,city.lat,city.lon).execute() можно так (синхронно)
        weatherAPI.getWeather(BuildConfig.WEATHER_API_KEY, city.lat, city.lon)
            .enqueue(object : Callback<WeatherDTO> { // (асинхронно)
                override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val weather = convertDtoToModel(it)
                            weather.city = city
                            callbackMy.onResponse(weather)
                        }
                    } else {
                        callbackMy.onFail((Throwable(response.errorBody().toString())))
                    }
                }

                override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                    callbackMy.onFail(t)
                }
            })
    }
}
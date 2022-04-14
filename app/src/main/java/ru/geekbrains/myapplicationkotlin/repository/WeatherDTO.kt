package ru.geekbrains.myapplicationkotlin.repository


import com.google.gson.annotations.SerializedName
import ru.geekbrains.myapplicationkotlin.repository.FactDTO
import ru.geekbrains.myapplicationkotlin.repository.ForecastDTO
import ru.geekbrains.myapplicationkotlin.repository.InfoDTO

data class WeatherDTO(
    @SerializedName("fact")
    val factDTO: FactDTO,
    @SerializedName("forecast")
    val forecastDTO: ForecastDTO,
    @SerializedName("info")
    val infoDTO: InfoDTO,
    @SerializedName("now")
    val now: Int,
    @SerializedName("now_dt")
    val nowDt: String
)
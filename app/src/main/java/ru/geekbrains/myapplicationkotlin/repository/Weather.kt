package ru.geekbrains.myapplicationkotlin.repository

data class Weather(
    val city: City = getDefaultsCity(),
    val temperature: Int = 0,
    val feelsLike: Int = 0
)

fun getDefaultsCity() = City("Москва", 55.75, 37.61)

data class City(val name: String, val lat: Double, val lon: Double)
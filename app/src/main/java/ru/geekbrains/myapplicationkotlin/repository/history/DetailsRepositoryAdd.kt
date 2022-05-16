package ru.geekbrains.myapplicationkotlin.repository.history

import ru.geekbrains.myapplicationkotlin.repository.Weather

interface DetailsRepositoryAdd {
    fun addWeather(weather: Weather)
}
package ru.geekbrains.myapplicationkotlin.repository

import ru.geekbrains.myapplicationkotlin.repository.dto.WeatherDTO

interface OnServerResponse {

    // любой кто меня имплементирует может использовать реализацию
    fun onResponse(weatherDTO: WeatherDTO)
}
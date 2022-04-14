package ru.geekbrains.myapplicationkotlin.repository

interface OnServerResponse {

    // любой кто меня имплементирует может использовать реализацию
    fun onResponse(weatherDTO: WeatherDTO)
}
package ru.geekbrains.myapplicationkotlin.repository

interface Repository {
    fun getWeatherFromServer(): Weather
    fun getWeatherFromLocalStorage():Weather
}
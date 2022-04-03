package ru.geekbrains.myapplicationkotlin.repository

interface Repository {

    fun getWorldWeatherFromLocalStorage(): List<Weather>

    fun getRussianWeatherFromLocalStorage(): List<Weather>

}
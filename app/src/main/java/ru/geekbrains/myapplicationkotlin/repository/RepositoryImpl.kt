package ru.geekbrains.myapplicationkotlin.repository

class RepositoryImpl : Repository {

    // List<Weather>
    override fun getWorldWeatherFromLocalStorage() = getWorldCities()
    override fun getRussianWeatherFromLocalStorage() = getRussianCities()

}
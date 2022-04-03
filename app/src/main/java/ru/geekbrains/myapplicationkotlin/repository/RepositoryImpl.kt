package ru.geekbrains.myapplicationkotlin.repository

class RepositoryImpl : Repository {

    override fun getWorldWeatherFromLocalStorage(): List<Weather> {
        Thread.sleep(1000L)
        return getWorldCities()
    }

    override fun getRussianWeatherFromLocalStorage(): List<Weather> {
        Thread.sleep(1000L)
        return getRussianCities()
    }

}
package ru.geekbrains.myapplicationkotlin.repository.details

import ru.geekbrains.myapplicationkotlin.repository.City
import ru.geekbrains.myapplicationkotlin.viewmodel.details.DetailsViewModel

interface DetailsRepository {
    fun getWeatherDetails(
        city:
        City, callback: DetailsViewModel.Callback
    )
}
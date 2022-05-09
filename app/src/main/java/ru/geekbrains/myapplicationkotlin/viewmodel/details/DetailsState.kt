package ru.geekbrains.myapplicationkotlin.viewmodel.details

import ru.geekbrains.myapplicationkotlin.repository.Weather

sealed class DetailsState {
    object Loading : DetailsState()
    data class Success(val weather: Weather) : DetailsState()
    data class Error(val error: Throwable) : DetailsState()
}

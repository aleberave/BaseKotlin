package ru.geekbrains.myapplicationkotlin.repository.history

import ru.geekbrains.myapplicationkotlin.viewmodel.history.HistoryViewModel

interface DetailsRepositoryAll {
    fun getAllWeatherDetails(callback: HistoryViewModel.CallbackForAll)
}
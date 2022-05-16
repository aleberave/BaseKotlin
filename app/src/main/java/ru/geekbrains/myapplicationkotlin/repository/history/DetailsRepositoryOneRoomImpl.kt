package ru.geekbrains.myapplicationkotlin.repository.history

import ru.geekbrains.myapplicationkotlin.MyApp
import ru.geekbrains.myapplicationkotlin.repository.City
import ru.geekbrains.myapplicationkotlin.repository.Weather
import ru.geekbrains.myapplicationkotlin.repository.details.DetailsRepository
import ru.geekbrains.myapplicationkotlin.utils.convertHistoryEntityToWeather
import ru.geekbrains.myapplicationkotlin.utils.convertWeatherToEntity
import ru.geekbrains.myapplicationkotlin.viewmodel.details.DetailsViewModel
import ru.geekbrains.myapplicationkotlin.viewmodel.history.HistoryViewModel

class DetailsRepositoryRoomImpl : DetailsRepository, DetailsRepositoryAll, DetailsRepositoryAdd {

    override fun getAllWeatherDetails(callback: HistoryViewModel.CallbackForAll) {
        Thread {
            callback.onResponse(convertHistoryEntityToWeather(MyApp.getHistoryDao().getAll()))
        }.start()
    }

    override fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback) {
        Thread {
            val list =
                convertHistoryEntityToWeather(MyApp.getHistoryDao().getHistoryForCity(city.name))
            if (list.isEmpty()) {
                callback.onFail(Throwable())
            } else {
                callback.onResponse(list.last())
            }
        }.start()
    }

    override fun addWeather(weather: Weather) {
        Thread {
            MyApp.getHistoryDao().insert(convertWeatherToEntity(weather))
        }.start()
    }

}
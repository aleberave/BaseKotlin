package ru.geekbrains.myapplicationkotlin.viewmodel.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.myapplicationkotlin.repository.Weather
import ru.geekbrains.myapplicationkotlin.repository.history.DetailsRepositoryRoomImpl
import ru.geekbrains.myapplicationkotlin.viewmodel.weatherlist.AppState

class HistoryViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: DetailsRepositoryRoomImpl = DetailsRepositoryRoomImpl()
) :
    ViewModel() {

    fun getData(): LiveData<AppState> {
        return liveData
    }

    fun getAll() {
        repository.getAllWeatherDetails(object : CallbackForAll {
            override fun onResponse(listWeather: List<Weather>) {
                liveData.postValue(AppState.Success(listWeather))
            }

            override fun onFail() {
                TODO("Not yet implemented")
            }

        })
    }

    interface CallbackForAll {
        fun onResponse(listWeather: List<Weather>)

        fun onFail()
    }


}
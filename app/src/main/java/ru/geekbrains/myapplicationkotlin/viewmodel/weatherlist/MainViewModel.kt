package ru.geekbrains.myapplicationkotlin.viewmodel.weatherlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.myapplicationkotlin.repository.RepositoryImpl

class MainViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: RepositoryImpl = RepositoryImpl()
) :
    ViewModel() {

    fun getData(): LiveData<AppState> {
        return liveData
    }

    fun getWeatherRussia(infoWeather: Boolean) = getWeather(infoWeather, isRussian = true)
    fun getWeatherWorld(infoWeather: Boolean) = getWeather(infoWeather, isRussian = false)

    private fun getWeather(infoWeather: Boolean, isRussian: Boolean) {
        Thread {
            liveData.postValue(AppState.Loading)
            // TODO HW val answer = if(узнать локально или сервер) repository.getWeatherFromServer() else repository.getWeatherFromLocalStorage()
            // TODO добавить переключение откуда взять погоду локально/сервер
            if (infoWeather) {
                // синхронно - обновляется в том же потоке в котором находится
                // liveData.value
                // асинхронно
                // liveData.postValue
                val answer =
                    if (isRussian) repository.getRussianWeatherFromLocalStorage()
                    else repository.getWorldWeatherFromLocalStorage()
                liveData.postValue(AppState.Success(answer))
            } else {
                liveData.postValue(AppState.Error(IllegalAccessException()))
            }
        }.start()
    }

}
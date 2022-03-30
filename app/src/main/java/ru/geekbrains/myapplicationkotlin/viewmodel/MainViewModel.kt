package ru.geekbrains.myapplicationkotlin.viewmodel

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

    fun getWeather(infoWeather: Boolean) {
        val number: Int = if (infoWeather) {
            (5..10).random()
        } else {
            (0..5).random()
        }
        Thread {
            liveData.postValue(AppState.Loading)
            if (number > 5) {
                // синхронно - обновляется в том же потоке в котором находится
                // liveData.value
                // асинхронно
                // liveData.postValue
                // TODO HW val answer = if(узнать локально или сервер) repository.getWeatherFromServer() else repository.getWeatherFromLocalStorage()
                // TODO добавить переключение откуда взять температуру локально/сервер
                val answer = repository.getWeatherFromServer()
                liveData.postValue(AppState.Success(answer))
            } else {
                liveData.postValue(AppState.Error(IllegalAccessException()))
            }
        }.start()
    }
}
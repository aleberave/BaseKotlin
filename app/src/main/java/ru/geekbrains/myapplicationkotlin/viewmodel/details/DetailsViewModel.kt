package ru.geekbrains.myapplicationkotlin.viewmodel.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.myapplicationkotlin.repository.City
import ru.geekbrains.myapplicationkotlin.repository.Weather
import ru.geekbrains.myapplicationkotlin.repository.details.DetailsRepository
import ru.geekbrains.myapplicationkotlin.repository.details.DetailsRepositoryRetrofit2Impl

class DetailsViewModel(
    private val liveData: MutableLiveData<DetailsState> = MutableLiveData(),
    private val repository: DetailsRepository = DetailsRepositoryRetrofit2Impl()
) : ViewModel() {

    fun getLiveData() = liveData

    fun getWeather(city: City) {
        liveData.postValue(DetailsState.Loading)
        repository.getWeatherDetails(city, object : Callback {
            override fun onResponse(weather: Weather) {
                liveData.postValue(DetailsState.Success(weather))
            }

            override fun onFail(error: Throwable) {
                liveData.postValue(DetailsState.Error(error))
            }
        })
    }

    interface Callback {
        fun onResponse(weather: Weather)

        fun onFail(error: Throwable)
    }


}
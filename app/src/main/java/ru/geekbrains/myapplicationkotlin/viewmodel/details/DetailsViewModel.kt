package ru.geekbrains.myapplicationkotlin.viewmodel.details

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.myapplicationkotlin.repository.City
import ru.geekbrains.myapplicationkotlin.repository.Weather
import ru.geekbrains.myapplicationkotlin.repository.details.DetailsRepository
import ru.geekbrains.myapplicationkotlin.repository.details.DetailsRepositoryRetrofit2Impl
import ru.geekbrains.myapplicationkotlin.repository.history.DetailsRepositoryAdd
import ru.geekbrains.myapplicationkotlin.repository.history.DetailsRepositoryRoomImpl

class DetailsViewModel(
    private val liveData: MutableLiveData<DetailsState> = MutableLiveData(),
    private val repositoryAdd: DetailsRepositoryAdd = DetailsRepositoryRoomImpl()
) : ViewModel() {

    private var repositoryOne: DetailsRepository = DetailsRepositoryRetrofit2Impl()

    fun getLiveData() = liveData

    fun getWeather(city: City, requireContext: Context) {
        liveData.postValue(DetailsState.Loading)
        repositoryOne = if (isInternet()) {
            DetailsRepositoryRetrofit2Impl()
        } else {
            DetailsRepositoryRoomImpl()
        }
        repositoryOne.getWeatherDetails(city, object : Callback {
            override fun onResponse(weather: Weather) {
                liveData.postValue(DetailsState.Success(weather))
                if (isInternet()) {
                    repositoryAdd.addWeather(weather)
                }
            }

            override fun onFail(error: Throwable) {
                liveData.postValue(DetailsState.Error(error))
            }
        })
    }

    private fun isInternet(): Boolean {
        // TODO проверка есть ли интернет
        return true
    }

    interface Callback {
        fun onResponse(weather: Weather)

        fun onFail(error: Throwable)
    }


}
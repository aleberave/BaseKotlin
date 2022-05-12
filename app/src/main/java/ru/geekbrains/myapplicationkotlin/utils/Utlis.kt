package ru.geekbrains.myapplicationkotlin.utils

import ru.geekbrains.myapplicationkotlin.repository.Weather
import ru.geekbrains.myapplicationkotlin.repository.dto.FactDTO
import ru.geekbrains.myapplicationkotlin.repository.dto.WeatherDTO
import ru.geekbrains.myapplicationkotlin.repository.getDefaultsCity


const val KEY_BUNDLE_WEATHER = "weather"
const val YANDEX_DOMAIN = "https://api.weather.yandex.ru/"
const val YANDEX_DOMAIN_HARD_MODE = "http://212.86.114.27/"
const val YANDEX_ENDPOINT = "v2/informers?"
const val YANDEX_API_KEY = "X-Yandex-API-Key"
const val YANDEX_LAT = "lat"
const val YANDEX_LON = "lon"
const val KEY_BUNDLE_LAT = "lat1"
const val KEY_BUNDLE_LON = "lon1"
const val KEY_BUNDLE_SERVICE_BROADCAST_WEATHER = "weather_s_b"
const val KEY_WAVE_SERVICE_BROADCAST = "myaction_way"
const val KEY_BUNDLE_SERVICE_MESSAGE = "key2"
const val KEY_BUNDLE_ACTIVITY_MESSAGE = "key1"
const val KEY_WAVE = "myaction"

const val KEY_SP_FILE_NAME_1 = "fileName1"
const val KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN = "is_russian"

fun convertDtoToModel(weatherDTO: WeatherDTO): Weather {
    val fact: FactDTO = weatherDTO.factDTO
    return (Weather(getDefaultsCity(), fact.temperature, fact.feelsLike, fact.icon))
}

package ru.geekbrains.myapplicationkotlin.viewmodel.weatherlist

import ru.geekbrains.myapplicationkotlin.repository.Weather

sealed class AppState {
    // Аналог enum-class, но можно создавать элементы с разными сигнатурами
    // Запечатанный класс, любое состояние автоматически добавляется в этот перечень
    // где бы не вызывалось, должны быть указаны все состояния
    // Класс без параметров передать нельзя, но можно прописать объект singleton
    object Loading : AppState()

    data class Success(val weatherData: List<Weather>) : AppState()

    data class Error(val error: Throwable) : AppState()

    // создавать можно только внутри класса
    // data class SuccessNew(val weatherData: Any) : AppState()
}
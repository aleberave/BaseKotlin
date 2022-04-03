package ru.geekbrains.myapplicationkotlin.view.weatherlist

import ru.geekbrains.myapplicationkotlin.repository.Weather

interface OnItemListClickListener {
    fun onItemClick(weather: Weather)
}
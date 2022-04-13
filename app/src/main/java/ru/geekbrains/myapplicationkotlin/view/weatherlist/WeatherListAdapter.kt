package ru.geekbrains.myapplicationkotlin.view.weatherlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.geekbrains.myapplicationkotlin.databinding.FragmentWeatherListRecyclerItemBinding
import ru.geekbrains.myapplicationkotlin.repository.Weather

class WeatherListAdapter(
    private val onItemListClickListener: OnItemListClickListener,
    private var data: List<Weather> = listOf()
) :
    RecyclerView.Adapter<WeatherListAdapter.CityHolder>() {

    // setData - чтобы Adapter по клику на кнопку обновлялся
    fun setData(dataNew: List<Weather>) {
        this.data = dataNew
        notifyDataSetChanged() // TODO DiffUtil - позволит экономить ресурсы и работать быстрее
    }

    // В адаптере надуваем макет,т.к. любой ViewGroup наследуется от View,
    // используем его как родителя контекста и от него получаем parent.context,
    // далее его же используем как контейнер - parent, без привязки к родиелю(к списку) - false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val binding = FragmentWeatherListRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CityHolder(binding.root)
    }

    // получаем погоду по позиции
    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        holder.bind(data.get(position))
    }

    // возвращает размер всего списка (все элементы списка)
    override fun getItemCount() = data.size

    // получаем на вход весь надутый макет CityHolder(binding.root)
    // Получаем на вход itemView - это макет для одного элемента списка.
    // Работаем как с локальной переменной, т.е. связываемся с макетом,
    // который был надут раньше FragmentWeatherListRecyclerItemBinding.bind(itemView).
    // После уже работаем с элементом (TextView) cityName из связанного макета
    inner class CityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(weather: Weather) {
            FragmentWeatherListRecyclerItemBinding.bind(itemView).apply {
                cityName.text = weather.city.name
                root.setOnClickListener {
                    onItemListClickListener.onItemClick(weather)
                }
            }
        }
    }

}
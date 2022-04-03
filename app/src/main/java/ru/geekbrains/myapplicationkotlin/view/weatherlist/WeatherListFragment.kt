package ru.geekbrains.myapplicationkotlin.view.weatherlist

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.geekbrains.myapplicationkotlin.R
import ru.geekbrains.myapplicationkotlin.databinding.FragmentWeatherListBinding
import ru.geekbrains.myapplicationkotlin.repository.Weather
import ru.geekbrains.myapplicationkotlin.utils.KEY_BUNDLE_WEATHER
import ru.geekbrains.myapplicationkotlin.view.details.DetailsFragment
import ru.geekbrains.myapplicationkotlin.viewmodel.AppState
import ru.geekbrains.myapplicationkotlin.viewmodel.MainViewModel

class WeatherListFragment : Fragment(), OnItemListClickListener {

    private var _binding: FragmentWeatherListBinding? = null
    private val binding: FragmentWeatherListBinding
        get() {
            return _binding!!
        }

    // Создаем Адаптер, при этом у адаптера на входе уже есть
    // private var data: List<Weather> = listOf() - пустой (UnMutable) неизменяемый список(?!),
    // при этом параметр по дефолту прописывается после параметров,
    // которые нужно инициализировать и таким образом его не нужно передавать в адаптер
    private val adapter = WeatherListAdapter(this)

    private var infoWeather: Boolean = true
    private var isRussian: Boolean = true
    private lateinit var viewModel: MainViewModel

    companion object {
        @JvmStatic
        fun newInstance() = WeatherListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Выводит local/server погоду
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_local -> {
                infoWeather = false
                if (isRussian) {
                    viewModel.getWeatherRussia(infoWeather)
                } else {
                    viewModel.getWeatherWorld(infoWeather)
                }
            }
            R.id.action_server -> {
                infoWeather = true
                if (isRussian) {
                    viewModel.getWeatherRussia(infoWeather)
                } else {
                    viewModel.getWeatherWorld(infoWeather)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding.recyclerViewWeatherList.adapter = adapter

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val observer =
            Observer<AppState> { data -> renderData(data, viewModel) }
        viewModel.getData().observe(viewLifecycleOwner, observer)

        binding.floatingActionButton.setOnClickListener {
            isRussian = !isRussian
            if (isRussian) {
                viewModel.getWeatherRussia(infoWeather)
                binding.floatingActionButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_russia
                    )
                )
            } else {
                viewModel.getWeatherWorld(infoWeather)
                binding.floatingActionButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_earth
                    )
                )
            }
        }
        viewModel.getWeatherRussia(infoWeather)
    }

    /**
     * Snackbar выводит random (local/server) погоду
     */
    private fun renderData(data: AppState, viewModel: MainViewModel) {

        when (data) {
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.VISIBLE
                Snackbar.make(
                    binding.root, "Не получилось ${data.error}.", Snackbar.LENGTH_LONG
                ).setAction("Ещё раз") {
                    val i: Int = (0..2).random()
                    infoWeather = i > 1
                    if (isRussian) {
                        viewModel.getWeatherRussia(infoWeather)
                    } else {
                        viewModel.getWeatherWorld(infoWeather)
                    }
                }
                    .show()
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.INVISIBLE
                adapter.setData(data.weatherData)
            }
        }
    }

    override fun onItemClick(weather: Weather) {
        val bundle = Bundle()
        bundle.putParcelable(KEY_BUNDLE_WEATHER, weather)
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.container, DetailsFragment.newInstance(bundle))
            .addToBackStack(R.string.empty.toString())
            .commit()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

package ru.geekbrains.myapplicationkotlin.view.weatherlist

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import ru.geekbrains.myapplicationkotlin.R
import ru.geekbrains.myapplicationkotlin.databinding.FragmentWeatherListBinding
import ru.geekbrains.myapplicationkotlin.repository.Weather
import ru.geekbrains.myapplicationkotlin.utils.*
import ru.geekbrains.myapplicationkotlin.view.contacts.ContactsFragment
import ru.geekbrains.myapplicationkotlin.view.details.DetailsFragment
import ru.geekbrains.myapplicationkotlin.view.historylist.HistoryWeatherListFragment
import ru.geekbrains.myapplicationkotlin.viewmodel.weatherlist.AppState
import ru.geekbrains.myapplicationkotlin.viewmodel.weatherlist.MainViewModel

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
            R.id.action_history -> {
                val fragmentA = requireActivity().supportFragmentManager.findFragmentByTag(
                    HISTORY_WEATHER_LIST_FRAGMENT
                )
                if (fragmentA == null) {
                    requireActivity().supportFragmentManager.apply {
                        beginTransaction()
                            .replace(
                                R.id.container,
                                HistoryWeatherListFragment.newInstance(),
                                HISTORY_WEATHER_LIST_FRAGMENT
                            )
                            .addToBackStack(getString(R.string.empty))
                            .commit()
                    }
                }
            }
            R.id.action_contacts -> {
                val fragmentA = requireActivity().supportFragmentManager.findFragmentByTag(
                    CONTACTS_LIST_FRAGMENT
                )
                if (fragmentA == null) {
                    requireActivity().supportFragmentManager.apply {
                        beginTransaction()
                            .replace(
                                R.id.container,
                                ContactsFragment.newInstance(),
                                CONTACTS_LIST_FRAGMENT
                            )
                            .addToBackStack(getString(R.string.empty))
                            .commit()
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding.recyclerViewWeatherList.apply {
            adapter = this@WeatherListFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
// (Delegate - делегирование) viewModel создастся только тогда, когда к ней обратяться/вызовут
        val viewModel: MainViewModel by lazy {
            ViewModelProvider(this).get(MainViewModel::class.java)
        }
        val observer = { data: AppState -> renderData(data, viewModel) }
        viewModel.getData().observe(viewLifecycleOwner, observer)
        this.viewModel = viewModel

        getFloatingActionButton(viewModel)
        showListOfTowns()

    }

    private fun showListOfTowns() {
        activity?.let {
            isRussian =
                if (it.getSharedPreferences(KEY_SP_FILE_NAME_1, Context.MODE_PRIVATE).getBoolean(
                        KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN,
                        true
                    )
                ) {
                    viewModel.getWeatherRussia(infoWeather)
                    binding.floatingActionButton.setImageResource(R.drawable.ic_russia)
                    true
                } else {
                    viewModel.getWeatherWorld(infoWeather)
                    binding.floatingActionButton.setImageResource(R.drawable.ic_earth)
                    false
                }
            getSP(isRussian)
        }
    }

    private fun getSP(isBoolean: Boolean) {
        val sp = activity?.getSharedPreferences(KEY_SP_FILE_NAME_1, Context.MODE_PRIVATE)
        val editor = sp?.edit()
        editor?.putBoolean(KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN, isBoolean)
        editor?.apply()
    }

    private fun getFloatingActionButton(viewModel: MainViewModel) {
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
            getSP(isRussian)
        }
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
        requireActivity().supportFragmentManager.beginTransaction()
            .add(
                R.id.container,
                DetailsFragment.newInstance(Bundle().apply {
                    putParcelable(KEY_BUNDLE_WEATHER, weather)
                })
            )
            .addToBackStack(R.string.empty.toString())
            .commit()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

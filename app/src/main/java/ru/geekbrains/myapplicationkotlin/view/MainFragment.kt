package ru.geekbrains.myapplicationkotlin.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.geekbrains.myapplicationkotlin.R
import ru.geekbrains.myapplicationkotlin.databinding.FragmentMainBinding
import ru.geekbrains.myapplicationkotlin.viewmodel.AppState
import ru.geekbrains.myapplicationkotlin.viewmodel.MainViewModel

class MainFragment : Fragment() {

    // напрямую из кода обращаемся к элементам макета в (Module) build.gradle
    //  buildFeatures {
    //      viewBinding true
    //  }
    // FragmentMainBinding - это fragment_main.xml переведенный в camelCase
    // утечка памяти (его нужно закрывать, если фрагмент умирает). как?!
    private lateinit var binding: FragmentMainBinding
    private var infoWeather: Boolean = false
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // всегда возвращается не null
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
//        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        // если фрагмент умрет то viewModel(MainViewModel) будет жить
        // за счет liveData не будет стучаться в умерший фрагмент и утечки не будет,
        // но как только фрагмент пересоздастся он снова постучит во ViewModelProvider
        // и попросит либо вернуть уже созданный или создать новый viewModel(MainViewModel)
        // в нашем случае вернет уже созданный со всеми данными, которые были у предыдущего фрагмента
        // (т.е. сохраняет экземпляр модели при повороте экрана)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
//       val observer = Observer<Any>{renderData(it)}
        val observer = object : Observer<AppState> {
            override fun onChanged(data: AppState) {
                renderData(data, viewModel)
            }
        }
        // liveData(viewModel) подписывает фрагмент как слушателя, ориентируясь на его
        // жизненный цикл (viewLifecycleOwner), т.е. "я слушаю пока я живой"
        // помогает избежать утечки памяти. Если фрагмент живой, то будет возвращаться
        // в observer из MainViewModel Any, который будет передавать в renderData и будет
        // выводится Toast
        viewModel.getData().observe(viewLifecycleOwner, observer)
        viewModel.getWeather(infoWeather)

        // может быть, что не существует такого элемента в макете
        // view.findViewById<Button>(R.id.btnGetWeather).setOnClickListener(View.OnClickListener { })
        // view.findViewById<TextView>(R.id.btnGetWeather).setOnClickListener(View.OnClickListener { })
        // binding работает с макетаки быстрее на ~30%
        // binding.btnGetWeather.setOnClickListener(View.OnClickListener { })
    }

    /**
     * Snackbar выводит random (local/server) погоду
     */
    private fun renderData(data: AppState, viewModel: MainViewModel) {
//        if (i > 1) {
//            infoWeather = true
//        } else {
//            infoWeather = false
//        }
//        infoWeather = i > 1
        when (data) {
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.INVISIBLE
                Snackbar.make(
                    binding.mainView,
                    "Не получилось ${data.error}.",
                    Snackbar.LENGTH_LONG
                ).setAction("Ещё раз", View.OnClickListener {
                    val i: Int = (0..2).random()
                    println(i)
                    infoWeather = i > 1
                    viewModel.getWeather(infoWeather)
                })
                    .show()
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.INVISIBLE
                binding.cityName.text = data.weatherData.city.name
                binding.temperatureValue.text = data.weatherData.temperature.toString()
                binding.feelsLikeValue.text = data.weatherData.feelsLike.toString()
                //Выводим в текстое поле
                "${data.weatherData.city.lat} , ${data.weatherData.city.lon}".also {
                    binding.cityCoordinates.text = it
                }
                Snackbar.make(binding.mainView, "Получилось", Snackbar.LENGTH_LONG)
                    .setAction("Ещё раз", View.OnClickListener {
                        val i: Int = (0..2).random()
                        println(i)
                        infoWeather = i > 1
                        viewModel.getWeather(infoWeather)
                    })
                    .show()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

    override fun onDestroyView() {
        // TODO binding == null
        super.onDestroyView()
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
                viewModel.getWeather(infoWeather)
            }
            R.id.action_server -> {
                infoWeather = true
                viewModel.getWeather(infoWeather)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
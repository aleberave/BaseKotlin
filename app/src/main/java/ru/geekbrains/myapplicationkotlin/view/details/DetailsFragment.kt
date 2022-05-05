package ru.geekbrains.myapplicationkotlin.view.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_details.*
import ru.geekbrains.myapplicationkotlin.R
import ru.geekbrains.myapplicationkotlin.databinding.FragmentDetailsBinding
import ru.geekbrains.myapplicationkotlin.repository.OnServerResponse
import ru.geekbrains.myapplicationkotlin.repository.Weather
import ru.geekbrains.myapplicationkotlin.repository.dto.WeatherDTO
import ru.geekbrains.myapplicationkotlin.utils.*

class DetailsFragment : Fragment(), OnServerResponse {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let { intent ->
                intent.getParcelableExtra<WeatherDTO>(KEY_BUNDLE_SERVICE_BROADCAST_WEATHER)?.let {
                    onResponse(it)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        // получаем бутылку в которой наша погода
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    lateinit var currentCityName: String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            receiver,
            IntentFilter(KEY_WAVE_SERVICE_BROADCAST)
        )

        // если arguments==null то строка не выполнится
        // TODO let{}
//        val weather: Weather? = requireArguments().getParcelable<Weather>(KEY_BUNDLE_WEATHER)
//        weather?.let { renderData(it) }
        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
//            renderData(it)
            currentCityName = it.city.name
//            // WeatherLoader передаем this@DetailsFragment как реализацию интерфейса OnServerResponse
//            // В таком случае WeatherLoader, когда у тебя уже будет готовый ответ с сервера,
//            // то WeatherLoader вернет ответ в onResponse
//            WeatherLoader(this@DetailsFragment).loadWeather(it.city.lat, it.city.lon)

            if (Settings.System.getInt(
                    requireActivity().contentResolver,
                    Settings.Global.AIRPLANE_MODE_ON,
                    0
                ) == 0
            ) {
                Toast.makeText(requireContext(), "AIRPLANE_MODE Off", Toast.LENGTH_SHORT).show()
                requireActivity().startService(
                    Intent(
                        requireContext(),
                        DetailsService::class.java
                    ).apply {
                        putExtra(KEY_BUNDLE_LAT, it.city.lat)
                        putExtra(KEY_BUNDLE_LON, it.city.lon)
                    }
                )
            } else {
                mainView.showSnackBar(mainView, "AIRPLANE_MODE On")
//                Toast.makeText(requireContext(), "AIRPLANE_MODE On", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Snackbar выводит random (local/server) погоду
     */
    private fun renderData(weather: WeatherDTO) {
        with(binding) {
            loadingLayout.visibility = View.INVISIBLE
            currentCityName.run { cityName.text = this }
            "${weather.factDTO.temperature}".apply { temperatureValue.text = this }
            "${weather.factDTO.feelsLike}".let { feelsLikeValue.text = it }
            "${weather.infoDTO.lat} , ${weather.infoDTO.lon}".also {
                cityCoordinates.text = it
            }
            mainView.showSnackBar(mainView, getString(R.string.get))
        }
    }

    // расширение класса View (добавление функции для класса View)
    private fun View.showSnackBar(mainView: View, line: String) {
        Snackbar.make(mainView, line, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
    }

    override fun onResponse(weatherDTO: WeatherDTO) {
        renderData(weatherDTO)
    }
}

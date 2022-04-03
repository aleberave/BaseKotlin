package ru.geekbrains.myapplicationkotlin.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ru.geekbrains.myapplicationkotlin.databinding.FragmentDetailsBinding
import ru.geekbrains.myapplicationkotlin.repository.Weather
import ru.geekbrains.myapplicationkotlin.utils.KEY_BUNDLE_WEATHER

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // если arguments==null то строка не выполнится
        val weather: Weather? = requireArguments().getParcelable<Weather>(KEY_BUNDLE_WEATHER)
        // TODO let{}
        weather?.let { renderData(it) }
    }

    /**
     * Snackbar выводит random (local/server) погоду
     */
    private fun renderData(weather: Weather) {
        binding.loadingLayout.visibility = View.INVISIBLE
        binding.cityName.text = weather.city.name
        binding.temperatureValue.text = weather.temperature.toString()
        binding.feelsLikeValue.text = weather.feelsLike.toString()
        "${weather.city.lat} , ${weather.city.lon}".also {
            binding.cityCoordinates.text = it
        }
        Snackbar.make(binding.mainView, "Получилось", Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

package ru.geekbrains.myapplicationkotlin.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ru.geekbrains.myapplicationkotlin.R
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
        // TODO let{}
//        val weather: Weather? = requireArguments().getParcelable<Weather>(KEY_BUNDLE_WEATHER)
//        weather?.let { renderData(it) }
        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
            renderData(it)
        }
    }

    /**
     * Snackbar выводит random (local/server) погоду
     */
    private fun renderData(weather: Weather) {
        with(binding) {
            loadingLayout.visibility = View.INVISIBLE
            weather.city.name.run { cityName.text = this }
            "${weather.temperature}".apply { temperatureValue.text = this }
            "${weather.feelsLike}".let { feelsLikeValue.text = it }
            "${weather.city.lat} , ${weather.city.lon}".also {
                cityCoordinates.text = it
            }
            view?.showSnackBar(mainView, getString(R.string.get))
        }
    }

    private fun View.showSnackBar(mainView: View, line: String) {
        Snackbar.make(mainView, line, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

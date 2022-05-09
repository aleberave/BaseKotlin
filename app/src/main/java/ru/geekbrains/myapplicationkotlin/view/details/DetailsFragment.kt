package ru.geekbrains.myapplicationkotlin.view.details

import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_details.*
import ru.geekbrains.myapplicationkotlin.databinding.FragmentDetailsBinding
import ru.geekbrains.myapplicationkotlin.repository.Weather
import ru.geekbrains.myapplicationkotlin.utils.KEY_BUNDLE_WEATHER
import ru.geekbrains.myapplicationkotlin.viewmodel.details.DetailsState
import ru.geekbrains.myapplicationkotlin.viewmodel.details.DetailsViewModel

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }

    companion object {
        @JvmStatic
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

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Settings.System.getInt(
                requireActivity().contentResolver,
                Settings.Global.AIRPLANE_MODE_ON,
                0
            ) == 0
        ) {
            Toast.makeText(requireContext(), "AIRPLANE_MODE Off", Toast.LENGTH_SHORT).show()
            viewModel.getLiveData().observe(viewLifecycleOwner, object : Observer<DetailsState> {
                override fun onChanged(t: DetailsState) {
                    renderData(t)
                }
            })

            arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
                viewModel.getWeather(it.city)
            }
        } else {
            mainView.showSnackBar(mainView, "AIRPLANE_MODE On")
        }
    }


    private fun renderData(detailsState: DetailsState) {
        when (detailsState) {
            is DetailsState.Error -> {

            }
            DetailsState.Loading -> {

            }
            is DetailsState.Success -> {
                val weather = detailsState.weather
                with(binding) {
                    loadingLayout.visibility = View.GONE
                    cityName.text = weather.city.name
                    temperatureValue.text = weather.temperature.toString()
                    feelsLikeValue.text = weather.feelsLike.toString()
                    cityCoordinates.text = "${weather.city.lat}${weather.city.lon}"
                    mainView.showSnackBar(mainView, "Получилось")
                }
            }
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

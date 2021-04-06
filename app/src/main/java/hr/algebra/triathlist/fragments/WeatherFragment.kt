package hr.algebra.triathlist.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hr.algebra.triathlist.R
import hr.algebra.triathlist.api.weatherCity
import hr.algebra.triathlist.api.weatherDescription
import hr.algebra.triathlist.api.weatherFeelsLike
import hr.algebra.triathlist.api.weatherTemp
import kotlinx.android.synthetic.main.fragment_weather.*

class WeatherFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showWeatherData()
    }

    @SuppressLint("SetTextI18n")
    private fun showWeatherData() {
        tvDescription.text = weatherDescription
        tvCityName.text = weatherCity
        tvTemperature.text = "${weatherTemp.toInt()}°C"
        tvFeelsLike.text = "Feels like ${weatherFeelsLike.toInt()}°C"
    }
}
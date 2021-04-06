package hr.algebra.triathlist.api

import android.content.Context
import android.util.Log
import hr.algebra.triathlist.framework.sendBroadcast
import hr.algebra.triathlist.model.weather.WeatherInfo
import hr.algebra.triathlist.services.WeatherReceiver
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

var weatherDescription = "No description"
var weatherCity = "Not available"
var weatherTemp = 0.0
var weatherFeelsLike = 0.0

class WeatherFetcher(private val context: Context) {
    private var weatherAPI: WeatherAPI

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        weatherAPI = retrofit.create(WeatherAPI::class.java)
    }

    fun fetchData(lat: Double, long: Double) {
        val request = weatherAPI.getWeatherResponse(
            mapOf(
                "lat" to lat.toString(),
                "lon" to long.toString(),
                "units" to "metric",
                "exclude" to "minutely,hourly,daily,alerts",
                "appid" to API_KEY
            )
        )
        request.enqueue(object : Callback<WeatherInfo> {
            override fun onFailure(call: Call<WeatherInfo>, t: Throwable) {
                Log.d(javaClass.name, t.message, t)
            }

            override fun onResponse(call: Call<WeatherInfo>, response: Response<WeatherInfo>) {
                if (response.body() != null) {
                    setWeatherData(response.body()!!)
                }
            }
        })
    }

    private fun setWeatherData(data: WeatherInfo) {
        GlobalScope.launch {
            weatherDescription = data.weather.first().main
            weatherCity = data.name
            weatherTemp = data.main.temp
            weatherFeelsLike = data.main.feelsLike
            context.sendBroadcast<WeatherReceiver>()
        }
    }
}
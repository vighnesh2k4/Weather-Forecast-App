package com.example.weatherapp

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {
    private val apiKey = "fe6b98e5bf4494753261706a0308528f"
    private val searchEditText: EditText by lazy { findViewById(R.id.search_edit_text) }
    private val weatherTextView: TextView by lazy { findViewById(R.id.weather_text_view) }
    private var isCelsius = true  // Default to Celsius

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search_button)
        val toggleButton = findViewById<Button>(R.id.toggle_button)

        searchButton.setOnClickListener {
            val cityName = searchEditText.text.toString()
            if (cityName.isNotBlank()) {
                fetchWeather(cityName)
            }
        }

        toggleButton.setOnClickListener {
            isCelsius = !isCelsius
            val cityName = searchEditText.text.toString()
            if (cityName.isNotBlank()) {
                fetchWeather(cityName)
            }
        }
    }

    private fun fetchWeather(cityName: String) {
        val units = if (isCelsius) "metric" else "imperial"
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$cityName&appid=$apiKey&units=$units"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val weatherData = response.optJSONObject("main")
                val temperature = weatherData?.optDouble("temp") ?: 0.0
                val description = response.optJSONArray("weather")?.optJSONObject(0)?.optString("description") ?: ""

                val weatherText = "Temperature: $temperature${if (isCelsius) "°C" else "°F"}, Description: $description"
                weatherTextView.text = weatherText
            },
            { error ->
                Log.e("WeatherApp", "Volley error: ${error.message}", error)
                Toast.makeText(this, "Failed to fetch weather data", Toast.LENGTH_SHORT).show()
            })

        Volley.newRequestQueue(this).add(request)
    }
}

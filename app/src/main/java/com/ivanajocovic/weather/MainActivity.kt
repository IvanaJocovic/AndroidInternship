package com.ivanajocovic.weather

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ivanajocovic.weather.databinding.ActivityMainBinding
import com.ivanajocovic.weather.networking.datasource.WeatherDataSource
import com.ivanajocovic.weather.ui.CityWeatherUi
import com.ivanajocovic.weather.ui.RecyclerViewAdapter
import com.ivanajocovic.weather.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: WeatherViewModel by viewModels()
    @Inject lateinit var dataSource: WeatherDataSource
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: RecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.loadSearchHistory()
        viewModel.getCityWeatherInfo(cityName = "London")
        setUpUi()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    Log.i("WeatherUiStateLog", "$uiState")
                    when(uiState){
                        is WeatherViewModel.WeatherUiState.Error -> Toast.makeText(this@MainActivity, uiState.exception.message, Toast.LENGTH_LONG).show()
                        is WeatherViewModel.WeatherUiState.Loading -> {}
                        is WeatherViewModel.WeatherUiState.NoContent -> {}
                        is WeatherViewModel.WeatherUiState.Success -> populateUi(uiState.data)
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchHistory.collectLatest { listOfCities ->
                    Log.i("WeatherUiStateLog", "$listOfCities")
                    adapter.updateData(listOfCities.map { entity -> entity.cityName })
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun populateUi(data: CityWeatherUi) {
        binding.cityTextView.text = data.cityName
        binding.tempTextView.text = data.temperature.toString().plus(" °C")
        binding.descriptionTextView.text = data.description.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
        Glide
            .with(this)
            .load("https://openweathermap.org/img/wn/"+data.icon+"@2x.png")
            .centerCrop()
            .into(binding.weatherImgView);
    }

    private fun setUpUi() {
        binding.btnSearch.setOnClickListener {
            viewModel.getCityWeatherInfo(cityName = binding.searchText.text.toString())
        }
        adapter = RecyclerViewAdapter(
            data = emptyList(),
            onDelete =  { cityName ->
                viewModel.deleteCity(cityName = cityName)
            },
            onClick = { cityName ->
                binding.searchText.text = SpannableStringBuilder(cityName)
                viewModel.getCityWeatherInfo(cityName = cityName)
            }
        )
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }
}
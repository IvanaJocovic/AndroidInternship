package com.ivanajocovic.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivanajocovic.weather.database.CityDao
import com.ivanajocovic.weather.database.CityEntity
import com.ivanajocovic.weather.networking.datasource.CityDataSource
import com.ivanajocovic.weather.networking.datasource.WeatherDataSource
import com.ivanajocovic.weather.ui.CityWeatherUi
import com.ivanajocovic.weather.usecase.TransformWeatherResponseToWeatherUiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val dataSource: WeatherDataSource,
    private val useCase: TransformWeatherResponseToWeatherUiUseCase,
    private val cityDataSource: CityDataSource
) : ViewModel() {

    private val _searchHistory = MutableSharedFlow<List<CityEntity>>()
    val searchHistory: SharedFlow<List<CityEntity>> = _searchHistory

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.NoContent)
    val uiState: StateFlow<WeatherUiState> = _uiState

    fun getCityWeatherInfo(cityName: String) {
        viewModelScope.launch {

            _uiState.update { WeatherUiState.Loading }
            try {
                val weatherResponse = dataSource.getForecast(
                    cityName = cityName
                )
                saveSearchHistory(cityName)
                val cityWeatherUi = useCase.invoke(weatherResponse)

                _uiState.update {
                    WeatherUiState.Success(cityWeatherUi)
                }
            } catch(e:Exception) {
                _uiState.update { WeatherUiState.Error(e) }
            }
        }
    }

    fun loadSearchHistory() {
        viewModelScope.launch {
            cityDataSource.getLast10City().collectLatest { history ->
                _searchHistory.emit(history)
            }
        }
    }

    fun deleteCity(cityName: String) {
        viewModelScope.launch {
            cityDataSource.deleteCitySearch(cityName)
        }
    }

    private fun saveSearchHistory(cityName: String) {
        viewModelScope.launch {
            val cityEntity = CityEntity(cityName, Instant.now().epochSecond)
            cityDataSource.insert(cityEntity)
            loadSearchHistory()
        }
    }


    sealed class WeatherUiState {
        data class Success(val data: CityWeatherUi): WeatherUiState()
        data class Error(val exception: Exception): WeatherUiState()
        object Loading: WeatherUiState()
        object NoContent: WeatherUiState()
    }
}
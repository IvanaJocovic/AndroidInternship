package com.ivanajocovic.weather.networking.datasource

import com.ivanajocovic.weather.database.CityDao
import com.ivanajocovic.weather.database.CityEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CityDataSource @Inject constructor(
    private val cityDao: CityDao
) {

    suspend fun insert(cityEntity: CityEntity) {
        withContext(Dispatchers.IO) {
            cityDao.insert(cityEntity)
        }
    }

    suspend fun deleteCitySearch(cityName: String) {
        withContext(Dispatchers.IO) {
            cityDao.delete(cityName)
        }
    }

    suspend fun getLast10City(): Flow<List<CityEntity>> {
        return withContext(Dispatchers.IO) {
            cityDao.getLast10City()
        }
    }

}
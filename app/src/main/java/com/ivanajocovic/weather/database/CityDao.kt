package com.ivanajocovic.weather.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @Query("SELECT * FROM CityEntity ORDER BY timestamp DESC LIMIT 10")
    fun getLast10City(): Flow<List<CityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cityEntity: CityEntity)

    @Query("DELETE FROM CityEntity WHERE cityName = :cityName")
    fun delete(cityName: String)

}
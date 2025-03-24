package com.example.sub2dicodingeventfun4.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sub2dicodingeventfun4.data.local.entity.EventEntity

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteEvent: EventEntity)

    @Query("DELETE FROM favorite_table WHERE id = :eventId")
    suspend fun delete(eventId: Int)

    @Query("SELECT * FROM favorite_table")
    fun getAllFavorites(): LiveData<List<EventEntity>>

    @Query("SELECT COUNT(*) FROM favorite_table WHERE id = :eventId")
    suspend fun isEventFavorite(eventId: Int): Int
}
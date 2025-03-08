package com.example.sub2dicodingeventfun4.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.sub2dicodingeventfun4.data.local.entity.EventEntity

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favorite: EventEntity)

    @Update
    fun update(favorite: EventEntity)

    @Delete
    fun delete(favorite: EventEntity)

    @Query("SELECT * from event ORDER BY id ASC")
    fun getAllNotes(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM event WHERE isBookmarked = 1")
    fun getFavoriteEvents(): LiveData<List<EventEntity>>
}
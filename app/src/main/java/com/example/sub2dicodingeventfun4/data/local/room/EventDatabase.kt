package com.example.sub2dicodingeventfun4.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sub2dicodingeventfun4.data.local.entity.EventEntity

@Database(entities = [EventEntity::class], version = 1)
abstract class EventDatabase : RoomDatabase() {
    abstract fun favoriteDao(): EventDao
    companion object {
        @Volatile
        private var INSTANCE: EventDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): EventDatabase {
            if (INSTANCE == null) {
                synchronized(EventDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        EventDatabase::class.java, "favorite_database")
                        .build()
                }
            }
            return INSTANCE as EventDatabase
        }
    }
}
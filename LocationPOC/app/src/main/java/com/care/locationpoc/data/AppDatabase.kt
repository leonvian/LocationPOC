package com.care.locationpoc.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GeofenceLog::class, LocationLog::class], version =1)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "logs"

        fun createDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java, DATABASE_NAME
            ).build()
        }

    }

    abstract fun locationLogDao(): LocationLogDAO

    abstract fun geofenceLogDao(): GeofenceLogDAO

}

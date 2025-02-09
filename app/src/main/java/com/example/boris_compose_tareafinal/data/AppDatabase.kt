package com.example.boris_compose_tareafinal.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UserDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
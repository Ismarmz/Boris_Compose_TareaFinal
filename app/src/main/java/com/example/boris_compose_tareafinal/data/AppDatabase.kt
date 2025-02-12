package com.example.boris_compose_tareafinal.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//We create our DB with this class

//database tag means this is a room database, and contains entity user from our table "users"
//is 3 version because room need to update the database when we change the entity
@Database(entities = [User::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    //We use this to use all the queries from usuarioDAo
    abstract fun usuarioDao(): UserDao

    //Is to make only 1 instance of app database in the whole life app
    companion object {
        //database instance, @volatile means that this variable will be visible to other threads
        @Volatile private var INSTANCE: AppDatabase? = null
        //returns the only instance of the database
        fun getInstance(context: Context): AppDatabase {
            //confirm that there is no other thread using the DB, to do not create a lot instances of the DB
            return INSTANCE ?: synchronized(this) {
                //create and database instance with the type of data
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    //name of the database
                    "app_database"
                )
                    .build()
                //We save it in the variable INSTANCE to avoid create it again
                INSTANCE = instance
                instance
            }
        }
    }
}
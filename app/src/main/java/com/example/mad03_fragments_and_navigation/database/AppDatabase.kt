package com.example.mad03_fragments_and_navigation.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mad03_fragments_and_navigation.models.Movie

@Database(
    entities = [
        Movie::class        // add all your tables here
    ],
    version = 2,             // define a database version -> needed for migrations
    exportSchema = false     // export schema needed -> we will not use this now but this would be for migration strategies
)
abstract class AppDatabase : RoomDatabase() {
    var context: Context? = null
    abstract fun movieDao(): MovieDao

    // Singleton prevents multiple instances of database opening at the same time.
    companion object{
        @Volatile // marking the instance as volatile to ensure atomic access to the variable eg.: is always up-to-date (never cached)
        private var INSTANCE: AppDatabase? = null

        private fun buildDatabase(context: Context): AppDatabase{
            return Room
                    .databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "AppDatabase"
                    )
                    // .addCallback()                       // you can define callback methods to for example prepopulate the database
                    .fallbackToDestructiveMigration()       // a migration strategy is needed - this example uses no real migration strategy, we going to rebuild the db on each version update
                    .build()
        }

        fun getDatabase(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){      // if INSTANCE is NULL -> call function to build database instance
                INSTANCE ?: buildDatabase(context).also{      // buildDatabase() and set the INSTANCE to database (it)
                    INSTANCE = it
                }
            }
        }
    }
}
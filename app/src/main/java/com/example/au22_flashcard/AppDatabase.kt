package com.example.au22_flashcard
//Credits to https://github.com/johncodeos-blog/RoomExample used as guide for this project room databas uppgift
//Other helpful links for ideas
// https://github.com/Cleveroad/AdaptiveTableLayout
//https://github.com/android/sunflower/tree/main


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(
    entities = [Word::class],
//    autoMigrations = [
//        AutoMigration (from = 1, to = 2)
//    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(WordConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
    companion object {
        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            if (INSTANCE == null) {
                synchronized(this) {
                    // Pass the database to the INSTANCE
                    INSTANCE = buildDatabase(context)
                }
            }
            // Return database.
            return INSTANCE!!
        }

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // The following query will add a new column called lastUpdate to the notes database
                database.execSQL("ALTER TABLE notes ADD COLUMN lastUpdate INTEGER NOT NULL DEFAULT 0")
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "words_database"
            )
                //.addMigrations(MIGRATION_1_2)
                .build()
        }


        //Same as getDatabase, decide to keep this one temporary
/*        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {

                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "word_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }*/
    }
}
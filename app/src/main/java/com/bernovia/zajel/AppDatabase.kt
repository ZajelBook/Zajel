package com.bernovia.zajel

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bernovia.zajel.bookList.data.BookDao
import com.bernovia.zajel.bookList.models.Book

@Database(entities = [Book::class], version = 2, exportSchema = false)

abstract class AppDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao


    companion object {

        private const val DATABASE_NAME = "zajel_DB"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).fallbackToDestructiveMigration().build()
        }
    }
}
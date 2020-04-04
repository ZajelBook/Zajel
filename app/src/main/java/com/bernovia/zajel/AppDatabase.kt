package com.bernovia.zajel

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bernovia.zajel.bookList.data.BookDao
import com.bernovia.zajel.bookList.models.Book
import com.bernovia.zajel.helpers.typeConverters.GenresTypeConverter
import com.bernovia.zajel.helpers.typeConverters.StringTypeConverter
import com.bernovia.zajel.messages.data.MessagesDao
import com.bernovia.zajel.messages.models.Message
import com.bernovia.zajel.notificationsList.data.NotificationsDao
import com.bernovia.zajel.notificationsList.models.Notification
import com.bernovia.zajel.requests.data.BookActivitiesDao
import com.bernovia.zajel.requests.models.BookActivity
import com.bernovia.zajel.splashScreen.data.MetaDataDao
import com.bernovia.zajel.splashScreen.models.MetaDataResponseBody

@Database(entities = [Book::class, MetaDataResponseBody::class, BookActivity::class, Message::class, Notification::class], version = 16, exportSchema = false)


@TypeConverters(StringTypeConverter::class, GenresTypeConverter::class) abstract class AppDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao
    abstract fun metaDataDao(): MetaDataDao
    abstract fun bookActivitiesDao(): BookActivitiesDao
    abstract fun messagesDao(): MessagesDao
    abstract fun notificationsDao(): NotificationsDao

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
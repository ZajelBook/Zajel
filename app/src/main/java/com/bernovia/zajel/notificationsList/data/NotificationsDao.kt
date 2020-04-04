package com.bernovia.zajel.notificationsList.data

import androidx.paging.DataSource
import androidx.room.*
import com.bernovia.zajel.notificationsList.models.Notification
import io.reactivex.Completable


@Dao interface NotificationsDao {

    @Transaction @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertAllNotifications(list: List<Notification>): Completable


    @Query("SELECT * FROM notification") fun allNotificationsPaginated(): DataSource.Factory<Int, Notification>

    @Query("DELETE FROM notification") fun deleteAllNotificationsList(): Completable

}
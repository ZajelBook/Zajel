package com.bernovia.zajel.splashScreen.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bernovia.zajel.splashScreen.models.MetaDataResponseBody
import io.reactivex.Completable

@Dao interface MetaDataDao {

    @Query("SELECT * FROM metaData") fun getMetaData(): LiveData<MetaDataResponseBody>

    @Transaction @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertMetaData(list: MetaDataResponseBody): Completable

    @Query("DELETE FROM metaData") fun deleteAllMetaData(): Completable

}
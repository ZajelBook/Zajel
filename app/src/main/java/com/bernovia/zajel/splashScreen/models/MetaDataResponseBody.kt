package com.bernovia.zajel.splashScreen.models


import androidx.room.*
import com.bernovia.zajel.helpers.typeConverters.GenresTypeConverter
import com.bernovia.zajel.helpers.typeConverters.StringTypeConverter
import com.google.gson.annotations.SerializedName


@Entity(tableName = "metaData", indices = [Index("id_meta")]) data class MetaDataResponseBody(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id_meta") val id: Int,
    @TypeConverters(GenresTypeConverter::class) @ColumnInfo(name = "genres_meta") @SerializedName("genres") val genres: List<Genre>?,
    @TypeConverters(StringTypeConverter::class) @ColumnInfo(name = "languages_meta") @SerializedName("languages") val languages: List<String>?)
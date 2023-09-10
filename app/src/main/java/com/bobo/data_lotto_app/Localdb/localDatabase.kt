package com.bobo.data_lotto_app.Localdb

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [
    NormalModeNumber::class,
], version = 1, exportSchema = false)
abstract class LocalDatabase: RoomDatabase() {

    abstract fun normalModeNumberDao() : NormalModeNumberDatabaseDao
}
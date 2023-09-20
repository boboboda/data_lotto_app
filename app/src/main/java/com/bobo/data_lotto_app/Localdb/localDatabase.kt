package com.bobo.data_lotto_app.Localdb

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [
    NormalModeNumber::class,
    BigDataModeNumber::class,
    LocalUserData::class
], version = 6, exportSchema = false)
abstract class LocalDatabase: RoomDatabase() {

    abstract fun normalModeNumberDao() : NormalModeNumberDatabaseDao

    abstract fun bigDataModeNumberDao() : BigdataModeNumberDatabaseDao

    abstract fun localUserDao(): LocalUserDatabaseDao
}
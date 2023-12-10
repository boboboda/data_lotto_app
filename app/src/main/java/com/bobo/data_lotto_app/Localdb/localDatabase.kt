package com.bobo.data_lotto_app.Localdb

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [
    NormalModeNumber::class,
    BigDataModeNumber::class,
    LocalUserData::class,
    Lotto::class
], version = 10, exportSchema = true)
abstract class LocalDatabase: RoomDatabase() {

    abstract fun normalModeNumberDao() : NormalModeNumberDatabaseDao

    abstract fun bigDataModeNumberDao() : BigdataModeNumberDatabaseDao

    abstract fun allLottoNumberDao() : AllLottoNumberDatabaseDao

    abstract fun localUserDao(): LocalUserDatabaseDao
}
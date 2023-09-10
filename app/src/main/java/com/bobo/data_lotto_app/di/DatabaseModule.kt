package com.bobo.data_lotto_app.di

import android.content.Context
import androidx.room.Room
import com.bobo.data_lotto_app.Localdb.LocalDatabase
import com.bobo.data_lotto_app.Localdb.NormalModeNumberDatabaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent:: class)
@Module
private object DatabaseModule {

    @Provides
    fun provideBuyDollarRecordDao(localDatabase: LocalDatabase) : NormalModeNumberDatabaseDao {
        return localDatabase.normalModeNumberDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) : LocalDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            LocalDatabase::class.java,
            "locals_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
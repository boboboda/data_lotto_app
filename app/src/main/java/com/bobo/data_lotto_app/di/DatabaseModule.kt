package com.bobo.data_lotto_app.di

import android.content.Context
import androidx.room.Room
import com.bobo.data_lotto_app.Localdb.AllLottoNumberDatabaseDao
import com.bobo.data_lotto_app.Localdb.BigdataModeNumberDatabaseDao
import com.bobo.data_lotto_app.Localdb.LocalDatabase
import com.bobo.data_lotto_app.Localdb.LocalUserDatabaseDao
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
    fun provideNormalModeNumberDao(localDatabase: LocalDatabase) : NormalModeNumberDatabaseDao {
        return localDatabase.normalModeNumberDao()
    }

    @Provides
    fun provideBigModeNumberDao(localDatabase: LocalDatabase) : BigdataModeNumberDatabaseDao {
        return localDatabase.bigDataModeNumberDao()
    }

    @Provides
    fun provideLocalUserDao(localDatabase: LocalDatabase): LocalUserDatabaseDao {
        return localDatabase.localUserDao()
    }

    @Provides
    fun provideAllLottoNumberDao(allLocalDatabase: LocalDatabase): AllLottoNumberDatabaseDao {
        return allLocalDatabase.allLottoNumberDao()
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
package com.bobo.data_lotto_app.di

import com.bobo.data_lotto_app.Localdb.AllLottoNumberDatabaseDao
import com.bobo.data_lotto_app.Localdb.BigdataModeNumberDatabaseDao
import com.bobo.data_lotto_app.Localdb.LocalRepository
import com.bobo.data_lotto_app.Localdb.LocalUserDatabaseDao
import com.bobo.data_lotto_app.Localdb.NormalModeNumber
import com.bobo.data_lotto_app.Localdb.NormalModeNumberDatabaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideLocalRepository(
        normalModeNumberDatabaseDao: NormalModeNumberDatabaseDao,
        bigdataModeNumberDatabaseDao: BigdataModeNumberDatabaseDao,
        userDatabaseDao: LocalUserDatabaseDao,
        allLottoNumberDatabaseDao: AllLottoNumberDatabaseDao): LocalRepository {
        return LocalRepository(
            normalModeNumberDatabaseDao,
            bigdataModeNumberDatabaseDao,
            userDatabaseDao,
            allLottoNumberDatabaseDao)
    }
}
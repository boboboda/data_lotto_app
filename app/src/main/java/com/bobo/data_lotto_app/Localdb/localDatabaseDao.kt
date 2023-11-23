package com.bobo.data_lotto_app.Localdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@Dao
interface NormalModeNumberDatabaseDao {

    @Query("SELECT * from NormalModeNumber_table")
    fun getRecords(): Flow<List<NormalModeNumber>>

    @Query("SELECT * from NormalModeNumber_table where id=:id")
    suspend fun getById(id: String): NormalModeNumber

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(normalModeNumber: NormalModeNumber)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(normalModeNumber: NormalModeNumber)

    @Query("DELETE from NormalModeNumber_table")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteNote(normalModeNumber: NormalModeNumber)
}

@Dao
interface BigdataModeNumberDatabaseDao {

    @Query("SELECT * from BigDataModeNumber_table")
    fun getRecords(): Flow<List<BigDataModeNumber>>

    @Query("SELECT * from BigDataModeNumber_table where id=:id")
    suspend fun getById(id: String): BigDataModeNumber

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bigDataModeNumber: BigDataModeNumber)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(bigDataModeNumber: BigDataModeNumber)

    @Query("DELETE from BigDataModeNumber_table")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteNote(bigDataModeNumber: BigDataModeNumber)
}

@Dao
interface LocalUserDatabaseDao {

    @Query("SELECT * from LocalUserData_table")
    fun getUserData(): Flow<LocalUserData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(localUserData: LocalUserData)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(localUserData: LocalUserData)

    @Query("DELETE from LocalUserData_table")
    suspend fun deleteAll()
    @Delete
    suspend fun deleteNote(localUserData: LocalUserData)
}


@Dao
interface AllLottoNumberDatabaseDao {
    @Query("SELECT * from AllLottoNumber_table")
    fun getAllLottoNumber(): Flow<List<Lotto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lotto: Lotto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(lottoList: List<Lotto>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(lotto: Lotto)

    @Query("DELETE from AllLottoNumber_table")
    suspend fun deleteAll()
    @Delete
    suspend fun deleteNote(lotto: Lotto)
}


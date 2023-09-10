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






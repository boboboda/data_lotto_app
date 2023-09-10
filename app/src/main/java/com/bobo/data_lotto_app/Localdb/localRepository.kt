package com.bobo.data_lotto_app.Localdb

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LocalRepository @Inject constructor(
    private val normalModeNumberDatabaseDao: NormalModeNumberDatabaseDao,
    ) {


    // 달러
    suspend fun add(normalModeNumber: NormalModeNumber) = normalModeNumberDatabaseDao.insert(normalModeNumber)
    suspend fun update(normalModeNumber: NormalModeNumber) = normalModeNumberDatabaseDao.update(normalModeNumber)
    suspend fun delete(normalModeNumber: NormalModeNumber) = normalModeNumberDatabaseDao.deleteNote(normalModeNumber)
    suspend fun deleteAll() = normalModeNumberDatabaseDao.deleteAll()
    fun getAll(): Flow<List<NormalModeNumber>> = normalModeNumberDatabaseDao.getRecords().flowOn(Dispatchers.IO).conflate()

}
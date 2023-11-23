package com.bobo.data_lotto_app.Localdb

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LocalRepository @Inject constructor(
    private val normalModeNumberDatabaseDao: NormalModeNumberDatabaseDao,
    private  val bigdataModeNumberDatabaseDao: BigdataModeNumberDatabaseDao,
    private val localUserDatabaseDao: LocalUserDatabaseDao,
    private val allLottoNumberDatabaseDao: AllLottoNumberDatabaseDao
    ) {

    // 모든 로또번호 로컬 저장
    suspend fun allLottoNumberAdd(lotto: Lotto) = allLottoNumberDatabaseDao.insert(lotto)
    suspend fun allLottoNumberAllAdd(lottos: List<Lotto>) = allLottoNumberDatabaseDao.insertAll(lottos)
    suspend fun allLottoNumberUpdate(lotto: Lotto) = allLottoNumberDatabaseDao.update(lotto)
    suspend fun allLottoNumberDelete(lotto: Lotto) = allLottoNumberDatabaseDao.deleteNote(lotto)
    suspend fun allLottoNumberDeleteAll() = allLottoNumberDatabaseDao.deleteAll()
    fun allLottoNumberGetAll(): Flow<List<Lotto>> = allLottoNumberDatabaseDao.getAllLottoNumber().flowOn(Dispatchers.IO).conflate()

    // 노말모드
    suspend fun add(normalModeNumber: NormalModeNumber) = normalModeNumberDatabaseDao.insert(normalModeNumber)
    suspend fun update(normalModeNumber: NormalModeNumber) = normalModeNumberDatabaseDao.update(normalModeNumber)
    suspend fun delete(normalModeNumber: NormalModeNumber) = normalModeNumberDatabaseDao.deleteNote(normalModeNumber)
    suspend fun deleteAll() = normalModeNumberDatabaseDao.deleteAll()
    fun getAll(): Flow<List<NormalModeNumber>> = normalModeNumberDatabaseDao.getRecords().flowOn(Dispatchers.IO).conflate()

    // 빅데이터 모드

    suspend fun bigDataAdd(bigDataModeNumber: BigDataModeNumber) = bigdataModeNumberDatabaseDao.insert(bigDataModeNumber)
    suspend fun bigDataUpdate(bigDataModeNumber: BigDataModeNumber) = bigdataModeNumberDatabaseDao.update(bigDataModeNumber)
    suspend fun bigDataDelete(bigDataModeNumber: BigDataModeNumber) = bigdataModeNumberDatabaseDao.deleteNote(bigDataModeNumber)
    suspend fun bigDataDeleteAll() = bigdataModeNumberDatabaseDao.deleteAll()
    fun bigdataGetAll(): Flow<List<BigDataModeNumber>> = bigdataModeNumberDatabaseDao.getRecords().flowOn(Dispatchers.IO).conflate()

    // 로컬 유저 생성

    suspend fun localUserAdd(localUserData: LocalUserData) = localUserDatabaseDao.insert(localUserData)
    suspend fun localUserUpdate(localUserData: LocalUserData) = localUserDatabaseDao.update(localUserData)
    suspend fun localUserDataDelete() = localUserDatabaseDao.deleteAll()
    fun localUserDataGet(): Flow<LocalUserData> = localUserDatabaseDao.getUserData().flowOn(Dispatchers.IO).conflate()


}
package com.bobo.data_lotto_app.ViewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobo.data_lotto_app.MainActivity.Companion.TAG
import com.bobo.data_lotto_app.service.Lotto
import com.bobo.data_lotto_app.service.LottoApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

class DataViewModel: ViewModel() {

    init {
        viewModelScope.launch {
            allFetched()

//            delay(1000)
//            searchLotto()

            if(allLottoNumberDataFlow != null) {
                Log.d(TAG, "${allLottoNumberDataFlow.value}")
            } else {
                return@launch
            }
        }
    }

    val dataCardId = MutableStateFlow(1)

    // 전체 로또 번호 데이터
    private val _allLottoNumberDataFlow = MutableStateFlow<List<Lotto>>(emptyList())

    val allLottoNumberDataFlow = _allLottoNumberDataFlow.asStateFlow()

    // 범위가 선택된 로또 번호 데이터

    private val _selectRangeLottoNumber = MutableStateFlow<List<Lotto>>(emptyList())

    var selectRangeLottoNumber = _selectRangeLottoNumber.asStateFlow()


    // 유저 선택 값
    val testNumbers: List<Int> = listOf(1, 2, 11, 16, 21, 22)

    val sameBallTwo = mutableStateOf(0)

    // 업로드 로또 모든 로또 번호
    fun allFetched() {
        viewModelScope.launch {
            var response = LottoApi.retrofitService.fetchLottos()

            Log.d(TAG, "모든 로또번호 - ${response}")



            response?.let{
               val mapDataResponse = it.lottos
                _allLottoNumberDataFlow.emit(mapDataResponse!!)

                _selectRangeLottoNumber.emit(mapDataResponse)

                resentLottoCall()
            } ?: Log.d(TAG, "allLotto 데이터가 없습니다.")
        }
    }

    // 최근 로또 번호 추출

    val resentLottoNumber = MutableStateFlow<Lotto>(Lotto(drwNo = 0, drwtNo1 = 1, drwtNo2 = 2, drwtNo3 = 3, drwtNo4 = 4, drwtNo5 = 5, drwtNo6 = 6, bnusNo = 7, totSellamnt = "0", firstPrzwnerCo = 0, firstWinamnt = "0"))

    fun resentLottoCall() {
        val lottoNumber = allLottoNumberDataFlow.value.map { it.drwNo }

        Log.d(TAG, "resentNumber ${lottoNumber}")

        val maxValue = lottoNumber.maxByOrNull { it!! } ?: throw Exception("null max value")

        Log.d(TAG, "resentNumber ${maxValue}")

        val filterLotto = allLottoNumberDataFlow.value.filter { it.drwNo == maxValue }.lastOrNull()

        viewModelScope.launch {
            resentLottoNumber.emit(filterLotto!!)
        }
    }

    // 로또 날짜 범위 값

    val startDateFlow = MutableStateFlow("${LocalDate.now()}")

    val endDateFlow = MutableStateFlow("${LocalDate.now()}")

    val startFilterRecordFlow = allLottoNumberDataFlow.combine(startDateFlow.filterNot { it.isEmpty() }) { recordList, startDate ->
        recordList.filter { it.drwNoDate!! >= startDate } }

    var endFilterRecordFlow = startFilterRecordFlow.combine(endDateFlow) { sellRecordList, endDate ->
        sellRecordList.filter { it.drwNoDate!! <= endDate }
    }

    val testValue = MutableStateFlow<Lotto>(
        Lotto()
    )

    val cgValue = testValue.replayCache

    val endFilerStateFlow = endFilterRecordFlow.stateIn(viewModelScope, SharingStarted.Eagerly, cgValue)

    fun filterRange() {
        viewModelScope.launch {
            _selectRangeLottoNumber.emit(endFilerStateFlow.value)
        }

    }




    //데이터 리스트 조회
    fun calculate(
        filterNumber: String,
    ): Float {
        val selectData = _selectRangeLottoNumber.value
        val numberFilterData : List<Lotto> = selectData.filter { it.hasNumber(filterNumber) }
        val rangeCount = _selectRangeLottoNumber.value.count()
        val countNumber = numberFilterData.count()

        Log.d(TAG, "number:${filterNumber} rangeCount: ${rangeCount}, filterNumber:${countNumber}")
        val result = (countNumber.toFloat()/rangeCount.toFloat()) * 100

        return result
    }



    fun searchLotto () {
        allLottoNumberDataFlow.value.forEach {
            demoPlayForOneGame(numbers = testNumbers, it)
        }
    }

    // 지정된 날짜 범위
    fun demoPlayForOneGame(numbers: List<Int>,
                           rangeLottoNumber: Lotto = Lotto(
                               drwNo = 23,
                               drwNoDate = "2023-06-04",
                               drwtNo1 = 1,
                               drwtNo2 =  6,
                               drwtNo3 = 11,
                               drwtNo4 = 16,
                               drwtNo5 =  21,
                               drwtNo6 =  26))
            : List<Int>
    {

        val matchOrNotList : List<Pair<Int, Boolean>> = numbers.map { number ->
            val match = rangeLottoNumber.hasNumber("$number")
            val matchAndNumber = Pair(number, match)
            matchAndNumber
        }

        // 맞춘 숫자들
        val matchNumbers : List<Int> = matchOrNotList
            .filter { it.second }
            .map{ it.first }

        // 일치하는 값의 갯수
        val matchBoolean : List<Boolean> = matchOrNotList
            .map { it.second }
        Log.d(TAG, "트루 값 ${matchBoolean}")
        Log.d(TAG, "횟차 ${rangeLottoNumber.drwNo}")
        Log.d(TAG, "트루 값 숫자${matchNumbers}")

        // 한개 이상인 회차


        // 트루가 2개 이상인 회차
        val matchSameTwo = matchBoolean.count { it == true } >= 2
        viewModelScope.launch {
            if(matchSameTwo == true) {
                sameBallTwo.value ++
            }
        }

        Log.d(TAG, "트루 값 2개 이상 ${matchSameTwo}")
        Log.d(TAG, "트루 값이 2개 이상의 값의 갯수 ${sameBallTwo.value}")

        // 트루가 3개 이상인 회차

        return matchNumbers
    }
}


package com.bobo.data_lotto_app.ViewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobo.data_lotto_app.MainActivity.Companion.TAG
import com.bobo.data_lotto_app.service.Lotto
import com.bobo.data_lotto_app.service.LottoApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

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
    // 빅데이터
    private val _selectRangeLottoNumber = MutableStateFlow<List<Lotto>>(emptyList())

    var selectRangeLottoNumber = _selectRangeLottoNumber.asStateFlow()

    // 범위가 설정된 날짜에서 나의 로또 번호 데이터 조회

    private val _selectRangeMyLottoNumber = MutableStateFlow<List<Lotto>>(emptyList())

    var selectRangeMyLottoNumber = _selectRangeMyLottoNumber.asStateFlow()


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

    // 모든 로또 번호 로또 날짜 범위 값

    val startDateFlow = MutableStateFlow("${LocalDate.now()}")

    val endDateFlow = MutableStateFlow("${LocalDate.now()}")

    val startFilterRecordFlow = allLottoNumberDataFlow.combine(startDateFlow.filterNot { it.isEmpty() }) { recordList, startDate ->
        recordList.filter { it.drwNoDate!! >= startDate } }

    var endFilterRecordFlow = startFilterRecordFlow.combine(endDateFlow) { sellRecordList, endDate ->
        sellRecordList.filter { it.drwNoDate!! <= endDate }
    }

    val cgStateValue = MutableStateFlow<Lotto>(
        Lotto()
    )

    val cgValue = cgStateValue.replayCache

    val endFilerStateFlow = endFilterRecordFlow.stateIn(viewModelScope, SharingStarted.Eagerly, cgValue)

    fun filterRange() {
        viewModelScope.launch {
            _selectRangeLottoNumber.emit(endFilerStateFlow.value)
        }

    }




    // 나의 로또 번호 조회를 위한 로또 날짜 범위 설정

    val myNumberStartDateFlow = MutableStateFlow("${LocalDate.now()}")

    val myNumberEndDateFlow = MutableStateFlow("${LocalDate.now()}")

    val startFilterMyNumber = allLottoNumberDataFlow.combine(myNumberStartDateFlow.filterNot { it.isEmpty() }) { recordList, startDate ->
        recordList.filter { it.drwNoDate!! >= startDate } }

    var endFilterMyNumber = startFilterMyNumber.combine(endDateFlow) { sellRecordList, endDate ->
        sellRecordList.filter { it.drwNoDate!! <= endDate }
    }


    val myNumberStateFlow = endFilterMyNumber.stateIn(viewModelScope, SharingStarted.Eagerly, allLottoNumberDataFlow.value)


    fun myNumberFilterRange() {
        viewModelScope.launch {
            _selectRangeMyLottoNumber.emit(myNumberStateFlow.value)
        }
    }


    // 입력한 나의 로또 번호

    val myNumberOneFlow = MutableStateFlow("")

    val myNumberTwoFlow = MutableStateFlow("")

    val myNumberThreeFlow = MutableStateFlow("")

    val myNumberFourFlow = MutableStateFlow("")

    val myNumberFiveFlow = MutableStateFlow("")

    val myNumberSixFlow = MutableStateFlow("")

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


    // 유저 선택 값

    val twoChunkNumberFlow = MutableStateFlow<List<List<Int>>>(emptyList())

    val threeChunkNumberFlow = MutableStateFlow<List<List<Int>>>(emptyList())

    val fourChunkNumberFlow = MutableStateFlow<List<List<Int>>>(emptyList())

    val fiveChunkNumberFlow = MutableStateFlow<List<List<Int>>>(emptyList())

    val sixChunkNumberFlow = MutableStateFlow<List<Int>>(emptyList())


    fun chunkMake(): ChunkNumber {

        val aOne = myNumberOneFlow.value.toIntOrNull()
        val aTwo = myNumberTwoFlow.value.toIntOrNull()
        val aThree = myNumberThreeFlow.value.toIntOrNull()
        val aFour = myNumberFourFlow.value.toIntOrNull()
        val aFive = myNumberFiveFlow.value.toIntOrNull()
        val aSix = myNumberSixFlow.value.toIntOrNull()

        val numbers = listOf(aOne!!, aTwo!!, aThree!! ,aFour!!, aFive!!, aSix!!)

        val twoValue = mutableListOf<List<Int>>()

        for (i in 0 until numbers.size) {
            for (j in i + 1 until numbers.size) {
                twoValue.add(listOf(numbers[i], numbers[j]))
            }
        }


        val threeValue = mutableListOf<List<Int>>()

        for (i in 0 until numbers.size) {
            for (j in i + 1 until numbers.size) {
                for (k in j + 1 until numbers.size) {
                    threeValue.add(listOf(numbers[i], numbers[j], numbers[k]))
                }
            }
        }

        val fourValue = mutableListOf<List<Int>>()

        for (i in 0 until numbers.size) {
            for (j in i + 1 until numbers.size) {
                for (k in j + 1 until numbers.size) {
                    for (l in k + 1 until numbers.size) {
                        fourValue.add(listOf(numbers[i], numbers[j], numbers[k], numbers[l]))
                    }
                }
            }
        }

        val fiveValue = mutableListOf<List<Int>>()

        for (i in 0 until numbers.size - 4) {
            fiveValue.add(listOf(numbers[i], numbers[i + 1], numbers[i + 2], numbers[i + 3], numbers[i + 4]))
        }

        return ChunkNumber(
            twoValue.distinct(),
            threeValue.distinct(),
            fourValue.distinct(),
            fiveValue.distinct(),
            numbers
            )
    }

    fun searchLotto (numbers: List<Int>): Pair<Int, Int> {
        var trueValueCount = 0

        val data = if(myNumberStateFlow.value.isEmpty()) { allLottoNumberDataFlow.value } else { myNumberStateFlow.value }

        val dataCount = data.count()

        data.forEach { lotto ->

            val result = demoPlayForOneGame(numbers = numbers, lotto)

            if(result) trueValueCount ++
        }
        return Pair(trueValueCount, dataCount)
    }

    fun demoPlayForOneGame(
        numbers: List<Int>,
        rangeLottoNumber: Lotto): Boolean
    {

            val matchOrNotList : List<Triple<Boolean, Int, Lotto>> = numbers.map { listNumber ->

                    val match = rangeLottoNumber.hasNumber("$listNumber")

                    val matchAndNumberAndRangeLottoNumber = Triple(match ,listNumber, rangeLottoNumber)

                    matchAndNumberAndRangeLottoNumber
            }

            // 일치하는 값의 갯수
            val matchBoolean : List<Boolean> = matchOrNotList
                .map { it.first }

          val trueValue =  when(numbers.count()) {
                2 -> {
                    // 트루가 2개 이상인 회차
                    val matchSameTwo = matchBoolean.count { it == true } >= 2

                    return matchSameTwo
                }
                3 -> {
                    // 트루가 3개 이상인 회차
                    val matchSameThree = matchBoolean.count { it == true } >= 3

                    return matchSameThree
                }

                4 -> {
                    val matchSameFour = matchBoolean.count { it == true } >= 4

                    return matchSameFour
                }

                5 -> {
                    val matchSameFive = matchBoolean.count { it == true } >= 5

                    return matchSameFive
                }

                6 -> {
                    val matchSameSix = matchBoolean.count { it == true } >= 6

                    return matchSameSix
                }

              else -> false
            }


        return trueValue
        }



}

data class ChunkNumber (
    val secondNumber: List<List<Int>>,
    val thirdNumber: List<List<Int>>,
    val fourthNumber: List<List<Int>>,
    val fifthNumber: List<List<Int>>,
    val sixthNumber: List<Int>)

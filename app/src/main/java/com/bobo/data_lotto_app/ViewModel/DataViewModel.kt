package com.bobo.data_lotto_app.ViewModel

import android.util.Log
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobo.data_lotto_app.Localdb.BigDataModeNumber
import com.bobo.data_lotto_app.Localdb.LocalRepository
import com.bobo.data_lotto_app.Localdb.NormalModeNumber
import com.bobo.data_lotto_app.MainActivity.Companion.TAG
import com.bobo.data_lotto_app.service.Lotto
import com.bobo.data_lotto_app.service.LottoApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.jetbrains.annotations.NotNull
import java.time.LocalDate
import java.util.Random
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class DataViewModel @Inject constructor(private val localRepository: LocalRepository): ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {


            allFetched()

            localRepository.getAll().distinctUntilChanged()
                .collect{ listNumber ->

                    if(listNumber.isNullOrEmpty()) {
                        Log.d(TAG, "Empty number list")
                    } else {
                        normalLottoNumberList.value = listNumber
                    }

                }




            if(allLottoNumberDataFlow != null) {
                Log.d(TAG, "${allLottoNumberDataFlow.value}")
            } else {
                return@launch
            }
        }
    }

    val dataCardId = MutableStateFlow(1)

    val lottoCardId = MutableStateFlow(1)

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

    var endFilterMyNumber = startFilterMyNumber.combine(myNumberEndDateFlow) { sellRecordList, endDate ->
        sellRecordList.filter { it.drwNoDate!! <= endDate }
    }

    val myNumberStateFlow = endFilterMyNumber.stateIn(viewModelScope, SharingStarted.Eagerly, allLottoNumberDataFlow.value)


    // 입력한 나의 로또 번호

    val myNumberOneFlow = MutableStateFlow("")

    val myNumberTwoFlow = MutableStateFlow("")

    val myNumberThreeFlow = MutableStateFlow("")

    val myNumberFourFlow = MutableStateFlow("")

    val myNumberFiveFlow = MutableStateFlow("")

    val myNumberSixFlow = MutableStateFlow("")

    //데이터 리스트 조회
    enum class ModeType {
        SEARCH, LOTTERY
    }
    fun calculate(
        filterNumber: String,
        type: ModeType = ModeType.SEARCH
    ): Float {

     when(type) {
            ModeType.SEARCH -> {
                val selectData = _selectRangeLottoNumber.value
                val numberFilterData : List<Lotto> = selectData.filter { it.hasNumber(filterNumber) }
                val rangeCount = _selectRangeLottoNumber.value.count()
                val countNumber = numberFilterData.count()

                Log.d(TAG, "number:${filterNumber} rangeCount: ${rangeCount}, filterNumber:${countNumber}")
                val result = (countNumber.toFloat()/rangeCount.toFloat()) * 100

                return result
            }

            ModeType.LOTTERY -> {
                val selectData = _selectRangeLottoNumber.value
                val numberFilterData : List<Lotto> = selectData.filter { it.hasNumber(filterNumber) }
                val rangeCount = _selectRangeLottoNumber.value.count()
                val countNumber = numberFilterData.count()

                Log.d(TAG, "number:${filterNumber} rangeCount: ${rangeCount}, filterNumber:${countNumber}")
                val result = (countNumber.toFloat()/rangeCount.toFloat()) * 100

                return result
            }
        }
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


    // 로또번호 추첨

    //일반
    val normalLottoNumberList = MutableStateFlow<List<NormalModeNumber>>(emptyList())

    val haveNormalNumberData = MutableStateFlow(NormalModeNumber())

    val normalFixNumber = MutableStateFlow<List<Int>>(emptyList())

    val normalRemoveNumber = MutableStateFlow<List<Int>>(emptyList())

    val viewRemoveNumber = MutableStateFlow<List<Int>>(emptyList())

    //빅데이터

    val bigDataLottoNumberList = MutableStateFlow<List<BigDataModeNumber>>(emptyList())

    val haveBigDataNumberData = MutableStateFlow(BigDataModeNumber())

    val bigDataFixNumber = MutableStateFlow<List<Int>>(emptyList())

    val bigDataRemoveNumber = MutableStateFlow<List<Int>>(emptyList())

    val bigDataViewRemoveNumber = MutableStateFlow<List<Int>>(emptyList())

    fun getNumberFromRange(rangeNumber: List<Int>): Pair<List<Int>, Int> {

        var tempNumberList = rangeNumber

        val randomNumber = rangeNumber.random()

        val finalNumberList = tempNumberList.filter { it != randomNumber }

        return Pair(finalNumberList, randomNumber)
    }

    fun bigDataGetNumberFromRange(rangeNumber: List<Int>): List<Int> {

        val list = listOf<Int>(1,2,3,4,5,6,7,8,9,10)

        val listSorted = rangeNumber.sorted()

        val firstCount = rangeNumber.count()/6

        val secondCount = rangeNumber.count()/5

        val thirdCount = rangeNumber.count()/4

        val fourCount = rangeNumber.count()/3

        val fiveCount = rangeNumber.count()/2

        val sixCount = rangeNumber.count()/1


        val listFirstTake  = listSorted.take(firstCount)

        val listFirstRandom = listFirstTake.random()

        val listSecondTake =  listSorted.toMutableList().apply {
            remove(listFirstRandom)
        }.toList().take(secondCount)

        val listSecondRandom = listSecondTake.random()

        val listThirdTake = listSecondTake.toMutableList().apply {
            remove(listSecondRandom)
        }.toList().take(thirdCount)

        return listOf(listFirstRandom, listSecondRandom)
    }



    enum class LotteryType {
        NORMAL,
        BIGDATA
    }
    fun lottery(
        removeNumber: List<Int>,
        modeType: LotteryType = LotteryType.NORMAL) {

        val rangeNumber = filterNumber(removeNumber)

        val sortData = rangeNumber.sorted()

        var randomNumberList = emptyList<Int>()

            when(modeType) {
            LotteryType.NORMAL -> {
                val (firstFilterList, firstRandomNumber) = getNumberFromRange(sortData)

                val (secondFilterList, secondRandomNumber) = getNumberFromRange(firstFilterList)

                val (thirdFilterList, thirdRandomNumber) = getNumberFromRange(secondFilterList)

                val (fourFilterList, fourRandomNumber) = getNumberFromRange(thirdFilterList)

                val (fifthFilterList, fifthRandomNumber) = getNumberFromRange(fourFilterList)

                val (sixthFilterList, sixthRandomNumber) = getNumberFromRange(fifthFilterList)

                randomNumberList = listOf<Int>(
                    firstRandomNumber,
                    secondRandomNumber,
                    thirdRandomNumber,
                    fourRandomNumber,
                    fifthRandomNumber,
                    sixthRandomNumber
                )
            }

            LotteryType.BIGDATA -> {
                val (firstFilterList, firstRandomNumber) = getNumberFromRange(sortData)

                val (secondFilterList, secondRandomNumber) = getNumberFromRange(firstFilterList)

                val (thirdFilterList, thirdRandomNumber) = getNumberFromRange(secondFilterList)

                val (fourFilterList, fourRandomNumber) = getNumberFromRange(thirdFilterList)

                val (fifthFilterList, fifthRandomNumber) = getNumberFromRange(fourFilterList)

                val (sixthFilterList, sixthRandomNumber) = getNumberFromRange(fifthFilterList)

                randomNumberList = listOf<Int>(
                    firstRandomNumber,
                    secondRandomNumber,
                    thirdRandomNumber,
                    fourRandomNumber,
                    fifthRandomNumber,
                    sixthRandomNumber
                )
            }
        }

        val sortList = randomNumberList.sorted()

        val fixNumberList = normalFixNumber.value.sorted()

        val firstNumber = if(fixNumberList.count() < 1) {
            sortList[0]
        } else fixNumberList[0]

        val secondNumber = if(fixNumberList.count() < 2) {
            sortList[1]
        } else fixNumberList[1]

        val thirdNumber = if(fixNumberList.count() < 3) {
            sortList[2]
        } else fixNumberList[2]

        val fourthNumber = if(fixNumberList.count() < 4) {
            sortList[3]
        } else fixNumberList[3]

        val fifthNumber = if(fixNumberList.count() < 5) {
            sortList[4]
        } else fixNumberList[4]

        val sixthNumber = if(fixNumberList.count() < 6) {
            sortList[5]
        } else fixNumberList[5]


        var sortCgList = listOf<Int>(
            firstNumber,
            secondNumber,
            thirdNumber,
            fourthNumber,
            fifthNumber,
            sixthNumber
        ).sorted()

        when(modeType) {
            LotteryType.NORMAL -> {

                val newData = NormalModeNumber(
                    id = UUID.randomUUID(),
                    firstNumber = sortCgList[0],
                    secondNumber = sortCgList[1],
                    thirdNumber = sortCgList[2],
                    fourthNumber = sortCgList[3],
                    fifthNumber = sortCgList[4],
                    sixthNumber = sortCgList[5])

                viewModelScope.launch {
                    haveNormalNumberData.emit(newData)
                }
            }
            LotteryType.BIGDATA -> {

                val newData = BigDataModeNumber(
                    id = UUID.randomUUID(),
                    firstNumber = sortCgList[0],
                    secondNumber = sortCgList[1],
                    thirdNumber = sortCgList[2],
                    fourthNumber = sortCgList[3],
                    fifthNumber = sortCgList[4],
                    sixthNumber = sortCgList[5])


                viewModelScope.launch {
                    haveBigDataNumberData.emit(newData)
                }
            }
        }


    }

    fun emitNormalNumber(
        modeType: LotteryType = LotteryType.NORMAL
    ) {
        when(modeType) {
            LotteryType.NORMAL -> {
                val newData = haveNormalNumberData.value

                viewModelScope.launch {
                    val list = normalLottoNumberList.value + newData

                    normalLottoNumberList.emit(list)

                    localRepository.add(newData)

                    Log.d(TAG, "체크 리스트 - ${list}")

                    Log.d(TAG, "체크 데이터 - ${newData}")

                    Log.d(TAG, "일반 로또번호 추첨 - ${normalLottoNumberList.value}")
                }
            }
            LotteryType.BIGDATA -> {
                val newData = haveBigDataNumberData.value

                viewModelScope.launch {
                    val list = bigDataLottoNumberList.value + newData

                    bigDataLottoNumberList.emit(list)

//                    localRepository.add(newData)

                    Log.d(TAG, "체크 리스트 - ${list}")

                    Log.d(TAG, "체크 데이터 - ${newData}")

                    Log.d(TAG, "일반 로또번호 추첨 - ${bigDataLottoNumberList.value}")
                }
            }
        }



    }

    fun filterNumber(
        removeNumber: List<Int>
    ): MutableList<Int> {

        // 제외수
        val rangeNumber = 1..45

        val listNumber: MutableList<Int> = rangeNumber.toMutableList()

        listNumber.removeAll(removeNumber)

        return listNumber
    }

    fun deleteNormalNumber(number: Any,
                           modeType: LotteryType = LotteryType.NORMAL) {

        when(modeType) {
            LotteryType.NORMAL -> {
                val mapNumber = number as NormalModeNumber
                viewModelScope.launch {
                    localRepository.delete(mapNumber)

                    val list = normalLottoNumberList.value
                    val removeLists = list.toMutableList().apply {
                        remove(mapNumber)
                    }.toList()

                    normalLottoNumberList.value = removeLists
                }
            }

            LotteryType.BIGDATA -> {
                val mapNumber = number as BigDataModeNumber
                viewModelScope.launch {
//                    localRepository.delete(mapNumber)

                    val list = bigDataLottoNumberList.value
                    val removeLists = list.toMutableList().apply {
                        remove(mapNumber)
                    }.toList()

                    bigDataLottoNumberList.value = removeLists
                }
            }

        }



    }


    // 빅데이터 추첨 모드
    //// 날짜 범위 설정
    val bigDataModeStartDateFlow = MutableStateFlow("${LocalDate.now()}")

    val bigDataModeEndDateFlow = MutableStateFlow("${LocalDate.now()}")

    val startFilterBigDataMode = allLottoNumberDataFlow.combine(bigDataModeStartDateFlow.filterNot { it.isEmpty() }) { recordList, startDate ->
        recordList.filter { it.drwNoDate!! >= startDate } }

    var endFilterBigDataMode = startFilterBigDataMode.combine(bigDataModeEndDateFlow) { sellRecordList, endDate ->
        sellRecordList.filter { it.drwNoDate!! <= endDate }
    }

    val bigDataModeRangeNumberStateFlow = endFilterBigDataMode.stateIn(viewModelScope, SharingStarted.Eagerly, allLottoNumberDataFlow.value)

//fun percentage() {
//    val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
//
//// probabilities 에 숫자의 인덱스와 확률을 매핑합니다.
//
//    val probabilities = mutableMapOf<Int, Double>()
//    for (i in 0..9) {
//        probabilities[i] = (100 - (i * 10)) / 100.0
//    }
//
//// numbers 에서 확률에 따라 무작위 숫자를 선택합니다.
//
//    fun getRandomNumber(numbers: List<Int>, probabilities: Map<Int, Double>): Int {
//        // 인덱스 범위를 벗어난 값이 선택되었을 경우 예외처리하고 5% 확률로 숫자를 선택합니다.
//
//        val randomNumber = if (numbers.size > probabilities.size - 1) {
//            numbers.random(0.05)
//        } else {
//            numbers.random(probabilities)
//        }
//
//        return randomNumber
//    }
//
//// 랜덤 숫자를 출력합니다.
//
//    for (i in 1..20) {
//        val randomNumber = getRandomNumber(numbers, probabilities)
//        println("랜덤 숫자: $randomNumber")
//    }
//}



}

data class ChunkNumber (
    val secondNumber: List<List<Int>>,
    val thirdNumber: List<List<Int>>,
    val fourthNumber: List<List<Int>>,
    val fifthNumber: List<List<Int>>,
    val sixthNumber: List<Int>)





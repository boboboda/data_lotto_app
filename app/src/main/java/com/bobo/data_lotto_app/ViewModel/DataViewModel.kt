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

        val maxValue = lottoNumber.maxByOrNull { it!! } ?: throw Exception("null max value")


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
        filterNumber: String? = null,
        type: ModeType = ModeType.SEARCH
    ): Any {

     when(type) {
            ModeType.SEARCH -> {
                val selectData = _selectRangeLottoNumber.value
                val numberFilterData : List<Lotto> = selectData.filter { it.hasNumber(filterNumber!!) }
                val rangeCount = _selectRangeLottoNumber.value.count()
                val countNumber = numberFilterData.count()

                val result = (countNumber.toFloat()/rangeCount.toFloat()) * 100

                return result
            }

            ModeType.LOTTERY -> {

                Log.d(TAG, "범위 퍼센트 계산 실행")

                val lottoNumber = (1..45).toList()

                var results : MutableList<Pair<Int, Float>> = mutableListOf()

                Log.d(TAG, "빅데이터 범위 로또번호 ${bigDataModeRangeNumberStateFlow.value}")

                if(!bigDataModeRangeNumberStateFlow.value.isEmpty()) {
                    lottoNumber.forEach {number ->
                        val selectData = bigDataModeRangeNumberStateFlow.value
                        val numberFilterData : List<Lotto> = selectData.filter { it.hasNumber(number.toString()) }
                        val rangeCount = bigDataModeRangeNumberStateFlow.value.count()
                        val countNumber = numberFilterData.count()

                        val result = (countNumber.toFloat()/rangeCount.toFloat()) * 100
                        Log.d(TAG, "범위 퍼센트 값 ${number} , ${result}")
                        results.add(Pair(number, result))
                    }
                } else {

                    lottoNumber.forEach {number ->
                        val selectData = allLottoNumberDataFlow.value
                        val numberFilterData : List<Lotto> = selectData.filter { it.hasNumber(number.toString()) }
                        val rangeCount = allLottoNumberDataFlow.value.count()
                        val countNumber = numberFilterData.count()

                        val result = (countNumber.toFloat()/rangeCount.toFloat()) * 100
                        Log.d(TAG, "범위 퍼센트 값 ${number} , ${result}")
                        results.add(Pair(number, result))
                    }
                }

                return results
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

    val bigDataNumberAndPercentValue = MutableStateFlow<List<Pair<Int, Float>>>(emptyList())

    fun getNumberFromRange(rangeNumber: List<Int>): Pair<List<Int>, Int> {

        var tempNumberList = rangeNumber

        val randomNumber = rangeNumber.random()

        val finalNumberList = tempNumberList.filter { it != randomNumber }

        return Pair(finalNumberList, randomNumber)
    }


    object RandomLottoNumberService {

        // 1 ~ 45번 뽑는 로직
        private fun getRandomNumber(rangeNumber: List<Int>) : Int {

            return rangeNumber.random()
        }

        // atPercent
        private fun getCertainNumber(atPercent : Int, desiredNumber: Int, rangeNumber: List<Int>) : Int {
            val randomValue = (0..100).random()
            return if (randomValue <= atPercent) {
                desiredNumber
            } else {
                getRandomNumber(rangeNumber)
            }
        }

        // (percent, desiredNumber)
        fun makeRandomLottoNumbers(data : List<Pair<Int, Int>>, rangeNumber: List<Int>) : List<Int> {
            return data.map { anItem ->
                val percent = anItem.first
                val desiredNumber = anItem.second
                this.getCertainNumber(percent, desiredNumber, rangeNumber)
            }
        }
    }

    fun bigDataGetNumberFromRange(rangeNumber: List<Int>): Pair<List<Int>, Int> {

        Log.d(TAG, "bigDataGetNumberFromRange 실행됨")

        val percentSet = RandomLottoNumberService.makeRandomLottoNumbers(
            listOf(
                Pair(90, rangeNumber[0]),
                Pair(85, rangeNumber[1]),
                Pair(80, rangeNumber[2]),
                Pair(75, rangeNumber[3]),
                Pair(70, rangeNumber[4]),
                Pair(65, rangeNumber[5]),
                Pair(60, rangeNumber[6]),
                Pair(65, rangeNumber[7]),
                Pair(60, rangeNumber[8]),
                Pair(55, rangeNumber[9]),
                Pair(50, rangeNumber[10]),
                Pair(45, rangeNumber[11]),
                Pair(40, rangeNumber[12]),
                Pair(35, rangeNumber[13]),
                Pair(30, rangeNumber[14]),
                Pair(25, rangeNumber[15]),
                Pair(20, rangeNumber[16]),
            ), rangeNumber = rangeNumber
        )

        var tempNumberList = rangeNumber

        val randomNumber = percentSet.first()

        val finalNumberList = tempNumberList.filter { it != randomNumber }

        Log.d(TAG, "${percentSet}, ${randomNumber}, ${finalNumberList}")

        return Pair(finalNumberList, randomNumber)
    }



    enum class LotteryType {
        NORMAL,
        BIGDATA
    }
    fun lottery(
        removeNumber: List<Int>,
        modeType: LotteryType = LotteryType.NORMAL) {

        Log.d(TAG, "추첨 실행됨")


        var randomNumberList :List<Int> = emptyList()

        when(modeType) {
            LotteryType.NORMAL -> {

                val rangeNumber = filterNumber(removeNumber)

                val sortData = rangeNumber.sorted()

                Log.d(TAG, "노말모드 추첨 실행됨")
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

                val rangeNumber = filterNumber(removeNumber, type = LotteryType.BIGDATA)

                Log.d(TAG, "빅데이터 추첨 실행됨")

                Log.d(TAG, "정렬된 값 ${rangeNumber}")
                val (firstFilterList, firstRandomNumber) = bigDataGetNumberFromRange(rangeNumber)

                val (secondFilterList, secondRandomNumber) = bigDataGetNumberFromRange(firstFilterList)

                val (thirdFilterList, thirdRandomNumber) = bigDataGetNumberFromRange(secondFilterList)

                val (fourFilterList, fourRandomNumber) = bigDataGetNumberFromRange(thirdFilterList)

                val (fifthFilterList, fifthRandomNumber) = bigDataGetNumberFromRange(fourFilterList)

                val (sixthFilterList, sixthRandomNumber) = bigDataGetNumberFromRange(fifthFilterList)

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

        val fixNumberList = bigDataFixNumber.value.sorted()

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

    fun emitNumber(
        modeType: LotteryType = LotteryType.NORMAL
    ) {
        when(modeType) {
            LotteryType.NORMAL -> {
                val newData = haveNormalNumberData.value

                viewModelScope.launch {
                    val list = normalLottoNumberList.value + newData

                    normalLottoNumberList.emit(list)

                    localRepository.add(newData)
                }
            }
            LotteryType.BIGDATA -> {
                val newData = haveBigDataNumberData.value

                viewModelScope.launch {
                    val list = bigDataLottoNumberList.value + newData

                    bigDataLottoNumberList.emit(list)

//                    localRepository.add(newData)
                }
            }
        }



    }

    fun filterNumber(
        removeNumber: List<Int>,
        type: LotteryType = LotteryType.NORMAL
    ): MutableList<Int> {

        when(type){
            LotteryType.NORMAL -> {
                val rangeNumber = 1..45

                val listNumber: MutableList<Int> = rangeNumber.toMutableList()

                listNumber.removeAll(removeNumber)

                return listNumber
            }
            LotteryType.BIGDATA -> {

                Log.d(TAG, "빅데이터 필터넘버 실행")
                val rangeNumber = bigDataNumberAndPercentValue.value.sortedByDescending { it.second }

                Log.d(TAG, "넘버퍼센트 정렬 ${rangeNumber}")

                val mapRangeNumber = rangeNumber.map { it.first }

                val listNumber: MutableList<Int> = mapRangeNumber.toMutableList()

                listNumber.removeAll(removeNumber)

                return listNumber
            }
        }
        // 제외수


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

    val bigDataDateRangeFlow =  MutableStateFlow<List<BigDataDate>>(emptyList())

    val startFilterBigDataMode = allLottoNumberDataFlow.combine(bigDataModeStartDateFlow.filterNot { it.isEmpty() }) { recordList, startDate ->
        recordList.filter { it.drwNoDate!! >= startDate } }

    var endFilterBigDataMode = startFilterBigDataMode.combine(bigDataModeEndDateFlow) { sellRecordList, endDate ->
        sellRecordList.filter { it.drwNoDate!! <= endDate }
    }

    val bigDataModeRangeNumberStateFlow = endFilterBigDataMode.stateIn(viewModelScope, SharingStarted.Eagerly, allLottoNumberDataFlow.value)


}

data class ChunkNumber (
    val secondNumber: List<List<Int>>,
    val thirdNumber: List<List<Int>>,
    val fourthNumber: List<List<Int>>,
    val fifthNumber: List<List<Int>>,
    val sixthNumber: List<Int>)


data class BigDataDate(
    val startDate: String? = null,
    val endDate: String? = null
)




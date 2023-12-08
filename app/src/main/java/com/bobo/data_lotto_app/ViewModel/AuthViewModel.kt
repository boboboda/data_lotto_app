package com.bobo.data_lotto_app.ViewModel

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobo.data_lotto_app.Localdb.LocalRepository
import com.bobo.data_lotto_app.Localdb.LocalUserData
import com.bobo.data_lotto_app.Localdb.Lotto
import com.bobo.data_lotto_app.MainActivity
import com.bobo.data_lotto_app.MainActivity.Companion.TAG
import com.bobo.data_lotto_app.ViewModel.AuthViewModel.Companion.USER
import com.bobo.data_lotto_app.components.UseType
import com.bobo.data_lotto_app.firebase.AuthRepository
import com.bobo.data_lotto_app.firebase.Resource
import com.bobo.data_lotto_app.service.EmailRequest
import com.bobo.data_lotto_app.service.User
import com.bobo.data_lotto_app.service.UserApi
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.isActive
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Calendar
import java.util.Timer
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.concurrent.timerTask
import kotlin.reflect.jvm.internal.impl.load.java.lazy.descriptors.DeclaredMemberIndex.Empty

@HiltViewModel
class AuthViewModel @Inject
    constructor(application: Application,
                private val localRepository: LocalRepository,
                private val authRepository: AuthRepository
            ): AndroidViewModel(application) {

    companion object {
        const val KAKAO = "카카오"
        const val USER = "유저"
    }

    //로컬에서 받아온 유저 데이터
    val localUser = MutableStateFlow<LocalUserData>(LocalUserData())

    // 로그인 후 받아온 유저 데이터
    val receiveUserDataFlow = MutableStateFlow<User>(User())

    private val context = application.applicationContext

    val isLoggedIn = MutableStateFlow<Boolean>(false)

    val isLoadingFlow = MutableStateFlow(false)

    val kakaoisLoadingFlow = MutableStateFlow(false)

    val needAuthContext = MutableStateFlow(false)

    val allNumberSearchCountFlow = MutableStateFlow(0)

    val myNumberSearchCountFlow = MutableStateFlow(0)

    val numberLotteryCountFlow = MutableStateFlow(0)

    val db = Firebase.firestore

    val userDb = Firebase.auth

    init {

//        testApi()

        //로그인 상태에 따라 유저 데이터 변경
        viewModelScope.launch {
            isLoggedIn.collectLatest {
                if(isLoggedIn.value) {
                    Log.d(USER, "로그인 후 유저 데이터 변경 실행")
                    if(receiveUserDataFlow.value != null) {
                        allNumberSearchCountFlow.emit(receiveUserDataFlow.value.allNumberSearchCount!!)
                        myNumberSearchCountFlow.emit(receiveUserDataFlow.value.myNumberSearchCount!!)
                        numberLotteryCountFlow.emit(receiveUserDataFlow.value.allNumberSearchCount!!)
                    } else { return@collectLatest }
                } else {
                    viewModelScope.launch(Dispatchers.IO) {
                        localRepository.localUserDataGet().distinctUntilChanged()
                            .collect{userData ->
                                if(userData == null) {
                                    localUserAdd()
                                    Log.d(USER, "로컬 유저 아이디 없음") }
                                else {

                                    Log.d(USER, "로컬 유저 불러옴 실행 ${userData}")

                                    localUser.emit(userData)

                                    allNumberSearchCountFlow.emit(userData.allNumberSearchCount!!)
                                    myNumberSearchCountFlow.emit(userData.myNumberSearchCount!!)
                                    numberLotteryCountFlow.emit(userData.allNumberSearchCount!!)

                                    //토요일 10시 이후 횟수 초기화
                                    userDataReSet()

                                    // 관리자 권한으로 횟수 리셋함 출시 때 삭제
//                                    adminReSetLocalUserCount()
                                }
                            }
                    }
                }
            }
        }
    }
    fun localUserAdd() {

        viewModelScope.launch {
            Log.d(USER, "로컬 유저 아이디 없음, 로컬 유저 생성 로직 실행")

            val createGuestUser = LocalUserData(
                id = UUID.randomUUID(),
                allNumberSearchCount = 3,
                myNumberSearchCount = 3,
                numberLotteryCount = 3
            )

            localRepository.localUserAdd(createGuestUser)

            allNumberSearchCountFlow.emit(3)
            myNumberSearchCountFlow.emit(3)
            numberLotteryCountFlow.emit(3)
        }
    }


    fun deleteUserId() {
        viewModelScope.launch {
            localRepository.localUserDataDelete()
        }
    }




    // 로그인

    val logInEmailInputFlow = MutableStateFlow("")

    val logInPasswordInputFlow = MutableStateFlow("")

    val failedLogIn = MutableStateFlow(false)

    //회원가입

    val registerEmailInputFlow = MutableStateFlow<String>("")

    val registerNicknameFlow = MutableStateFlow<String>("")

    val registerPasswordInputFlow = MutableStateFlow<String>("")

    val registerPasswordConfirmInputFlow = MutableStateFlow<String>("")

    val registerIsLoadingFlow = MutableStateFlow<Boolean>(false)

    val kakaoUserEmailFlow = MutableStateFlow("")

    val kakaoUsernameFlow = MutableStateFlow("")

    val registerSuccessFlow = MutableStateFlow(false)

    val userAuthState = MutableStateFlow(false)

    fun registerUser() = viewModelScope.launch {
        authRepository.registerUser(registerEmailInputFlow.value, registerPasswordInputFlow.value).collect{ result ->
            when(result) {
                is Resource.Success -> {
                    Log.d(TAG, "가입성공")

                    val firebaseCurrentEmail = userDb.currentUser?.let { it.email }

                    Log.d(TAG, "이메일 인증 토큰 ${firebaseCurrentEmail}")

                    //이메일 인증 api 호출

                    val emailResponse = UserApi.retrofitService.sendEmailVerification(
                        request = EmailRequest(email = "kju9038@gmail.com")
                    )

                    if(emailResponse.message == "success") {
                        Log.d(TAG, "이메일 인증 전송 성공 ${emailResponse.email}")

                        val newDocumentRef = db.collection("users").document()

                        val time = Calendar.getInstance().time.toString()

                    val newUserData = User(
                        id = newDocumentRef.id,
                        deviceId = localUser.value.id.toString(),
                        type = "default",
                        authState = false,
                        signupVerifyToken = emailResponse.email?.token,
                        username = registerNicknameFlow.value,
                        email = firebaseCurrentEmail,
                        payment = false,
                        paymentStartDate = null,
                        paymentEndDate = null,
                        allNumberSearchCount = allNumberSearchCountFlow.value,
                        myNumberSearchCount = myNumberSearchCountFlow.value,
                        numberLotteryCount = numberLotteryCountFlow.value,
                        createdAt = time,
                        updatedAt = time,


                    )
                    newDocumentRef
                        .set(newUserData.asHasMap())
                        .addOnSuccessListener { result->
                            Log.d(TAG, "유저데이터 저장 성공")
                            viewModelScope.launch {
                                delay(500)
                                registerIsLoadingFlow.emit(false)
                                registerSuccessFlow.emit(true)

                                registerEmailInputFlow.emit("")
                                registerPasswordInputFlow.emit("")
                                registerPasswordConfirmInputFlow.emit("")
                                registerNicknameFlow.emit("")
                            }

                        }
                        .addOnFailureListener {
                            Log.d(TAG, "유저데이터 저장 실패 ${it}")
                        }

                    } else {
                        Log.d(TAG, "이메일 인증 전송 실패")
                    }


                }
                is Resource.Loading -> {
                    registerIsLoadingFlow.emit(false)
                }
                is Resource.Error -> {
                    registerIsLoadingFlow.emit(false)
                }

                else -> { }
            }
        }
    }

    enum class LoginType(name: String) {
        DEFAULT(name = "default"),
        KAKAO(name = "kakao")
    }
    fun loginUser(type: LoginType, activity: Activity? = null) {

        when(type) {
            LoginType.DEFAULT -> {
                viewModelScope.launch {

                    authRepository.loginUser(logInEmailInputFlow.value, logInPasswordInputFlow.value).collect { result->
                        when(result) {
                            is Resource.Success -> {
                                Log.d(TAG, "파이어 베이스 로그인 성공 ${result.data}")

                               userDataLoad(logInEmailInputFlow.value)

                            }
                            is Resource.Loading -> {

                            }

                            is Resource.Error -> {
                                Log.d(TAG, "파이어 베이스 로그인 실패, ${result.message} ${result.data}")

                                logInEmailInputFlow.emit("")
                                logInPasswordInputFlow.emit("")
                                failedLogIn.value = true
                            }

                            else ->{ }
                        }
                    }
                }
            }
            LoginType.KAKAO -> {
                handleKakaoLogin(type = "kakao", activity!!)
            }
        }



    }

    fun userDataLoad(email: String) {

        viewModelScope.launch {
            db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener { result->
                    val userData = result.toObjects(User::class.java)
                    val authState = userData.map { it.authState }.first()!!

                    viewModelScope.launch {
                        if(authState) {
                            Log.d(TAG , "이메일 인증 또는 유저 정보 로드 성공")
                            val userResponse = userData.first()

                            receiveUserDataFlow.emit(userResponse)

                            Log.d(TAG, "유저 정보 - ${receiveUserDataFlow.value}")

                            isLoggedIn.emit(true)
                            needAuthContext.emit(true)
                            delay(2000)
                            isLoadingFlow.emit(false)
                        } else {
                            Log.d(TAG , "이메일 인증 또는 유저정보 로드 실패")

                        }
                    }
                }
                .addOnFailureListener { e->
                    Log.d(TAG, "유저정보 로드 실패 ${e}")
                    viewModelScope.launch {
                        logInEmailInputFlow.emit("")
                        logInPasswordInputFlow.emit("")
                        failedLogIn.value = true
                    }
                }
        }
    }


    fun handleKakaoLogin(type: String, activity: Activity) {
        // 로그인 조합 예제
        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")

                kakaoDataRequest(type)
            }
        }

// 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(activity)) {
            UserApiClient.instance.loginWithKakaoTalk(activity) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(activity, callback = callback)
                } else if (token != null) {

                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")

                    kakaoDataRequest(type)

                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(activity, callback = callback)
        }
    }


   fun kakaoDataRequest(type: String) {
       UserApiClient.instance.me { user, error ->
           if (error != null) {
               Log.e(TAG, "사용자 정보 요청 실패", error)
           }
           else if (user != null) {

               viewModelScope.launch {

                   Log.i(TAG, "사용자 정보 요청 성공" +
                           "\n회원번호: ${user.id}" +
                           "\n이메일: ${user.kakaoAccount?.email}" +
                           "\n닉네임: ${user.kakaoAccount?.profile?.nickname}")

                   kakaoUserEmailFlow.emit(user.kakaoAccount?.email!!)
                   kakaoUsernameFlow.emit(user.kakaoAccount?.profile?.nickname!!)


                   db.collection("users")
                       .whereEqualTo("email", kakaoUserEmailFlow.value)
                       .get()
                       .addOnSuccessListener { result->
                           val userData = result.toObjects(User::class.java).firstOrNull()
                           val loadKakaoEmail = userData?.email

                           if(!loadKakaoEmail.isNullOrEmpty()){
                               viewModelScope.launch {
                                       Log.d(TAG , "카카오 유저데이터 불러오기 성공")
                                       val userResponse = userData!!

                                       receiveUserDataFlow.emit(userResponse)

                                       Log.d(TAG, "유저 정보 - ${receiveUserDataFlow.value}")

                                       isLoggedIn.emit(true)
                                       needAuthContext.emit(true)
                                       delay(2000)
                                   kakaoisLoadingFlow.emit(false)
                               }
                           } else {
                               Log.d(TAG , "카카오 유저 정보 없음, 카카오 유저 정보 생성")

                               val newDocumentRef = db.collection("users").document()

                               val time = Calendar.getInstance().time.toString()

                               val newUserData = User(
                                   id = newDocumentRef.id,
                                   deviceId = localUser.value.id.toString(),
                                   type = "kakao",
                                   authState = true,
                                   signupVerifyToken = null,
                                   username = kakaoUsernameFlow.value,
                                   email = kakaoUserEmailFlow.value,
                                   payment = false,
                                   paymentStartDate = null,
                                   paymentEndDate = null,
                                   allNumberSearchCount = allNumberSearchCountFlow.value,
                                   myNumberSearchCount = myNumberSearchCountFlow.value,
                                   numberLotteryCount = numberLotteryCountFlow.value,
                                   createdAt = time,
                                   updatedAt = time,


                                   )
                               newDocumentRef
                                   .set(newUserData.asHasMap())
                                   .addOnSuccessListener { result->
                                       Log.d(TAG, "카카오 유저데이터 저장 성공")

                                       db.collection("users")
                                           .whereEqualTo("email", kakaoUserEmailFlow.value)
                                           .get()
                                           .addOnSuccessListener { result->
                                               Log.d(TAG, "카카오 유저데이터 저장 후 불러오기 성공")

                                               val userData = result.toObjects(User::class.java)
                                               val loadKakaoEmail = userData.map { it.email }.first()!!

                                               if(loadKakaoEmail == kakaoUserEmailFlow.value) {

                                                   val userResponse = userData.first()

                                                   viewModelScope.launch {
                                                       receiveUserDataFlow.emit(userResponse)

                                                       Log.d(TAG, "유저 정보 - ${receiveUserDataFlow.value}")

                                                       isLoggedIn.emit(true)
                                                       needAuthContext.emit(true)
                                                       delay(2000)
                                                       kakaoisLoadingFlow.emit(false)
                                                   }
                                               } else {
                                                   Log.d(TAG , "카카오 유저 정보 저장 후 다시 불러오기 실패")
                                               }
                                           }
                                           .addOnFailureListener {
                                               Log.d(TAG , "카카오 유저 정보 저장 후 다시 불러오기 실패")
                                           }

                                   }
                                   .addOnFailureListener {
                                       Log.d(TAG, "카카오 유저데이터 저장 실패 ${it}")
                                   }
                           }


                       }
                       .addOnFailureListener { e->
                           Log.d(TAG, "카카오 유저정보 로드 실패 ${e}")
                       }
               }

           }
       }
   }




    // 유저 데이터 정보 초기화
    // 회원가입 된 유저는 db에서 초기화 실행
    fun userDataReSet() {

        val now = LocalDateTime.now()

        val kstZoneId = ZoneId.of("Asia/Seoul")
        val newTime = now.atZone(ZoneId.of("UTC")).withZoneSameInstant(kstZoneId)

        val saturday = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))

        val settingTime = saturday.withHour(21).withMinute(0).withSecond(0)

        val formatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        val formatTime = formatter.format(newTime)

        val formatSettingTime = formatter.format(settingTime)


        Log.d(USER, "유저데이터 리셋 체크, 현재 날짜: ${LocalDateTime.now()}, 요일: ${LocalDateTime.now().dayOfWeek} 현재시간: ${formatTime} 이번주 토요일 ${formatSettingTime}")

        if(formatTime >= "2023-12-08 01:41:00") {
            Log.d(USER, "지정 날짜보다 크다")
        } else {
            Log.d(USER, "지정 날짜보다 작다")
        }

        if(LocalDateTime.now().dayOfWeek == DayOfWeek.SATURDAY &&
            LocalDateTime.now().hour >= 22)
        {

            val nowLocalUserId = localUser.value.id

            if(localUser.value.allNumberSearchCount != null) {

                val updateUserData = LocalUserData(
                    id = nowLocalUserId,
                    allNumberSearchCount = 3,
                    myNumberSearchCount = 3,
                    numberLotteryCount = 3
                )
                viewModelScope.launch(Dispatchers.IO) {
                    localRepository.localUserUpdate(updateUserData)
                }

            } else {
                return
            }
        } else {
            return
        }
    }

    fun useItem(
        allNumberSearchCount: Int? = null,
        myNumberSearchCount: Int? = null,
        numberLotteryCount: Int? = null
    ) {

        if(allNumberSearchCount != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val userId = localUser.value.id
                localRepository.localUserUpdate(
                    LocalUserData(
                    id = userId,
                    allNumberSearchCount = allNumberSearchCount,
                    myNumberSearchCount = myNumberSearchCountFlow.value,
                    numberLotteryCount = numberLotteryCountFlow.value)
                )
            }
        }

        if(myNumberSearchCount != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val userId = localUser.value.id
                localRepository.localUserUpdate(LocalUserData(
                    id = userId,
                    allNumberSearchCount = allNumberSearchCountFlow.value,
                    myNumberSearchCount = myNumberSearchCount,
                    numberLotteryCount = numberLotteryCountFlow.value))
            }
        }

        if(numberLotteryCount != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val userId = localUser.value.id
                localRepository.localUserUpdate(LocalUserData(
                    id = userId,
                    allNumberSearchCount = allNumberSearchCountFlow.value,
                    myNumberSearchCount = myNumberSearchCountFlow.value,
                    numberLotteryCount = numberLotteryCount))
            }
        }
    }

    fun filterItem(itemCount: Int,
                   searchDataCount: List<Lotto>,
                   useType: UseType) {
        if(itemCount == 0) {
            // 광고 나온 후
        } else {
            viewModelScope.launch {

                if(searchDataCount.count() <= 0) {
                    Log.d(TAG, "UseCountDialog 예외처리됨")
                    return@launch
                } else {

                    Log.d(TAG, "UseCountDialog 예외처리 안됨")

                    when(useType) {

                        UseType.ALLNUMBER -> {

                            val changeValue = allNumberSearchCountFlow.value - 1

                            useItem(allNumberSearchCount = changeValue)
                            allNumberSearchCountFlow.emit(changeValue) }
                        UseType.MYNUMBER -> {

                            val changeValue = myNumberSearchCountFlow.value - 1

                            useItem(myNumberSearchCount = changeValue)
                            myNumberSearchCountFlow.emit(changeValue) }
                        UseType.LOTTERY -> {

                            val changeValue = numberLotteryCountFlow.value - 1

                            useItem(numberLotteryCount = changeValue)
                            numberLotteryCountFlow.emit(changeValue) }
                    }

                }
            }
        }
    }

    fun adminReSetLocalUserCount() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(USER, "관리자 권한 카운트 리셋 실행됨")
            val userId = localUser.value.id
            localRepository.localUserUpdate(LocalUserData(
                id = userId,
                allNumberSearchCount = 3,
                myNumberSearchCount = 3,
                numberLotteryCount = 3))
        }
    }
}



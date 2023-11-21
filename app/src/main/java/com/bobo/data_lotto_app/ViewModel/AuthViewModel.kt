package com.bobo.data_lotto_app.ViewModel

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobo.data_lotto_app.Localdb.LocalRepository
import com.bobo.data_lotto_app.Localdb.LocalUserData
import com.bobo.data_lotto_app.MainActivity
import com.bobo.data_lotto_app.MainActivity.Companion.TAG
import com.bobo.data_lotto_app.components.UseType
import com.bobo.data_lotto_app.service.Lotto
import com.bobo.data_lotto_app.service.User
import com.bobo.data_lotto_app.service.UserApi
import com.bobo.data_lotto_app.service.UserRequest
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
import java.util.Timer
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.concurrent.timerTask
import kotlin.reflect.jvm.internal.impl.load.java.lazy.descriptors.DeclaredMemberIndex.Empty

@HiltViewModel
class AuthViewModel @Inject
    constructor(application: Application,
                private val localRepository: LocalRepository
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

    val needAuthContext = MutableStateFlow(false)

    val allNumberSearchCountFlow = MutableStateFlow(0)

    val myNumberSearchCountFlow = MutableStateFlow(0)

    val numberLotteryCountFlow = MutableStateFlow(0)

    init {

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
                                    adminReSetLocalUserCount()
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
        }

        viewModelScope.launch(Dispatchers.IO) {

                localRepository.localUserDataGet().distinctUntilChanged()
                    .collect{userData ->

                        Log.d(USER, "생성 후 로컬 유저 불러옴 실행 ${userData}")

                        localUser.emit(userData)

//                        allNumberSearchCountFlow.emit(userData.allNumberSearchCount!!)
//                        myNumberSearchCountFlow.emit(userData.myNumberSearchCount!!)
//                        numberLotteryCountFlow.emit(userData.allNumberSearchCount!!)
                    }

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


    fun registerUser() {

        viewModelScope.launch {
            val response = UserApi.retrofitService.UserSignUp(request = UserRequest(
                type = "default",
                username = registerNicknameFlow.value,
                email = registerEmailInputFlow.value,
                password = registerPasswordInputFlow.value
            ))


            Log.d(TAG, "유저 정보 - ${response}")

            if(response != null) {
                registerSuccessFlow.emit(true)
            }

        }
    }

    enum class LoginType(name: String) {
        DEFAULT(name = "default"),
        KAKAO(name = "kakao")
    }
    fun loginUser(type: LoginType) {

        when(type) {
            LoginType.DEFAULT -> {
                viewModelScope.launch {

                    try {
                        val response = UserApi.retrofitService.UserLogin(
                            request = UserRequest(
                                type = "default",
                                email = logInEmailInputFlow.value,
                                password = logInPasswordInputFlow.value
                            )
                        )

                        receiveUserDataFlow.emit(response.users!!)

                        Log.d(TAG, "유저 정보 - ${receiveUserDataFlow.value}")

                        logInEmailInputFlow.emit("")
                        logInPasswordInputFlow.emit("")

                        if (response == null) {
                            Log.d(USER, "로그인 실패")
                        } else {
                            isLoggedIn.emit(true)
                            needAuthContext.emit(true)
                        }


                    } catch (e: HttpException) {
                        logInEmailInputFlow.emit("")
                        logInPasswordInputFlow.emit("")
                        failedLogIn.value = true
                        Log.d(TAG, "로그인 실패 $e ")
                    }
                }
            }
            LoginType.KAKAO -> {
                handleKakaoLogin(type = "kakao")
            }
        }



    }


    fun handleKakaoLogin(type: String) {
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
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                } else if (token != null) {

                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")

                    kakaoDataRequest(type)

                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
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

                   viewModelScope.launch {
                       val response = UserApi.retrofitService.UserLogin(request = UserRequest(
                           type = "${type}",
                           email = kakaoUserEmailFlow.value,
                           username = kakaoUsernameFlow.value
                       ))

                       Log.d(TAG, "유저 정보 - ${response}")

                       if(response == null) {
                           failedLogIn.value = true
                       } else {
                           val receiveUserData = response.users
                           receiveUserDataFlow.emit(receiveUserData!!)
                           needAuthContext.emit(true)
                       }
                   }

               }

           }
       }
   }




    // 유저 데이터 정보 초기화
    // 회원가입 된 유저는 db에서 초기화 실행
    fun userDataReSet() {

        Log.d(USER, "유저데이터 리셋 체크, 현재 날짜: ${LocalDateTime.now()}, 요일: ${LocalDateTime.now().dayOfWeek} 현재시간: ${LocalDateTime.now().hour}")

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



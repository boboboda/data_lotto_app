package com.bobo.data_lotto_app.service

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

private const val BASE_URL = "https://lotto-app-service-production.up.railway.app"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
//    .add(EnumJsonAdapter)
    .build()

private val LottoRetrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


//object LottoApi {
//    val retrofitService : LottoApiService by lazy { LottoRetrofit.create(LottoApiService::class.java) }
//}
//
//interface LottoApiService {
//    @GET("lottos/loadData")
//    suspend fun fetchLottos() : LottoListResponse
//
//}



private val UserRetrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


object UserApi {
    val retrofitService : UserApiService by lazy { UserRetrofit.create(UserApiService::class.java) }
}

interface UserApiService {
    @POST("auth/signup")
    suspend fun UserSignUp(@Body request: UserRequest) : UserResponse


    @POST("auth/login")
    suspend fun UserLogin(@Body request: UserRequest) : UserResponse

}
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

private const val BASE_URL = "https://asia-northeast3-lotto-app-service.cloudfunctions.net/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
//    .add(EnumJsonAdapter)
    .build()

private val UserRetrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


object UserApi {
    val retrofitService : UserApiService by lazy { UserRetrofit.create(UserApiService::class.java) }
}

interface UserApiService {
    @POST("sendEmailVerification")
    suspend fun sendEmailVerification(@Body request: EmailRequest) : EmailResponse

}
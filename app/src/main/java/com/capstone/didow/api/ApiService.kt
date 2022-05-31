package com.capstone.didow.api

import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {
    @GET("questions")
    suspend fun getQuestions(
        @Query("type") type: String,
        @Query("weightPoint") weightPoint: Int?,
    ): QuestionsResponse

    @GET("users/{id}")
    suspend fun getUser(
        @Path("id") id: String,
        @Query("weightOnly") weightOnly: Boolean?,
    ): GetUserResponse

    @POST("users")
    suspend fun createUser(@Body requestBody: RequestBody): CreateUserResponse

    @GET("exercises")
    suspend fun getExercises(
        @Query("userId") userId: String?,
        @Query("groupBy") groupBy: String?,
        @Query("startDate") startDate: String?,
        @Query("endDate") endDate: String?,
        @Header("x-timezone") timezone: Int,
    ): GetExercisesResponse

    @POST("exercises")
    suspend fun createExercise(@Body requestBody: RequestBody): CreateExerciseResponse
}

class RetrofitInstance {
    companion object{
        fun getApiService(): ApiService {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://didow-api-r3cbd22ima-et.a.run.app/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}

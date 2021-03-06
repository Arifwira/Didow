package com.capstone.didow.api

import okhttp3.MultipartBody
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
        @Query("qty") qty: Int?,
        @Query("easy") easy: Boolean?,
        @Query("medium") medium: Boolean?,
        @Query("hard") hard: Boolean?,
    ): QuestionsResponse

    @GET("users/{id}")
    suspend fun getUser(
        @Path("id") id: String,
        @Query("weightOnly") weightOnly: Boolean?,
        @Header("x-firebase-token") firebaseToken: String,
    ): GetUserResponse

    @POST("users")
    suspend fun createUser(@Body requestBody: RequestBody): CreateUserResponse

    @PUT("users/{id}")
    suspend fun editUser(
        @Path("id") id: String,
        @Body requestBody: RequestBody
    ): EditUserResponse

    @GET("exercises")
    suspend fun getExercises(
        @Query("userId") userId: String?,
        @Query("groupBy") groupBy: String?,
        @Query("startDate") startDate: String?,
        @Query("endDate") endDate: String?,
        @Header("x-timezone") timezone: Int,
        @Header("x-firebase-token") firebaseToken: String,
    ): GetExercisesResponse

    @POST("exercises")
    suspend fun createExercise(
        @Body requestBody: RequestBody,
        @Header("x-firebase-token") firebaseToken: String,
    ): CreateExerciseResponse

    @POST("assessments")
    suspend fun createAssessmentReport(@Body requestBody: RequestBody): CreateAssessmentResponse

    @GET("assessments/{id}")
    suspend fun getAssessmentReport(@Path("id") assessmentId: String): GetAssessmentResponse

    @POST("handwritings")
    suspend fun analyzeHandwriting(@Body requestBody: MultipartBody): HandwritingResponse

    @GET("suggestions")
    suspend fun getSuggestions(@Query("userId") userId: String): SuggestionsResponse
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

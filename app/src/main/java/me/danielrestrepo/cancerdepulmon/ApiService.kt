package me.danielrestrepo.cancerdepulmon

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/predict")
    suspend fun predict(@Body input: InputData): PredictionResponse
}
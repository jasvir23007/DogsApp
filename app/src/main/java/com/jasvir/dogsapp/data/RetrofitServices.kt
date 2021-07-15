package com.jasvir.dogsapp.data

import com.jasvir.dogsapp.utils.ApiConstants.GET_BREED
import com.jasvir.dogsapp.utils.ApiConstants.GET_IMAGES
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface Api {
    @GET(GET_IMAGES)
    suspend fun getdogsResponse(@Header("x-api-key") key:String ,@Query("limit") limit: Int, @Query("page") page: Int,@Query("breed_id") breed_id: Int,@Query("order")  order:String = "DESC" ): Response<List<DogData>>

    @GET(GET_BREED)
    suspend fun getComments(@Query("limit") limit: Int,@Query("page") page: Int): Response<List<Breed>>
}


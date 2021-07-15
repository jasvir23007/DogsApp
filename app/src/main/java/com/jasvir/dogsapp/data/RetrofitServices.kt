package com.jasvir.dogsapp.data

import com.jasvir.dogsapp.utils.ApiConstants.GET_BREED
import com.jasvir.dogsapp.utils.ApiConstants.GET_IMAGES
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface Api {

    /**
     *
     *
     * @param key key from dogs api
     * @param limit limit of data default 20
     * @param page for paginating
     * @param breed_id to data for particular breed
     * @param order ascending or decending
     * @return list of images
     */
    @GET(GET_IMAGES)
    suspend fun getdogsResponse(@Header("x-api-key") key:String ,@Query("limit") limit: Int, @Query("page") page: Int,@Query("breed_id") breed_id: Int,@Query("order")  order:String = "DESC" ): Response<List<DogData>>


    /**
     * TODO
     *
     * @param limit to get data which is 120
     * @param page page for pagination
     * @return list of breeds
     */
    @GET(GET_BREED)
    suspend fun getComments(@Query("limit") limit: Int,@Query("page") page: Int): Response<List<Breed>>
}


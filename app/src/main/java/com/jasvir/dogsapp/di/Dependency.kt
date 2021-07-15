package com.jasvir.dogsapp

import com.jasvir.dogsapp.data.Api
import com.jasvir.dogsapp.data.Repository
import com.jasvir.dogsapp.data.RepositoryImpl
import com.jasvir.dogsapp.ui.fragment.DogsViewModel
import com.jasvir.dogsapp.networkstates.NetworkState
import com.jasvir.dogsapp.utils.ApiConstants.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * koin di module
 */
val dataSourceModule = module {

    single { createClient(get()) }

    single { createLogger() }
    single { createRetrofitInstance(get()) }

    single { get<Retrofit>().create(Api::class.java) }

    single { NetworkState(androidApplication()) }

    single { RepositoryImpl(get(), get(), androidApplication()) as Repository }

}

/**
 * viewmodel module
 */
val viewmodelModule = module {
    viewModel { DogsViewModel(get()) }

}

/**
 * TODO
 * logging interceptor to get logs for api
 * @return interceptor
 */
fun createLogger(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
}

/**
 * TODO
 *
 * @param loggingInterceptor injected interceptor for client
 * @return client object for retrofit
 */
fun createClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
}

/**
 * TODO
 *
 * @param client injected
 * @return retrofit object for api call
 */
fun createRetrofitInstance(client:OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
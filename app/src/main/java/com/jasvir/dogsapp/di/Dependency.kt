package com.jasvir.dogsapp

import com.jasvir.dogsapp.data.Api
import com.jasvir.dogsapp.data.Repository
import com.jasvir.dogsapp.data.RepositoryImpl
import com.jasvir.dogsapp.ui.fragment.DogsViewModel
import com.jasvir.dogsapp.networkstates.NetworkState
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataSourceModule = module {

    single { createClient(get()) }

    single { createLogger() }
    single { createRetrofitInstance(get()) }

    single { get<Retrofit>().create(Api::class.java) }

    single { NetworkState(androidApplication()) }

    single { RepositoryImpl(get(), get(), androidApplication()) as Repository }

}

val viewmodelModule = module {
    viewModel { DogsViewModel(get()) }

}


fun createLogger(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
}

fun createClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
}


fun createRetrofitInstance(client:OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://api.thedogapi.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
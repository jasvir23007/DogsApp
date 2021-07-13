package com.jasvir.dogsapp

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jasvir.dogsapp.networkstates.NetworkState
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.koin.android.ext.koin.with
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.test.KoinTest

open class BaseTestClass : KoinTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val networkState = mockk<NetworkState>()
    private val application: Application = mockk()

    @Before
    open fun before() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        MockKAnnotations.init(this)
        startKoin(listOf(dataSourceModule, viewmodelModule)) with (application)
        StandAloneContext.loadKoinModules(module {
            single(override = true) { networkState }
        })
        every { application.getString(any()) } returns "Test String"
        every { networkState.isConnected() } returns true
    }

    @After
    open fun after() {
        Dispatchers.resetMain()
        StandAloneContext.stopKoin()
    }
}
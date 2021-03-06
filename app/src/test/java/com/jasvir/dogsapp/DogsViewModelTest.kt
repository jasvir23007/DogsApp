package com.jasvir.dogsapp

import androidx.lifecycle.Observer
import com.jasvir.dogsapp.data.DogData
import com.jasvir.dogsapp.networkstates.BreedState
import com.jasvir.dogsapp.ui.fragment.DogsViewModel
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.koin.standalone.get
import timber.log.Timber

class DogsViewModelTest : BaseTestClass() {

    private lateinit var dogsViewModel: DogsViewModel

    @RelaxedMockK
    lateinit var mockBreedObserver: Observer<BreedState>



    @Before
    override fun before() {
        super.before()
        dogsViewModel = get()
    }


    @Test
    fun testGetBreedDataIsSuccessful() =
        runBlocking<Unit> {
            dogsViewModel.breedLiveData.observeForever(mockBreedObserver)
            assert(dogsViewModel.breedLiveData.value == null)
            dogsViewModel.getBreeds().join()
            val value = dogsViewModel.breedLiveData.value
            println("called..... suspend function")

            assert(value != null)
            when (value) {
                is BreedState.Loaded -> {
                    assert(value.dogs.size >= 0)
                }
                is BreedState.Error -> {
                    Timber.e(value.message)
                    assert(false)
                }
            }

            assert((value as BreedState.Loaded).dogs.size >= 0)
        }




    @Test
    fun testGetdogsDataIsSuccessful() =
        runBlocking{
            dogsViewModel.getListforSearch(1).collect {
                assert(!it.equals(emptyList<DogData>()))
            }

        }


}
package com.jasvir.dogsapp.ui.fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jasvir.dogsapp.coroutines.CouroutineViewModel
import com.jasvir.dogsapp.data.Repository
import com.jasvir.dogsapp.networkstates.BreedState
import com.jasvir.dogsapp.networkstates.DogsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class DogsViewModel(
    private val repository: Repository,
    uiContext: CoroutineContext = Dispatchers.Main
) :
    CouroutineViewModel(uiContext) {

    private val privateState = MutableLiveData<DogsState>()
    private val breedState = MutableLiveData<BreedState>()

    val breedLiveData: LiveData<BreedState> = breedState


    val dogLiveData: LiveData<DogsState> = privateState



    fun getBreeds() = launch {
        breedState.value = BreedState.Loading
        Timber.d("getdogsState() called....")
        breedState.value = repository.getCommentsFordogs()
        Timber.d("repository.getdogsState() executed....")
        Log.i("valueeee","==="+breedState.value)
    }

    fun refreshdogs(breedId: Int) = launch {
        privateState.value = DogsState.Loading
        Timber.d("getdogsState() called....")
        privateState.value = repository.getdogsState(breedId)
        Timber.d("repository.getdogsState() executed....")
        Log.i("valueeee","==="+privateState.value)
    }

}
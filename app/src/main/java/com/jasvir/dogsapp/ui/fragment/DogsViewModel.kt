package com.jasvir.dogsapp.ui.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jasvir.dogsapp.coroutines.CouroutineViewModel
import com.jasvir.dogsapp.data.Api
import com.jasvir.dogsapp.data.DogData
import com.jasvir.dogsapp.data.Repository
import com.jasvir.dogsapp.networkstates.BreedState
import com.jasvir.dogsapp.repository.SearchDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DogsViewModel(
    private val api: Api,
    private val repository: Repository,
    uiContext: CoroutineContext = Dispatchers.Main
) :
    CouroutineViewModel(uiContext) {

    private val breedState = MutableLiveData<BreedState>()
    val breedLiveData: LiveData<BreedState> = breedState


    fun getListforSearch(breedId: Int): Flow<PagingData<DogData>> {
        val listData = Pager(PagingConfig(pageSize = 1)) {
            SearchDataSource(repository, breedId)
        }.flow.cachedIn(this)
        return listData
    }

    fun getBreeds() = launch {
        breedState.value = BreedState.Loading
        breedState.value = repository.getCommentsFordogs()
    }


}
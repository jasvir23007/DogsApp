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

/**
 * Viewmodel for api
 *
 * @property repository to call api
 * @constructor to init viewmodel
 * T
 *
 * @param uiContext coroutine context
 */
class DogsViewModel(private val repository: Repository,
    uiContext: CoroutineContext = Dispatchers.Main
) :
    CouroutineViewModel(uiContext) {

    private val breedState = MutableLiveData<BreedState>()
    val breedLiveData: LiveData<BreedState> = breedState

    /**
     * init paging library
     *
     * @param breedId to search
     * @return  Paged list with kotlin flow
     *
     */
    fun getListforSearch(breedId: Int): Flow<PagingData<DogData>> {
        val listData = Pager(PagingConfig(pageSize = 20)) {
            SearchDataSource(repository, breedId)
        }.flow.cachedIn(this)
        return listData
    }

    /**
     * get breeds
     *
     */
    fun getBreeds() = launch {
        breedState.value = BreedState.Loading
        breedState.value = repository.getBreedsFordogs()
    }


}
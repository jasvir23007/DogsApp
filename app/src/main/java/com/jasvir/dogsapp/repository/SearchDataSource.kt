package com.jasvir.dogsapp.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jasvir.dogsapp.data.Api
import com.jasvir.dogsapp.data.DogData
import com.jasvir.dogsapp.data.Repository
import com.jasvir.dogsapp.networkstates.DogsState
import com.jasvir.dogsapp.utils.Constants
import kotlinx.coroutines.CoroutineScope
import java.util.ArrayList

class SearchDataSource(private val repository: Repository,
                        private val query: Int): PagingSource<Int, DogData>() {

    override fun getRefreshKey(state: PagingState<Int, DogData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(20)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(20)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DogData> {
        return try {
            val nextPage = params.key ?: 0
            val response = repository.getdogsState(nextPage,query)

            LoadResult.Page(
                data = response,
                prevKey = if (nextPage == 0) null else nextPage - 20 ,
                nextKey = if(listOf(response).size%20==0) response.size + 20 else null
            )
        }catch (e: Exception){
            LoadResult.Error(e)
        }
    }


}
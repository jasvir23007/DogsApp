package com.jasvir.dogsapp.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jasvir.dogsapp.data.DogData
import com.jasvir.dogsapp.data.Repository


class SearchDataSource(private val repository: Repository,
                        private val query: Int): PagingSource<Int, DogData>() {


    override val keyReuseSupported: Boolean
       = true

    override fun getRefreshKey(state: PagingState<Int, DogData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DogData> {
        return try {
            val nextPage = params.key ?: 0
            val response = repository.getdogsState(nextPage,query)
            val tempList = ArrayList<DogData>()
            tempList.addAll(response)

            LoadResult.Page(
                data = response,
                prevKey = if (nextPage == 0) null else nextPage - 1 ,
                nextKey = if(response.size==0) null else nextPage + 1
            )
        }catch (e: Exception){
            LoadResult.Error(e)
        }
    }


}
package com.jasvir.dogsapp.data

import android.app.Application
import com.jasvir.dogsapp.R
import com.jasvir.dogsapp.networkstates.BreedState
import com.jasvir.dogsapp.networkstates.DogsState
import com.jasvir.dogsapp.networkstates.NetworkState
import com.jasvir.dogsapp.utils.Constants.API_KEY
import com.jasvir.dogsapp.utils.Constants.LIMIT
import com.jasvir.dogsapp.utils.Constants.PAGE
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class RepositoryImpl(
    private val dogApi: Api,
    private val networkState: NetworkState,
    private val application: Application,
    private val backgroundDispatcher: CoroutineDispatcher = Dispatchers.Default
) : Repository {

    //Switched context to background thread
    override suspend fun getdogsState(breedId: Int): DogsState =
        withContext(backgroundDispatcher) {
            if (!networkState.isConnected()) {
                return@withContext DogsState.Error(application.getString(R.string.not_connected))
            }

            val response = try {
                dogApi.getdogsResponse(API_KEY, LIMIT, PAGE , breedId)
            } catch (e: Throwable) {
                //Sending a generic exception to the view for now

                Timber.e(e)
                return@withContext DogsState.Error(application.getString(R.string.network_error))
            }

            return@withContext if (response.isSuccessful) {
                response.body()?.let {
                    DogsState.DogsLoaded(it)
                } ?: DogsState.Error(application.getString(R.string.emty_body))
            } else {
                Timber.e(response.message())
                DogsState.Error(response.message())
            }
        }

    //Switched context to background thread
    override suspend fun getCommentsFordogs(): BreedState = withContext(backgroundDispatcher) {
        if (!networkState.isConnected()) {
            return@withContext BreedState.Error(application.getString(R.string.not_connected))
        }

        val response = try {
            dogApi.getComments(LIMIT, PAGE)
        } catch (e: Throwable) {
            //Sending a generic exception to the view for now
            Timber.e(e)
            return@withContext BreedState.Error(application.getString(R.string.network_error))
        }

        return@withContext if (response.isSuccessful) {
            response.body()?.let {
                BreedState.Loaded(it)
            } ?: BreedState.Error(application.getString(R.string.emty_body))
        } else {
            Timber.e(response.message())
            BreedState.Error(response.message())
        }
    }

}

interface Repository {
    suspend fun getdogsState(breedId: Int): DogsState
    suspend fun getCommentsFordogs(): BreedState
}
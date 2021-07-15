package com.jasvir.dogsapp.data

import android.app.Application
import com.jasvir.dogsapp.R
import com.jasvir.dogsapp.networkstates.BreedState
import com.jasvir.dogsapp.networkstates.NetworkState
import com.jasvir.dogsapp.utils.Constants
import com.jasvir.dogsapp.utils.Constants.LIMIT
import com.jasvir.dogsapp.utils.Constants.PAGE
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber


/**
 * repository class to get data
 *
 * @property dogApi api interface of retrofit
 * @property networkState is network connected
 * @property application refrence of application object
 * @property backgroundDispatcher dispatcher
 */
class RepositoryImpl(
    private val dogApi: Api,
    private val networkState: NetworkState,
    private val application: Application,
    private val backgroundDispatcher: CoroutineDispatcher = Dispatchers.Default
) : Repository {
    /**
     * get dogs images api
     *
     * @param nextPage for pagination
     * @param breedId which breed images needed
     * @return list of images
     */
    override suspend fun getdogsState(nextPage: Int, breedId: Int): List<DogData> {
        try {
            return dogApi.getdogsResponse(Constants.API_KEY, 20, nextPage, breedId).body()!!
        } catch (e: Exception) {
            return emptyList()
        }
    }

    /**
     * api to get breeds
     *
     * @return list of breeds
     */
    //Switched context to background thread
    override suspend fun getBreedsFordogs(): BreedState = withContext(backgroundDispatcher) {
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
    suspend fun getdogsState(nextPage: Int, breedId: Int): List<DogData>
    suspend fun getBreedsFordogs(): BreedState
}
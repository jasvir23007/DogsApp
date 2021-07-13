package com.jasvir.dogsapp.networkstates

import com.jasvir.dogsapp.data.DogData

sealed class DogsState {
    object Loading : DogsState()
    data class Error(val message: String?) : DogsState()
    data class DogsLoaded(val dogs: List<DogData>) : DogsState()
}
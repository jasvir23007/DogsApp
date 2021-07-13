package com.jasvir.dogsapp.networkstates

import com.jasvir.dogsapp.data.Breed

sealed class BreedState {
    object Loading : BreedState()
    data class Error(val message: String?) : BreedState()
    data class Loaded(val dogs: List<Breed>) : BreedState()
}
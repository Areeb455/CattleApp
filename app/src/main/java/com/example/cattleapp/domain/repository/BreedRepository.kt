package com.example.cattleapp.domain.repository

import com.example.cattleapp.domain.model.Breed

interface BreedRepository {
    fun getTopBreeds(imagePath: String): List<Breed>
    fun getBreedDetails(breedId: Int): Breed
    fun getUserCattleList(): List<Breed>
    fun addCattle(breed: Breed)
}

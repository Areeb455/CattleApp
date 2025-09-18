package com.example.cattleapp.data.repository

import com.example.cattleapp.domain.model.Breed
import com.example.cattleapp.domain.repository.BreedRepository

    class BreedRepositoryImpl : BreedRepository {

        private val cattleList = mutableListOf<Breed>()

        // mock data, later replaced with ML model results
        private val sampleBreeds = listOf(
            Breed(1, "Gir", "Indigenous cow", "Green fodder", "Low disease resistance", "12L/day"),
            Breed(2, "Murrah", "Popular buffalo", "High protein feed", "Ticks", "20L/day"),
            Breed(3, "Sahiwal", "Milk rich breed", "Balanced diet", "Moderate disease resistance", "15L/day")
        )

        override fun getTopBreeds(imagePath: String): List<Breed> {
            return sampleBreeds.shuffled().take(3)
        }

        override fun getBreedDetails(breedId: Int): Breed {
            return sampleBreeds.first { it.id == breedId }
        }

        override fun getUserCattleList(): List<Breed> = cattleList

        override fun addCattle(breed: Breed) {
            cattleList.add(breed)
        }
    }


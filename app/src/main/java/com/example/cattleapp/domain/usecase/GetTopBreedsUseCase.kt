package com.example.cattleapp.domain.usecase


import com.example.cattleapp.domain.model.Breed
import com.example.cattleapp.domain.repository.BreedRepository

class GetTopBreedsUseCase(private val repository: BreedRepository) {
    operator fun invoke(imagePath: String): List<Breed> = repository.getTopBreeds(imagePath)
}

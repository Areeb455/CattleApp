package com.example.cattleapp.domain.usecase


import com.example.cattleapp.domain.model.Breed
import com.example.cattleapp.domain.repository.BreedRepository

class GetBreedDetailsUseCase(private val repository: BreedRepository) {
    operator fun invoke(breedId: Int): Breed = repository.getBreedDetails(breedId)
}

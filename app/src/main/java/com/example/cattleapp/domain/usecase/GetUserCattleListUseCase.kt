package com.example.cattleapp.domain.usecase

import com.example.cattleapp.domain.model.Breed
import com.example.cattleapp.domain.repository.BreedRepository


class GetUserCattleListUseCase(private val repository: BreedRepository) {
        operator fun invoke(): List<Breed> = repository.getUserCattleList()
    }


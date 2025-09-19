package com.cattlelabs.cattleapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cattlelabs.cattleapp.data.model.BreedDetails
import com.cattlelabs.cattleapp.data.model.Cattle
import com.cattlelabs.cattleapp.data.model.CattleRequest
import com.cattlelabs.cattleapp.data.model.CattleResponse
import com.cattlelabs.cattleapp.data.model.ImageUploadResponse
import com.cattlelabs.cattleapp.data.repo.AuthRepo
import com.cattlelabs.cattleapp.data.repo.CattleRepo
import com.cattlelabs.cattleapp.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class CattleViewModel @Inject constructor(
    private val cattleRepository: CattleRepo,
    private val authRepo: AuthRepo
) : ViewModel() {

    private val _cattleListState = MutableStateFlow<UiState<List<Cattle>>>(UiState.Idle)
    val cattleListState: StateFlow<UiState<List<Cattle>>> = _cattleListState.asStateFlow()

    private val _addCattleState = MutableStateFlow<UiState<CattleResponse>>(UiState.Idle)
    val addCattleState: StateFlow<UiState<CattleResponse>> = _addCattleState.asStateFlow()

    private val _breedDetailsState = MutableStateFlow<UiState<BreedDetails>>(UiState.Idle)
    val breedDetailsState: StateFlow<UiState<BreedDetails>> = _breedDetailsState.asStateFlow()

    private val _imageUploadState = MutableStateFlow<UiState<ImageUploadResponse>>(UiState.Idle)
    val imageUploadState: StateFlow<UiState<ImageUploadResponse>> = _imageUploadState.asStateFlow()

    fun getCattle() {
        val userId = authRepo.getCurrentUserId()
        if (userId.isNullOrBlank()) {
            _cattleListState.value = UiState.Failed("User not logged in")
            return
        }

        viewModelScope.launch {
            _cattleListState.value = UiState.Loading

            when (val result = cattleRepository.getCattle(userId)) {
                is UiState.Success -> {
                    _cattleListState.value = UiState.Success(result.data.body)
                }

                is UiState.Failed -> {
                    _cattleListState.value = UiState.Failed(result.message)
                }

                is UiState.InternetError -> {
                    _cattleListState.value = UiState.InternetError
                }

                is UiState.InternalServerError -> {
                    _cattleListState.value = UiState.InternalServerError(result.errorMessage)
                }

                is UiState.NoDataFound -> {
                    _cattleListState.value = UiState.NoDataFound
                }

                else -> {
                    _cattleListState.value = UiState.Failed("Unknown error occurred")
                }
            }
        }
    }

    fun addCattle(cattleRequest: CattleRequest) {
        viewModelScope.launch {
            _addCattleState.value = UiState.Loading

            when (val result = cattleRepository.addCattle(cattleRequest)) {
                is UiState.Success -> {
                    _addCattleState.value = UiState.Success(result.data.body)
                    // Refresh cattle list after successful addition
                    getCattle()
                }

                is UiState.Failed -> {
                    _addCattleState.value = UiState.Failed(result.message)
                }

                is UiState.InternetError -> {
                    _addCattleState.value = UiState.InternetError
                }

                is UiState.InternalServerError -> {
                    _addCattleState.value = UiState.InternalServerError(result.errorMessage)
                }

                is UiState.NoDataFound -> {
                    _addCattleState.value = UiState.NoDataFound
                }

                else -> {
                    _addCattleState.value = UiState.Failed("Unknown error occurred")
                }
            }
        }
    }

    fun getBreedById(breedId: String) {
        viewModelScope.launch {
            _breedDetailsState.value = UiState.Loading

            when (val result = cattleRepository.getBreedById(breedId)) {
                is UiState.Success -> {
                    _breedDetailsState.value = UiState.Success(result.data.body)
                }

                is UiState.Failed -> {
                    _breedDetailsState.value = UiState.Failed(result.message)
                }

                is UiState.InternetError -> {
                    _breedDetailsState.value = UiState.InternetError
                }

                is UiState.InternalServerError -> {
                    _breedDetailsState.value = UiState.InternalServerError(result.errorMessage)
                }

                is UiState.NoDataFound -> {
                    _breedDetailsState.value = UiState.NoDataFound
                }

                else -> {
                    _breedDetailsState.value = UiState.Failed("Unknown error occurred")
                }
            }
        }
    }

    fun uploadImage(
        imagePart: MultipartBody.Part,
        name: String? = null,
        description: String? = null
    ) {
        viewModelScope.launch {
            _imageUploadState.value = UiState.Loading

            val nameBody = name?.let {
                RequestBody.create("text/plain".toMediaTypeOrNull(), it)
            }
            val descBody = description?.let {
                RequestBody.create("text/plain".toMediaTypeOrNull(), it)
            }

            when (val result = cattleRepository.uploadImage(imagePart, nameBody, descBody)) {
                is UiState.Success -> {
                    _imageUploadState.value = UiState.Success(result.data.body)
                }

                is UiState.Failed -> {
                    _imageUploadState.value = UiState.Failed(result.message)
                }

                is UiState.InternetError -> {
                    _imageUploadState.value = UiState.InternetError
                }

                is UiState.InternalServerError -> {
                    _imageUploadState.value = UiState.InternalServerError(result.errorMessage)
                }

                is UiState.NoDataFound -> {
                    _imageUploadState.value = UiState.NoDataFound
                }

                else -> {
                    _imageUploadState.value = UiState.Failed("Unknown error occurred")
                }
            }
        }
    }

    // Reset state functions
    fun resetAddCattleState() {
        _addCattleState.value = UiState.Idle
    }

    fun resetBreedDetailsState() {
        _breedDetailsState.value = UiState.Idle
    }

    fun resetImageUploadState() {
        _imageUploadState.value = UiState.Idle
    }

    fun resetCattleListState() {
        _cattleListState.value = UiState.Idle
    }
}

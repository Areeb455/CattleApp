package com.cattlelabs.cattleapp.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cattlelabs.cattleapp.data.model.BreedDetails
import com.cattlelabs.cattleapp.data.model.Cattle
import com.cattlelabs.cattleapp.data.model.CattleRequest
import com.cattlelabs.cattleapp.data.model.CattleResponse
import com.cattlelabs.cattleapp.data.model.PredictionBody
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
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.InputStream
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

    private val _predictionState = MutableStateFlow<UiState<PredictionBody>>(UiState.Idle)
    val predictionState: StateFlow<UiState<PredictionBody>> = _predictionState.asStateFlow()


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

    fun uploadAndPredict(context: Context, imageUri: Uri) {
        viewModelScope.launch {
            _predictionState.value = UiState.Loading

            // Convert Uri to MultipartBody.Part
            val imagePart = try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
                val imageBytes = inputStream?.readBytes()
                inputStream?.close()

                if (imageBytes == null) {
                    _predictionState.value = UiState.Failed("Could not read image file.")
                    return@launch
                }

                val requestBody = imageBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("image", "photo.jpg", requestBody)
            } catch (e: Exception) {
                _predictionState.value = UiState.Failed("Error preparing image: ${e.message}")
                return@launch
            }

            // Call the repository
            when (val result = cattleRepository.uploadAndPredict(imagePart)) {
                is UiState.Success -> {
                    _predictionState.value = UiState.Success(result.data.body)
                }

                is UiState.Failed -> {
                    _predictionState.value = UiState.Failed(result.message)
                }
                // ... handle other states like you do for other functions
                else -> {
                    _predictionState.value = UiState.Failed("Unknown error occurred")
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

    fun resetPredictionState() {
        _predictionState.value = UiState.Idle
    }

    fun resetCattleListState() {
        _cattleListState.value = UiState.Idle
    }
}

package com.cattlelabs.cattleapp.data.repo

import com.cattlelabs.cattleapp.data.model.ApiResponse
import com.cattlelabs.cattleapp.data.model.BreedDetails
import com.cattlelabs.cattleapp.data.model.Cattle
import com.cattlelabs.cattleapp.data.model.CattleRequest
import com.cattlelabs.cattleapp.data.model.CattleResponse
import com.cattlelabs.cattleapp.data.model.ImageUploadResponse
import com.cattlelabs.cattleapp.data.remote.CattleApiService
import com.cattlelabs.cattleapp.state.UiState
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CattleRepo @Inject constructor(
    private val apiService: CattleApiService
) {

    suspend fun addCattle(cattleRequest: CattleRequest): UiState<ApiResponse<CattleResponse>> {
        return try {
            val response = apiService.addCattle(cattleRequest)

            when {
                response.isSuccessful -> {
                    response.body()?.let { body ->
                        UiState.Success(body)
                    } ?: UiState.NoDataFound
                }

                response.code() in 500..599 -> {
                    UiState.InternalServerError("Server error: ${response.code()}")
                }

                else -> {
                    UiState.Failed("API Error: ${response.code()} - ${response.message()}")
                }
            }
        } catch (e: UnknownHostException) {
            UiState.InternetError
        } catch (e: SocketTimeoutException) {
            UiState.InternetError
        } catch (e: IOException) {
            UiState.InternetError
        } catch (e: Exception) {
            UiState.Failed("Unexpected error: ${e.message}")
        }
    }

    suspend fun getCattle(userId: String): UiState<ApiResponse<List<Cattle>>> {
        return try {
            val response = apiService.getCattle(userId)

            when {
                response.isSuccessful -> {
                    response.body()?.let { body ->
                        UiState.Success(body)
                    } ?: UiState.NoDataFound
                }

                response.code() in 500..599 -> {
                    UiState.InternalServerError("Server error: ${response.code()}")
                }

                else -> {
                    UiState.Failed("API Error: ${response.code()} - ${response.message()}")
                }
            }
        } catch (e: UnknownHostException) {
            UiState.InternetError
        } catch (e: SocketTimeoutException) {
            UiState.InternetError
        } catch (e: IOException) {
            UiState.InternetError
        } catch (e: Exception) {
            UiState.Failed("Unexpected error: ${e.message}")
        }
    }

    suspend fun getBreedById(breedId: String): UiState<ApiResponse<BreedDetails>> {
        return try {
            val response = apiService.getBreedById(breedId)

            when {
                response.isSuccessful -> {
                    response.body()?.let { body ->
                        UiState.Success(body)
                    } ?: UiState.NoDataFound
                }

                response.code() in 500..599 -> {
                    UiState.InternalServerError("Server error: ${response.code()}")
                }

                else -> {
                    UiState.Failed("API Error: ${response.code()} - ${response.message()}")
                }
            }
        } catch (e: UnknownHostException) {
            UiState.InternetError
        } catch (e: SocketTimeoutException) {
            UiState.InternetError
        } catch (e: IOException) {
            UiState.InternetError
        } catch (e: Exception) {
            UiState.Failed("Unexpected error: ${e.message}")
        }
    }

    suspend fun uploadImage(
        imagePart: MultipartBody.Part,
        name: RequestBody? = null,
        description: RequestBody? = null
    ): UiState<ApiResponse<ImageUploadResponse>> {
        return try {
            val response = apiService.uploadImage(imagePart, name, description)

            when {
                response.isSuccessful -> {
                    response.body()?.let { body ->
                        UiState.Success(body)
                    } ?: UiState.NoDataFound
                }

                response.code() in 500..599 -> {
                    UiState.InternalServerError("Server error: ${response.code()}")
                }

                else -> {
                    UiState.Failed("API Error: ${response.code()} - ${response.message()}")
                }
            }
        } catch (e: UnknownHostException) {
            UiState.InternetError
        } catch (e: SocketTimeoutException) {
            UiState.InternetError
        } catch (e: IOException) {
            UiState.InternetError
        } catch (e: Exception) {
            UiState.Failed("Unexpected error: ${e.message}")
        }
    }
}
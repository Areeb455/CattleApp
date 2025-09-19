package com.cattlelabs.cattleapp.data.repo

import com.cattlelabs.cattleapp.data.Prefs
import com.cattlelabs.cattleapp.data.model.ApiResponse
import com.cattlelabs.cattleapp.data.model.LoginRequest
import com.cattlelabs.cattleapp.data.model.LoginResponse
import com.cattlelabs.cattleapp.data.remote.CattleApiService
import com.cattlelabs.cattleapp.state.UiState
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class AuthRepo @Inject constructor(
    private val apiService: CattleApiService,
    private val prefs: Prefs
) {

    suspend fun login(userId: String, password: String): UiState<ApiResponse<LoginResponse>> {
        return try {
            val request = LoginRequest(userId, password)
            val response = apiService.login(request)

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

    fun saveUserSession(userId: String, userName: String, phoneNumber: String, location: String) {
        prefs.saveSession(
            userId = userId,
            userName = userName,
            phoneNumber = phoneNumber,
            location = location
        )
    }

    fun getCurrentUserId(): String? {
        return prefs.getUserId()
    }

    fun logout() {
        prefs.clearSession()
    }
}

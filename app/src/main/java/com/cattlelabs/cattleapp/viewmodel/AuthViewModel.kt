package com.cattlelabs.cattleapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cattlelabs.cattleapp.data.model.LoginResponse
import com.cattlelabs.cattleapp.data.repo.AuthRepo
import com.cattlelabs.cattleapp.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepo: AuthRepo
) : ViewModel() {

    private val _loginState = MutableStateFlow<UiState<LoginResponse>>(UiState.Idle)
    val loginState: StateFlow<UiState<LoginResponse>> = _loginState.asStateFlow()

    fun login(userId: String, password: String) {
        viewModelScope.launch {
            _loginState.value = UiState.Loading

            when (val result = authRepo.login(userId, password)) {
                is UiState.Success -> {
                    val loginData = result.data.body
                    authRepo.saveUserSession(
                        loginData.userId,
                        userName = loginData.name,
                        phoneNumber = loginData.phoneNumber,
                        location = loginData.location
                    )
                    _loginState.value = UiState.Success(loginData)
                }

                is UiState.Failed -> {
                    _loginState.value = UiState.Failed(result.message)
                }

                is UiState.InternetError -> {
                    _loginState.value = UiState.InternetError
                }

                is UiState.InternalServerError -> {
                    _loginState.value = UiState.InternalServerError(result.errorMessage)
                }

                is UiState.NoDataFound -> {
                    _loginState.value = UiState.NoDataFound
                }

                else -> {
                    _loginState.value = UiState.Failed("Unknown error occurred")
                }
            }
        }
    }

    fun getUserName(): String {
        return authRepo.getUserName()
    }

    fun getLocation(): String {
        return authRepo.getLocation()
    }

    fun logout() {
        authRepo.logout()
        _loginState.value = UiState.Idle
    }

}

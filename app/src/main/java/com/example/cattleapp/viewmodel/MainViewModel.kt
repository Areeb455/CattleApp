package com.example.cattleapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

data class BreedSuggestion(val id: Int, val name: String, val confidence: Double)
data class CattleRecord(val name: String, val breed: String, val nutrition: String, val injuries: String, val yield: String)
data class UserProfile(val name: String, val phone: String, val location: String? = null)

/**
 * MainViewModel contains in-memory lists and a mock scan implementation.
 * Replace scanImageMock with real ML pipeline when ready.
 */
class MainViewModel : ViewModel() {

    private val _breedSuggestions = MutableStateFlow<List<BreedSuggestion>>(emptyList())
    val breedSuggestions: StateFlow<List<BreedSuggestion>> = _breedSuggestions.asStateFlow()

    private val _showGeoPopup = MutableStateFlow(false)
    val showGeoPopup: StateFlow<Boolean> = _showGeoPopup.asStateFlow()

    private val _geoSuggestedBreed = MutableStateFlow<BreedSuggestion?>(null)
    val geoSuggestedBreed: StateFlow<BreedSuggestion?> = _geoSuggestedBreed.asStateFlow()

    private val _selectedBreedName = MutableStateFlow<String?>(null)
    val selectedBreedName: StateFlow<String?> = _selectedBreedName.asStateFlow()

    private val _cattleRecords = MutableStateFlow<List<CattleRecord>>(emptyList())
    val cattleRecords: StateFlow<List<CattleRecord>> = _cattleRecords.asStateFlow()

    private val _user = MutableStateFlow<UserProfile?>(null)
    val user: StateFlow<UserProfile?> = _user.asStateFlow()

    private val geoThreshold = 0.12

    init {
        _cattleRecords.value = listOf(CattleRecord("Laxmi", "Gir", "Green fodder", "None", "8L/day"))
    }

    fun updateUser(name: String, phone: String, location: String? = null) {
        _user.value = UserProfile(name = name, phone = phone, location = location)
    }

    fun addCattle(record: CattleRecord) {
        _cattleRecords.value = _cattleRecords.value + record
    }

    fun dismissGeoPopup() {
        _showGeoPopup.value = false
        _geoSuggestedBreed.value = null
    }

    fun selectBreed(breedName: String) {
        _selectedBreedName.value = breedName
    }

    /**
     * Mock scan: generates 3 random suggestions and decides if geospatial popup must be shown.
     * In real pipeline, replace with ML model results and use location to confirm.
     */
    fun scanImageMock(imagePath: String) {
        viewModelScope.launch {
            val pool = listOf("Gir", "Sahiwal", "Murrah", "Jersey", "Holstein", "Tharparkar")
            val suggestions = (0..2).map { i ->
                val name = pool[Random.nextInt(pool.size)]
                val confidence = Random.nextDouble(0.45, 0.96)
                BreedSuggestion(id = i, name = name, confidence = confidence)
            }.sortedByDescending { it.confidence }

            _breedSuggestions.value = suggestions

            if (suggestions.size >= 2) {
                val top = suggestions[0].confidence
                val second = suggestions[1].confidence
                if ((top - second) < geoThreshold) {
                    // Demo logic: if user has a location, bias to second, else first
                    val geoPick = if ((_user.value?.location?.isNotBlank() == true)) suggestions[1] else suggestions[0]
                    _geoSuggestedBreed.value = geoPick
                    _showGeoPopup.value = true
                } else {
                    _showGeoPopup.value = false
                    _geoSuggestedBreed.value = null
                }
            } else {
                _showGeoPopup.value = false
                _geoSuggestedBreed.value = null
            }
        }
    }
}

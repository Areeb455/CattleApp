package com.cattlelabs.cattleapp.navigation

sealed class CattleAppScreens(val route: String) {
    object LoginScreen : CattleAppScreens("login_screen")
    object HomeScreen : CattleAppScreens("home_screen")
    object ProfileScreen : CattleAppScreens("profile_screen")
    object CattleScanScreen : CattleAppScreens("cattle_scan_screen")
    object BreedPredictionScreen : CattleAppScreens("breed_prediction_screen")
    object CattleFormScreen : CattleAppScreens("cattle_form_screen")
    object PastRecordsScreen : CattleAppScreens("past_records_screen")
}

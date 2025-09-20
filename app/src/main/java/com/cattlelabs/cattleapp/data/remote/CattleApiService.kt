package com.cattlelabs.cattleapp.data.remote

import com.cattlelabs.cattleapp.data.model.ApiResponse
import com.cattlelabs.cattleapp.data.model.BreedDetails
import com.cattlelabs.cattleapp.data.model.Cattle
import com.cattlelabs.cattleapp.data.model.CattleRequest
import com.cattlelabs.cattleapp.data.model.CattleResponse
import com.cattlelabs.cattleapp.data.model.LoginRequest
import com.cattlelabs.cattleapp.data.model.LoginResponse
import com.cattlelabs.cattleapp.data.model.PredictionBody
import retrofit2.http.Body
import retrofit2.http.POST

import retrofit2.Response
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface CattleApiService {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<LoginResponse>>

    @POST("push-cattle")
    suspend fun addCattle(@Body request: CattleRequest): Response<ApiResponse<CattleResponse>>

    @GET("get-cattle")
    suspend fun getCattle(@Query("userId") userId: String): Response<ApiResponse<List<Cattle>>>

    @GET("get-breed/{breed_id}")
    suspend fun getBreedById(@Path("breed_id") breedId: String): Response<ApiResponse<BreedDetails>>

    @Multipart
    @POST("upload-and-predict")
    suspend fun uploadAndPredict(
        @Part image: MultipartBody.Part,
        @Part("name") name: RequestBody? = null,
        @Part("description") description: RequestBody? = null
    ): Response<ApiResponse<PredictionBody>>
}

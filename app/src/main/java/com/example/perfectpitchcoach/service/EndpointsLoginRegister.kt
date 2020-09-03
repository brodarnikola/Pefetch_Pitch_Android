package com.example.brc.kotlintestapp.Settings

import com.example.perfect_pitch_trainer.model.*
import com.example.perfectpitchcoach.model.SolvedSubMasterClassRequest
import com.example.perfectpitchcoach.model.SolvedSubMasterClassResponse
import org.simpleframework.xml.core.Validate
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

interface EndpointsLoginRegister {

    //@HTTP(method = "POST", path = "signUpUser", hasBody = true)
    // THIS ARE EXAMPLES FOR NODE JS
    @POST("signUpUser")
    fun getPopular(@Body deviceInfo: RegisterRequest) : Call<RegisterResponse>

    @POST("loginUser")
    fun getLoginUserData(@Body deviceInfo: LoginRequest) : Call<LoginResponse>

    @POST("passwordRecovery")
    fun getPasswordRecoveryData(@Body deviceInfo: PasswordRecoveryRequest) : Call<PasswordRecoveryResponse>

    @POST("passwordUpdate")
    fun getPasswordUpdateData(@Body deviceInfo: PasswordUpdateRequest) : Call<PasswordUpdateResponse>

    // ------------ THIS ARE EXAMPLES FOR NODE JS --------------




    // ------------ THIS ARE EXAMPLES FOR JAVA SPRING BOOT --------------

    @POST("api/auth/signin")
    fun getLoginUserDataSpringBoot( @Body deviceInfo: LoginRequestJava) : Call<LoginResponseJava>

    @POST("api/auth/signup")
    fun getRegisterUserData(@Body deviceInfo: RegisterRequest) : Call<RegisterResponse>

    @POST("api/auth/solvedSubMasterClass")
    fun userSolvedSubMasterClass(@Body deviceInfo: SolvedSubMasterClassRequest) : Call<SolvedSubMasterClassResponse>

    @POST("api/auth/{userId}/userDataSplash/")
    fun getUserDataSplashScreen(@Path("userId") userId: Long) : Call<SplashScreenResponse>


    // ------------ THIS ARE EXAMPLES FOR JAVA SPRING BOOT --------------

}
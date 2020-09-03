package com.example.perfect_pitch_trainer.service

import com.example.perfect_pitch_trainer.model.MasterClassMainData
import com.example.perfectpitchcoach.model.PracticeQuestions
import com.example.perfectpitchcoach.model.PratcticeBatch
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface EndpointDataMasterClass {

    @GET(value = "masterclasses.json")
    fun getJsonFileMasterClass() : Deferred<Response<MasterClassMainData>>


    @GET( value = "{subMasterClassFilename}")
    fun getSubMasterClassData(@Path(value = "subMasterClassFilename", encoded = false) url: String ) : Deferred<Response<MasterClassMainData>>


    @GET( value = "{subMasterClassFilename}")
    fun getPracticesQuestion(@Path(value = "subMasterClassFilename", encoded = false) url: String ) : Deferred<Response<PracticeQuestions>>
}
package com.example.perfect_pitch_trainer.model

import com.example.perfectpitchcoach.database.model.MasterClass
import com.example.perfectpitchcoach.database.model.SubMasterClass
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginResponseJava {

    @SerializedName("success")
    //@Expose
    // I don't know why here is this @expose, or what he does
    var success: Boolean = false

    @SerializedName("message")
    var message: String = ""

    @SerializedName("userId")
    var userId: Long = 0

    @SerializedName("masterClasses")
    var masterClassList: List<MasterClass> = listOf()

    @SerializedName("subMasterClasses")
    var subMasterClassList: MutableList<SubMasterClass> = mutableListOf()
    //@SerializedName("currentUser")
    //var userData: UserMusicData = UserMusicData()
}
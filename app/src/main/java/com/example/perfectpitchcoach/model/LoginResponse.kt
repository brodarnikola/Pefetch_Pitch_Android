package com.example.perfect_pitch_trainer.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginResponse {

    @SerializedName("success")
    //@Expose
    // I don't know why here is this @expose, or what he does
    var success: Boolean = false

    @SerializedName("message")
    var message: String = ""

    @SerializedName("currentUser")
    var userData: UserMusicData = UserMusicData()
}
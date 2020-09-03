package com.example.perfect_pitch_trainer.model

import com.google.gson.annotations.SerializedName

class LoginRequestJava {

    @SerializedName("usernameOrEmail")
    var usernameOrEmail: String = ""

    @SerializedName("password")
    var password: String = ""
}
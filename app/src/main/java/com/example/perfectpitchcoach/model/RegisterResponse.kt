package com.example.perfect_pitch_trainer.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegisterResponse {

    /* @SerializedName("code")
    @Expose
    var code: String = "" */

    @SerializedName("message")
    @Expose
    var message: String = ""

    @SerializedName("success")
    @Expose
    var success: Boolean = false

}
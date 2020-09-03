package com.example.perfect_pitch_trainer.model

import com.google.gson.annotations.SerializedName

class PasswordRecoveryResponse {

    @SerializedName("message")
    var message: String = ""

    @SerializedName("success")
    var success: Boolean = false

    @SerializedName("randomConfirmationNumber")
    var randomConfirmationNumber: String = ""

    @SerializedName("email")
    var email: String = ""
}
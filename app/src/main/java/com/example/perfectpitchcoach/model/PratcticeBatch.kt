package com.example.perfectpitchcoach.model

import com.example.perfect_pitch_trainer.model.MasterClassList
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PratcticeBatch {

    @SerializedName("id")
    @Expose
    val id: Int = 0

    @SerializedName("question")
    val question : List<List<String>> = listOf()

    @SerializedName("anwser")
    val anwser : List<List<String>> = listOf()

    var answeredCorrect: String = ""
}
package com.example.perfectpitchcoach.model

import com.example.perfect_pitch_trainer.model.MasterClassList
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PracticeQuestions {

    @SerializedName("group")
    @Expose
    val group: String? = null

    @SerializedName("practice")
    @Expose
    val practice: String? = null

    @SerializedName("description")
    @Expose
    val description: String? = null

    @SerializedName("uuid")
    @Expose
    val uuid: String? = null

    @SerializedName("maxHits")
    @Expose
    val maxHits: Int? = 0

    @SerializedName("practiceTyp")
    @Expose
    val practiceTyp: String? = null

    @SerializedName("practiceBatch")
    @Expose
    val practiceBatch: MutableList<PratcticeBatch> = mutableListOf()
}
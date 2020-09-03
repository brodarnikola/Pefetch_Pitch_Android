package com.example.perfect_pitch_trainer.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



public class MasterClassMainData {

    @SerializedName("description")
    @Expose
    val description: String? = null

    @SerializedName("list")
    @Expose
    val list: MutableList<MasterClassList> = mutableListOf()
}
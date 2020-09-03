package com.example.perfect_pitch_trainer.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class MasterClassList {

    @SerializedName("name")
    @Expose
    val name: String = ""
    @SerializedName("filename")
    @Expose
    val filename: String = ""

    /* fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }

    fun getQuestion(): List<List<String>>? {
        return question
    }

    fun setQuestion(question: List<List<String>>) {
        this.question = question
    }

    fun getAnwser(): List<List<String>>? {
        return anwser
    }

    fun setAnwser(anwser: List<List<String>>) {
        this.anwser = anwser
    } */

}
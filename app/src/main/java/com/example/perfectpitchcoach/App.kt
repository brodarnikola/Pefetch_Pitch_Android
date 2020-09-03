package com.example.perfectpitchcoach

import android.app.Application
import com.example.perfect_pitch_trainer.settings.AppPreferences
import com.example.perfectpitchcoach.database.model.SubMasterClass
import com.example.perfectpitchcoach.model.ParsedJsonFiles
import com.example.perfectpitchcoach.model.PracticeQuestions

class App : Application() {
    companion object {
        @JvmStatic
        lateinit var ref: App

        lateinit var parsedJsonSubMasterClassesFiles: MutableList<ParsedJsonFiles>

        lateinit var parsedPracticeBatchHashMap: LinkedHashMap<String, PracticeQuestions>

        lateinit var practiceBatch: PracticeQuestions

        lateinit var backendSubMasterClassData: MutableList<SubMasterClass>
    }

    init {
        ref = this
        parsedJsonSubMasterClassesFiles = mutableListOf()
        parsedPracticeBatchHashMap = linkedMapOf()
        practiceBatch = PracticeQuestions()

        backendSubMasterClassData = mutableListOf()
    }


    override fun onCreate() {
        super.onCreate()
        AppPreferences.init(this)
    }
}
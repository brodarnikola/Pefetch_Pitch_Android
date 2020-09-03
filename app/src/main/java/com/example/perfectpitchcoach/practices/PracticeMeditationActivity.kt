package com.example.perfectpitchcoach.practices

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.example.perfectpitchcoach.R
import com.example.perfectpitchcoach.audio.MidiPlayer

class PracticeMeditationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_practice_meditation)

        val tvPitch = findViewById<TextView>(R.id.tvPitch)
        val tvScore = findViewById<TextView>(R.id.tvScore)
        val tvScoreMax = findViewById<TextView>(R.id.tvScoreMax)
        val tvDescription = findViewById<TextView>(R.id.tvDescription)

        val btnPlay = findViewById<TextView>(R.id.btnPlay)
        val btnNext = findViewById<TextView>(R.id.btnNext)
        val btnPlayC = findViewById<TextView>(R.id.btnPlayC)
        val btnSkip = findViewById<TextView>(R.id.btnSkip)


        btnPlayC?.setOnClickListener {
            MidiPlayer.playMultipleNotesMelodicly(listOf("c'"))
        }

        btnSkip?.setOnClickListener {

        }
    }
}
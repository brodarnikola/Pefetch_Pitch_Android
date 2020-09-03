package com.example.perfectpitchcoach.practices

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.text.InputFilter
import com.example.perfectpitchcoach.audio.MidiPlayer
import com.example.perfectpitchcoach.audio.PitchParser
import com.example.perfectpitchcoach.R


class PracticeTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice_test)

        val tvLog = findViewById<TextView>(R.id.tvLog)
        val etLog = findViewById<EditText>(R.id.etLog)
//        val etLogTv = findViewById<TextView>(R.id.etLogTv)
        val btnMidiToPitch = findViewById<TextView>(R.id.btnMidiToPitch)
        val btnPitchToMidi = findViewById<TextView>(R.id.btnPitchToMidi)
        val btnPlayNotesMelodicly = findViewById<TextView>(R.id.btnPlayNotesMelodicly)
        val btnPlayNotesHarmonicly = findViewById<TextView>(R.id.btnPlayNotesHarmonicly)

        etLog.filters = arrayOf(InputFilter.AllCaps())

        btnMidiToPitch?.setOnClickListener {
            //TODO: is there a oneliner?
            val midiList = arrayListOf<Int>()
            val textList = etLog.text.toString().toLowerCase().filter { it in "cdefgahb" }.split(" ")
            for (text in textList) {
                val number = text.toIntOrNull()
                if (number != null) {
                    midiList.add(number)
                }
            }
            val pitchList = PitchParser.getPitchListFromMidiList(midiList)
            val tvString = pitchList.joinToString(" ")
            tvLog.setText(tvString)
        }

        btnPitchToMidi?.setOnClickListener {
            val pitchList = etLog.text.toString().toLowerCase().split(" ")
            var tvString = ""
            if (PitchParser.isPitchListValid(pitchList)) {
                val midiList = PitchParser.getMidiListFromPitchList(pitchList)
                tvString = midiList.joinToString(" ")
            } else {
                tvString = "Syntax error"
            }
            tvLog.setText(tvString)
        }

        btnPlayNotesMelodicly?.setOnClickListener {
            val pitchList = etLog.getText().toString().toLowerCase().split(" ")
            if (PitchParser.isPitchListValid(pitchList)) {
                MidiPlayer.playMultipleNotesMelodicly(pitchList)
            }
        }

        btnPlayNotesHarmonicly?.setOnClickListener {
            val pitchList = etLog.getText().toString().toLowerCase().split(" ")
            if (PitchParser.isPitchListValid(pitchList)) {
                MidiPlayer.playMultipleNotesHarmonicly(pitchList)
            }
        }
    }
}
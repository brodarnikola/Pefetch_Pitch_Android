package com.example.perfectpitchcoach.audio

import android.media.AudioAttributes
import android.media.SoundPool
import android.util.Log
import android.widget.Toast
import com.example.perfectpitchcoach.App
import java.util.*
import kotlin.concurrent.schedule

object MidiPlayer {

    val noteDuration = 1000L
    val firstTone = 21
    val lastTone = 108
    var streamId = mutableMapOf<Int, Int>()
    var loadedSoundCount = 0

    var attributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()

    val soundPool = SoundPool.Builder()
        .setAudioAttributes(attributes)
        .setMaxStreams(7)
        .build();

    fun getResNote(midi: Int): Int {
        return App.ref.resources.getIdentifier("piano_" + midi, "raw", App.ref.packageName)
    }

    init {
        for (x in firstTone..lastTone) {
            soundPool.load(
                App.ref,
                getResNote(x), 1)
        }
        soundPool.setOnLoadCompleteListener(SoundPool.OnLoadCompleteListener { soundPool, sampleId, status ->

            loadedSoundCount++
            if (loadedSoundCount >= (lastTone - firstTone)) {
                Toast.makeText(
                    App.ref,
                    "Sound load finished",
                    Toast.LENGTH_SHORT
                ).show()
            }
            Log.e("MidiPlayer", soundPool.toString() + sampleId.toString() + status.toString())
        })
    }

    fun noteOn(midi: Int) {
        Log.e("MidiPlayer", "noteOn: " + midi.toString())
        val sndId = midi - firstTone + 1
        val strId = soundPool.play(sndId, 1F, 1F, 1, 0, 1F)
        streamId.put(sndId, strId)
    }

    fun noteOff(midi: Int) {
        Log.e("MidiPlayer", "noteOff: " + midi.toString())
        val sndId = midi - firstTone + 1
        soundPool.stop(streamId[sndId]!!)
        streamId.remove(sndId)
    }

    fun playMultipleNotesMelodicly(pitchList: List<String>) {
        Log.e("MidiPlayer", pitchList.toString())
        val pitch = pitchList.get(0)
        val midi = PitchParser.getMidiFromPitch(pitch)
        noteOn(midi)
        Timer().schedule(noteDuration) {
            noteOff(midi)
            if (pitchList.lastIndex > 0) {
                playMultipleNotesMelodicly(
                    pitchList.subList(
                        1,
                        pitchList.lastIndex + 1
                    )
                )
            }
        }
    }

    fun playMultipleNotesHarmonicly(pitchList: List<String>) {
        for (pitch in pitchList) {
            val midi = PitchParser.getMidiFromPitch(pitch)
            noteOn(midi)
            Timer().schedule((noteDuration * 2.0).toLong()) {
                noteOff(midi)
            }
        }
    }
}
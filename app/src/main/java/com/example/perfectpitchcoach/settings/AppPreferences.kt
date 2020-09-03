package com.example.perfect_pitch_trainer.settings

import android.content.Context
import android.content.SharedPreferences

object AppPreferences {
    private const val NAME = "PerfectPitch"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    // list of app specific preferences
    private val IS_FIRST_RUN_PREF = Pair("is_first_run", false)
    private val IS_USER_LOGGED_IN = Pair("is_user_logged_in", false)
    private val USER_ID = Pair("user_id", 0)

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    /**
     * SharedPreferences extension function, so we won't need to call edit() and apply()
     * ourselves on every SharedPreferences operation.
     */
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var firstRun: Boolean
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getBoolean(IS_FIRST_RUN_PREF.first, IS_FIRST_RUN_PREF.second)

        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putBoolean(IS_FIRST_RUN_PREF.first, value)
        }

    var userLoggedIn: Boolean
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getBoolean(IS_USER_LOGGED_IN.first, IS_USER_LOGGED_IN.second)

        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putBoolean(IS_USER_LOGGED_IN.first, value)
        }

    var userId: Long
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getLong(USER_ID.first, USER_ID.second.toLong())

        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putLong(USER_ID.first, value)
        }
}
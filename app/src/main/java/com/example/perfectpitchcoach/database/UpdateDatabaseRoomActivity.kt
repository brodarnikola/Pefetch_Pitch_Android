package com.example.perfectpitchcoach.database

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.perfectpitchcoach.R
import java.util.*

class UpdateDatabaseRoomActivity : AppCompatActivity() {

    private lateinit var tvFirstNumber: TextView
    private lateinit var tvSecondNumber: TextView
    private lateinit var etFirstNumber: EditText

    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_database_room)
        tvFirstNumber = findViewById(R.id.tvFirstNumber)
        tvSecondNumber = findViewById(R.id.tvSecondNumber)
        etFirstNumber = findViewById(R.id.etFirstNumber)

        val r = Random()
        val r1 = r.nextInt(20 - 10) + 10
        val r2 = r.nextInt(20 - 10) + 10

        val calculate: Int = r1 + r2

        tvFirstNumber.text = "" + r1
        tvSecondNumber.text = "" + r2


        val extras = getIntent().getExtras()
        if (extras != null) {
            userId = extras.getInt("WholeMasterClassObject")
            println("User id is: " + userId )
        }


        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {

            var userInputNumber = etFirstNumber.text.toString().toInt()

            val replyIntent = Intent()
            if (TextUtils.isEmpty(etFirstNumber.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else if (calculate == userInputNumber) {

                replyIntent.putExtra(EXTRA_REPLY_USER_ID, userId.toString())
                replyIntent.putExtra(EXTRA_REPLY_USER_SOLVED_MASTERCLASS, "YES")
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }



    companion object {
        const val EXTRA_REPLY_USER_SOLVED_MASTERCLASS = "com.example.android.wordlistsql.EXTRA_REPLY_USER_SOLVED_MASTERCLASS"
        const val EXTRA_REPLY_USER_ID = "com.example.android.wordlistsql.EXTRA_REPLY_USER_ID"
    }

}
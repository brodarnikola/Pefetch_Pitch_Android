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


class NewWordActivity : AppCompatActivity() {

    private lateinit var tvFirstNumber: TextView
    private lateinit var tvSecondNumber: TextView
    private lateinit var etFirstNumber: EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_world)

        tvFirstNumber = findViewById(R.id.tvFirstNumber)
        tvSecondNumber = findViewById(R.id.tvSecondNumber)
        etFirstNumber = findViewById(R.id.etFirstNumber)

        val r = Random()
        val r1 = r.nextInt(20 - 10) + 10
        val r2 = r.nextInt(20 - 10) + 10

        val calculate: Int = r1 + r2

        tvFirstNumber.text = "" + r1
        tvSecondNumber.text = "" + r2

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {

            var userInputNumber = etFirstNumber.text.toString().toInt()

            val replyIntent = Intent()
            if (TextUtils.isEmpty(etFirstNumber.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else if( calculate == userInputNumber ) {
                val masterClass = etFirstNumber.text.toString()
                replyIntent.putExtra(EXTRA_REPLY_NAME, masterClass)
                replyIntent.putExtra(EXTRA_REPLY_STATUS, "YES")
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY_NAME = "com.example.android.wordlistsql.REPLY_NAME"
        const val EXTRA_REPLY_STATUS = "com.example.android.wordlistsql.REPLY_STATUS"
    }
}


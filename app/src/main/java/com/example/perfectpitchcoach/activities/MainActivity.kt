package com.example.perfectpitchcoach.activities

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.perfect_pitch_trainer.service.RetrofitGithubJsonFiles
import com.example.perfect_pitch_trainer.service.RetrofitUserService
import com.example.perfect_pitch_trainer.settings.AppPreferences
import com.example.perfectpitchcoach.App
import com.example.perfectpitchcoach.R
import com.example.perfectpitchcoach.database.MasterClassesActivity
import com.example.perfectpitchcoach.database.viewModels.MasterClassViewModel
import com.example.perfectpitchcoach.practices.PracticeMeditationActivity
import com.example.perfectpitchcoach.practices.PracticeTestActivity
import kotlinx.coroutines.*
import ru.gildor.coroutines.retrofit.await

class MainActivity : AppCompatActivity() {

    private lateinit var masterClassViewModel: MasterClassViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<android.support.v7.widget.Toolbar>(R.id.toolbar)
        this.setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val ivLogout = findViewById<ImageView>(R.id.ivLogout)
        val btnTestPractice = findViewById<TextView>(R.id.btnTestPractice)
        val btnMeditation = findViewById<TextView>(R.id.btnMeditation)
        val btnRoomDatabase = findViewById<TextView>(R.id.btnRoomDatabase)

        ivLogout.setOnClickListener {

            masterClassViewModel = ViewModelProviders.of(this).get(MasterClassViewModel::class.java)

            AppPreferences.userLoggedIn = false
            AppPreferences.userId = 0
            masterClassViewModel.deleteAllMasterClassData()
            masterClassViewModel.deleteAllSubMasterClassData()

            App.parsedJsonSubMasterClassesFiles = mutableListOf()
            App.parsedPracticeBatchHashMap = linkedMapOf()

            val mainIntent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(mainIntent)
            finish()
        }

        btnTestPractice.setOnClickListener {
            val mainIntent = Intent(this@MainActivity, PracticeTestActivity::class.java)
            startActivity(mainIntent)
            finish()
        }

        btnMeditation.setOnClickListener {
            val mainIntent = Intent(this@MainActivity, PracticeMeditationActivity::class.java)
            startActivity(mainIntent)
            finish()
        }

        btnRoomDatabase.setOnClickListener {
            val mainIntent = Intent(this@MainActivity, MasterClassesActivity::class.java)
            startActivity(mainIntent)
            finish()
        }
    }
}

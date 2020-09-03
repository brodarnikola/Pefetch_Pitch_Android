package com.example.perfectpitchcoach.activities


import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import com.example.perfect_pitch_trainer.service.RetrofitGithubJsonFiles
import com.example.perfect_pitch_trainer.settings.AppPreferences
import com.example.perfectpitchcoach.R
import kotlinx.coroutines.*
import android.arch.lifecycle.Observer
import com.example.perfect_pitch_trainer.model.LoginResponseJava
import com.example.perfect_pitch_trainer.model.MasterClassMainData
import com.example.perfect_pitch_trainer.model.SplashScreenResponse
import com.example.perfect_pitch_trainer.service.RetrofitUserService
import com.example.perfectpitchcoach.App
import com.example.perfectpitchcoach.audio.MidiPlayer
import com.example.perfectpitchcoach.database.databaseAccessObject_DAO.MasterClassDao
import com.example.perfectpitchcoach.database.model.MasterClass
import com.example.perfectpitchcoach.database.model.SubMasterClass
import com.example.perfectpitchcoach.database.repository.MasterClassRepository
import com.example.perfectpitchcoach.database.viewModels.MasterClassViewModel
import okhttp3.Response
import ru.gildor.coroutines.retrofit.await
import com.example.perfectpitchcoach.database.WordRoomDatabase
import com.example.perfectpitchcoach.database.model.Word


class SplashScreen : AppCompatActivity() {

    lateinit var progressBar: ProgressBar

    private lateinit var masterClassViewModel: MasterClassViewModel
    private lateinit var masterClassRepository: MasterClassRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        progressBar = findViewById(R.id.progressBar)

        masterClassViewModel = ViewModelProviders.of(this).get(MasterClassViewModel::class.java)
        //masterClassRepository = MasterClassRepository(masterClassDao = MasterClassDao)

        //val mainIntent = Intent(this@SplashScreen, MainActivity::class.java)
        //startActivity(mainIntent)
        //finish()
        if (AppPreferences.firstRun == false || AppPreferences.userLoggedIn == false) {
            //Log.d("Thrre Fragment", "The value of our pref is: ${AppPreferences.firstRun}")

            val mainIntent = Intent(this@SplashScreen, IntroductionActivity::class.java)
            startActivity(mainIntent)
            finish()
        } else {

            GlobalScope.launch {

                try {

                    val retrofitUserData = RetrofitUserService().getRetrofit(applicationContext)
                    val retrofitGithubJsonFiles = RetrofitGithubJsonFiles().getRetrofit()

                    val userDataWork = async { retrofitUserData.getUserDataSplashScreen(AppPreferences.userId) }
                    val jsonFilesWork = async { retrofitGithubJsonFiles.getJsonFileMasterClass() }

                    val resultJsonFiles = jsonFilesWork.await().await()
                    val resultUserData = userDataWork.await().await()

                    if (resultJsonFiles != null) {

                        updateMasterClassTable(resultJsonFiles, resultUserData, this)
                        updateSubMasterClassTable(resultUserData)
                    }

                    MidiPlayer


                    withContext(Dispatchers.Main) {

                        progressBar.visibility = View.GONE

                        if (AppPreferences.userLoggedIn == true) {
                            val mainIntent = Intent(this@SplashScreen, MainActivity::class.java)
                            startActivity(mainIntent)
                            finish()
                        } else {
                            val mainIntent = Intent(this@SplashScreen, LoginActivity::class.java)
                            startActivity(mainIntent)
                            finish()
                        }

                    }

                } catch (exception: Exception) {

                    withContext(Dispatchers.Main) {

                        progressBar.visibility = View.GONE
                    }

                    exception.printStackTrace()
                    exception.printStackTrace()
                }
            }
        }
    }

    private fun updateMasterClassTable(resultJsonFiles: retrofit2.Response<MasterClassMainData>,
                                       resultUserData: SplashScreenResponse,
                                       courotineScope: CoroutineScope) {

        val agentDao = WordRoomDatabase.getDatabase(this@SplashScreen, courotineScope)
        val aaa = agentDao.masterClassDao().getAllMasterClasses()

        //val aaaa = masterClassRepository.allMasterClasses.value
        val bb = masterClassViewModel.getAllMasterClasses()
        val ccc = masterClassViewModel.allMasterClasses.value
        //val ddd = masterClassRepository.allMasterClasses


        masterClassViewModel.allMasterClasses.observe(this@SplashScreen, Observer { masterClasses ->

            if (masterClasses?.size == 0) {
                for (webJsonItems in resultJsonFiles.body()?.list!!) {

                    var masterClass: MasterClass = MasterClass("", "", "")
                    masterClass.name = webJsonItems.name
                    masterClass.solved = "NO"
                    masterClass.filename = webJsonItems.filename
                    masterClassViewModel.insertMasterClasses(masterClass)
                }
            } else {
                // Add new masterClass practice from json file, to entity, table in masterClass roomdatabase.
                for (webJsonItems in resultJsonFiles.body()?.list!!) {

                    var masterClassNotExists: Boolean = false

                    var masterClass: MasterClass = MasterClass("", "", "")
                    if (masterClasses != null) {
                        for (roomDatabaseItems in masterClasses) {

                            if (webJsonItems.name == roomDatabaseItems.name) {

                                masterClassNotExists = false
                                break

                            } else {
                                masterClassNotExists = true
                                masterClass.name = webJsonItems.name
                                masterClass.filename = webJsonItems.filename
                                masterClass.solved = "NO"
                            }
                        }
                    }

                    if (masterClassNotExists == true) {
                        masterClassViewModel.insertMasterClasses(masterClass)
                    }
                }

                // check if user has solved some masterClass, then change properties from "NO" to "YES",
                // so that we can updateMasterClassWithUserId screen, with correct background color
                if (AppPreferences.userId != 0.toLong())
                    for (backendData in resultUserData.masterClassList) {
                        if (masterClasses != null) {
                            for (roomDatabaseItems in masterClasses) {

                                if (backendData.name == roomDatabaseItems.name) {

                                    masterClassViewModel.updateMasterClassWithOutUserId(
                                        roomDatabaseItems.name.toString(),
                                        "YES"
                                    )
                                    break
                                }
                            }
                        }
                    }
            }
        })

        println("SPLASHACTITIVTY ispis na drugom mjestu je: " + resultJsonFiles)
        println("SPLASHACTITIVTY ispis na drugom mjestu je: " + resultJsonFiles)
        println("SPLASHACTITIVTY ispis na drugom mjestu je: " + resultJsonFiles)
        println("SPLASHACTITIVTY ispis na drugom mjestu je: " + resultJsonFiles)

    }


    private fun updateSubMasterClassTable(resultUserData: SplashScreenResponse) {

        masterClassViewModel.allSubMasterClasses.observe(this@SplashScreen, Observer { subMasterClass ->

            // check if user has solved some masterClass, then change properties from "NO" to "YES",
            // so that we can updateMasterClassWithUserId screen, with correct background color
            if (AppPreferences.userId != 0.toLong()) {
                if (subMasterClass != null) {
                    for (backendData in resultUserData.subMasterClassList) {

                        var addNewSubMasterClass = true
                        var subMasterClassDao: SubMasterClass? = null
                        for (roomDatabaseItems in subMasterClass) {

                            if (backendData.name == roomDatabaseItems.name
                                && backendData.masterClassName == roomDatabaseItems.masterClassName  ) {

                                addNewSubMasterClass = false
                                break
                            }
                            subMasterClassDao = roomDatabaseItems
                        }
                        if (addNewSubMasterClass) {
                            if (subMasterClassDao != null) {
                                masterClassViewModel.insertSubMasterClasses(subMasterClassDao)
                            }
                        }
                    }
                }
            }
        })

    }


}

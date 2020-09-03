package com.example.perfectpitchcoach.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.example.perfect_pitch_trainer.model.LoginRequestJava
import com.example.perfect_pitch_trainer.model.LoginResponseJava
import com.example.perfect_pitch_trainer.model.MasterClassMainData
import com.example.perfect_pitch_trainer.service.RetrofitGithubJsonFiles
import com.example.perfect_pitch_trainer.service.RetrofitUserService
import com.example.perfect_pitch_trainer.settings.AppPreferences
import com.example.perfect_pitch_trainer.util.NetworkUtil
import com.example.perfectpitchcoach.App
import com.example.perfectpitchcoach.R
import com.example.perfectpitchcoach.database.model.MasterClass
import com.example.perfectpitchcoach.database.model.SubMasterClass
import com.example.perfectpitchcoach.database.viewModels.MasterClassViewModel
import kotlinx.coroutines.*
import okhttp3.Response
import ru.gildor.coroutines.retrofit.await

class LoginActivity : AppCompatActivity() {

    lateinit var context: Context

    lateinit  var btLogin: Button
    lateinit var  etUsername: EditText
    lateinit var  etPassword: EditText
    lateinit  var btRegisterActivity: Button
    lateinit  var tvForgotPassword: TextView
    lateinit var  tvBackendResponseErrorMessage: TextView
    lateinit var progressBar: ProgressBar

    private lateinit var masterClassViewModel: MasterClassViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        context = this

        masterClassViewModel = ViewModelProviders.of(this).get(MasterClassViewModel::class.java)

        btLogin = findViewById(R.id.btLogin)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btRegisterActivity = findViewById(R.id.btRegisterActivity)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)
        tvBackendResponseErrorMessage = findViewById(R.id.tvBackendResponseErrorMessage)
        progressBar = findViewById(R.id.progressBar)

        val extras = getIntent().getExtras()
        if (extras != null) {
            val reponseBackend = extras.getString("backendResponseSuccess")
            val reponseBackendMessage = extras.getString("backendResponseMessage")

            tvBackendResponseErrorMessage.setTextColor( ContextCompat.getColor( context, R.color.colorPrimary))
            tvBackendResponseErrorMessage.visibility = View.VISIBLE
            tvBackendResponseErrorMessage.text = reponseBackendMessage

            if( extras.getString("username") != "" ) {
                etUsername.setText(extras.getString("username"))
                etPassword.setText(extras.getString("password"))
            }
        }


        btRegisterActivity.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                val randomIntent = Intent (this@LoginActivity, RegisterActivity::class.java)
                finish()
                startActivity(randomIntent)

            }
        })

        tvForgotPassword.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                val randomIntent = Intent (this@LoginActivity, ForgotPasswordActivity::class.java)
                finish()
                startActivity(randomIntent)
            }
        })

        btLogin.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                if( NetworkUtil.checkIfUserHasInternetConnection(context ) ) {

                    tvBackendResponseErrorMessage.setTextColor( ContextCompat.getColor( context, android.R.color.holo_red_dark))
                    if( etUsername.text.toString() == "" ) {

                        tvBackendResponseErrorMessage.visibility = View.VISIBLE
                        tvBackendResponseErrorMessage.text = "You did not fill username"
                    }
                    else if( etPassword.text.toString() == "" ) {

                        tvBackendResponseErrorMessage.visibility = View.VISIBLE
                        tvBackendResponseErrorMessage.text = "You did not fill password"
                    }
                    else if( etUsername.text.length > 24 ) {

                        tvBackendResponseErrorMessage.visibility = View.VISIBLE
                        tvBackendResponseErrorMessage.text = "Your password is to large, maximum allowed number of characters are 25. " +
                                "You have " +  etUsername.text.length + " charachters"
                    }
                    else if( etPassword.text.length > 24 ) {

                        tvBackendResponseErrorMessage.visibility = View.VISIBLE
                        tvBackendResponseErrorMessage.text = "Your password is to large, maximum allowed number of characters are 25. " +
                                "You have " +  etPassword.text.length + " charachters"
                    }
                    else {
                        progressBar.visibility = View.VISIBLE
                        val newLoginUser: LoginRequestJava = LoginRequestJava()
                        newLoginUser.usernameOrEmail = etUsername.text.toString()
                        newLoginUser.password = etPassword.text.toString()

                        loginUser(newLoginUser)
                    }
                }
                else {
                    tvBackendResponseErrorMessage.visibility = View.VISIBLE
                    tvBackendResponseErrorMessage.text = "You don't have internet connection"
                }
            }
        })

    }

    private fun loginUser(newLoginUser: LoginRequestJava) {

        val retrofitService = RetrofitUserService().getRetrofit(applicationContext)
        val retrofitGithubJsonFiles = RetrofitGithubJsonFiles().getRetrofit()

        GlobalScope.launch {

            try {

                val workUserData = async { retrofitService.getLoginUserDataSpringBoot(newLoginUser)  }
                val jsonFilesWork = async { retrofitGithubJsonFiles.getJsonFileMasterClass() }

                var resultUserData = workUserData.await().await()
                val resultJsonFiles = jsonFilesWork.await().await()

                if (resultJsonFiles != null) {

                    updateMasterClassTable(resultUserData, resultJsonFiles)
                    updateSubMasterClassTable(resultUserData)

                    println("SPLASHACTITIVTY ispis na drugom mjestu je: " + resultJsonFiles )
                    println("SPLASHACTITIVTY ispis na drugom mjestu je: " + resultJsonFiles )
                    println("SPLASHACTITIVTY ispis na drugom mjestu je: " + resultJsonFiles )
                    println("SPLASHACTITIVTY ispis na drugom mjestu je: " + resultJsonFiles )
                }

                withContext(Dispatchers.Main) {

                    progressBar.visibility = View.GONE
                    if( resultUserData?.success == true ) {
                        Log.d("LoginActivity", "2" + resultUserData?.message)

                        AppPreferences.userLoggedIn = true
                        AppPreferences.userId = resultUserData.userId

                        //do something with result
                        val randomIntent = Intent(this@LoginActivity, MainActivity::class.java)
                        randomIntent.putExtra("backendResponseSuccess", resultUserData?.success)
                        randomIntent.putExtra("backendResponseMessage", resultUserData?.message)

                        startActivity(randomIntent)

                        finish()
                    }
                    else {
                        tvBackendResponseErrorMessage.visibility = View.VISIBLE
                        tvBackendResponseErrorMessage.text = resultUserData?.message
                    }
                }

            } catch (exception: Exception) {

                withContext(Dispatchers.Main) {

                    progressBar.visibility = View.GONE
                    tvBackendResponseErrorMessage.visibility = View.VISIBLE
                    if( exception.message == "HTTP 401 " )
                        tvBackendResponseErrorMessage.text = "Wrong username or password"
                    else
                        tvBackendResponseErrorMessage.text = "Something went wrong, please try again latter"
                }

                exception.printStackTrace()
            }
        }

    }

    private fun updateSubMasterClassTable(resultUserData: LoginResponseJava) {

         for (backendData in resultUserData.subMasterClassList)
              masterClassViewModel.insertSubMasterClasses(backendData)
    }

    private fun updateMasterClassTable(resultUserData:LoginResponseJava,
                                       resultJsonFiles: retrofit2.Response<MasterClassMainData> ) {

        masterClassViewModel.allMasterClasses.observe( this@LoginActivity, Observer { masterClasses ->

            if( masterClasses?.size == 0 ) {
                for (webJsonItems in resultJsonFiles.body()?.list!!) {

                    var masterClass: MasterClass = MasterClass("", "", "")
                    masterClass.name = webJsonItems.name
                    masterClass.solved = "NO"
                    masterClass.filename = webJsonItems.filename
                    masterClassViewModel.insertMasterClasses(masterClass)
                }
            }
            else {
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
                if( AppPreferences.userId != 0.toLong() )
                    for( backendData in resultUserData.masterClassList ) {
                        if (masterClasses != null) {
                            for (roomDatabaseItems in masterClasses) {

                                if (backendData.name == roomDatabaseItems.name) {

                                    masterClassViewModel.updateMasterClassWithOutUserId(roomDatabaseItems.name.toString(), "YES")
                                    break
                                }
                            }
                        }
                    }
            }
        })
    }


}

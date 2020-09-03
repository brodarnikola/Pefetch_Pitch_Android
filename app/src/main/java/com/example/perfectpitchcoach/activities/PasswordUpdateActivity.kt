package com.example.perfectpitchcoach.activities

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
import com.example.perfect_pitch_trainer.model.PasswordUpdateRequest
import com.example.perfect_pitch_trainer.model.PasswordUpdateResponse
import com.example.perfect_pitch_trainer.service.RetrofitUserService
import com.example.perfect_pitch_trainer.util.NetworkUtil
import com.example.perfectpitchcoach.R
import kotlinx.coroutines.*
import ru.gildor.coroutines.retrofit.await

class PasswordUpdateActivity : AppCompatActivity() {

    lateinit var context: Context

    lateinit var progressBar: ProgressBar
    lateinit  var btNewPassword: Button
    lateinit  var etPassword: EditText
    lateinit  var etConfirmPassword: EditText
    lateinit  var etPin: EditText
    lateinit  var  tvBackendResponseErrorMessage: TextView
    var pinUser: String = ""
    var emailUser: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_update)

        context = this

        progressBar = findViewById(R.id.progressBar)
        btNewPassword = findViewById(R.id.btNewPassword)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        etPin = findViewById(R.id.etPin)
        tvBackendResponseErrorMessage = findViewById(R.id.tvBackendResponseErrorMessage)

        val extras = getIntent().getExtras()
        if (extras != null) {
            val reponseBackend = extras.getString("backendResponseSuccess")
            val reponseBackendMessage = extras.getString("backendResponseMessage")

            pinUser = extras.getString("randomConfirmationNumber")
            emailUser = extras.getString("emailUser")

            tvBackendResponseErrorMessage.visibility = View.VISIBLE
            tvBackendResponseErrorMessage.setTextColor( ContextCompat.getColor( context, R.color.colorPrimary))
            tvBackendResponseErrorMessage.text = reponseBackendMessage
        }

        btNewPassword.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {


                if( NetworkUtil.checkIfUserHasInternetConnection(context ) ) {

                    tvBackendResponseErrorMessage.setTextColor( ContextCompat.getColor( context, android.R.color.holo_red_dark))
                    if (etPassword.text.toString() == "" || etConfirmPassword.text.toString() == ""
                        || etPin.text.toString() == "" ) {

                        tvBackendResponseErrorMessage.visibility = View.VISIBLE
                        tvBackendResponseErrorMessage.text = "Please fill out all fields"
                    }
                    else if( etPassword.text.length > 24  ) {

                        tvBackendResponseErrorMessage.visibility = View.VISIBLE
                        tvBackendResponseErrorMessage.text =  "Your first password is to large, maximum allowed number of characters are 25. " +
                                "You have " +  etPassword.text.length + " charachters"
                    }
                    else if( etConfirmPassword.text.length > 24  ) {

                        tvBackendResponseErrorMessage.visibility = View.VISIBLE
                        tvBackendResponseErrorMessage.text =  "Your second password is to large, maximum allowed number of characters are 25. " +
                                "You have " +  etConfirmPassword.text.length + " charachters"
                    }
                    else if( etPassword.text.toString() != etConfirmPassword.text.toString() ) {

                        tvBackendResponseErrorMessage.visibility = View.VISIBLE
                        tvBackendResponseErrorMessage.text = "Both of your passwords don't match"
                    }
                    else if (etPin.text.toString() != pinUser) {

                        tvBackendResponseErrorMessage.visibility = View.VISIBLE
                        tvBackendResponseErrorMessage.text = "Pin that you received on email does not match"
                    } else {

                        progressBar.visibility = View.VISIBLE
                        val newPasswordUpdateUser: PasswordUpdateRequest = PasswordUpdateRequest()
                        newPasswordUpdateUser.password = etPassword.text.toString()
                        newPasswordUpdateUser.email = emailUser

                        sendPasswordUpdateToBackend(newPasswordUpdateUser)
                    }
                }
                else {
                    tvBackendResponseErrorMessage.visibility = View.VISIBLE
                    tvBackendResponseErrorMessage.text = "You don't have internet connection"
                }
            }
        })




    }

    private fun sendPasswordUpdateToBackend(newPasswordUpdateUser: PasswordUpdateRequest) {

        val retrofitService = RetrofitUserService().getRetrofit(applicationContext)

        var result3: PasswordUpdateResponse = PasswordUpdateResponse()

        GlobalScope.launch {

            try {

                val work2 = async { retrofitService.getPasswordUpdateData(newPasswordUpdateUser)  }
                //val work2 = async { retrofitService.getPosts()  }

                //val result2 =work2.await().await().body()
                result3 =work2.await().await()


                withContext(Dispatchers.Main) {

                    progressBar.visibility = View.GONE
                    if( result3?.success == true ) {
                        Log.d("RegisterActivity", "2" + result3?.message)

                        //do something with result
                        val randomIntent = Intent(this@PasswordUpdateActivity, LoginActivity::class.java)
                        randomIntent.putExtra("backendResponseSuccess", result3?.success)
                        randomIntent.putExtra("backendResponseMessage", result3?.message)

                        startActivity(randomIntent)

                        finish()
                    }
                    else {
                        tvBackendResponseErrorMessage.visibility = View.VISIBLE
                        tvBackendResponseErrorMessage.text = result3?.message
                    }
                }

            } catch (exception: Exception) {

                withContext(Dispatchers.Main) {

                    progressBar.visibility = View.GONE
                    tvBackendResponseErrorMessage.visibility = View.VISIBLE
                    tvBackendResponseErrorMessage.text = result3?.message
                }

                exception.printStackTrace()
                exception.printStackTrace()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val randomIntent = Intent (this@PasswordUpdateActivity, ForgotPasswordActivity::class.java)
        finish()
        startActivity(randomIntent)
    }

}

package com.example.perfectpitchcoach.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.example.perfect_pitch_trainer.model.PasswordRecoveryRequest
import com.example.perfect_pitch_trainer.model.PasswordRecoveryResponse
import com.example.perfect_pitch_trainer.service.RetrofitUserService
import com.example.perfect_pitch_trainer.util.CommonUtil
import com.example.perfect_pitch_trainer.util.NetworkUtil
import com.example.perfectpitchcoach.R
import kotlinx.coroutines.*
import ru.gildor.coroutines.retrofit.await

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var context: Context

    lateinit  var btEmail: Button
    lateinit  var etEmail: EditText
    lateinit  var tvBackendResponseErrorMessage: TextView
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        context = this

        btEmail = findViewById(R.id.btEmail)
        etEmail = findViewById(R.id.etEmail)
        tvBackendResponseErrorMessage = findViewById(R.id.tvBackendResponseErrorMessage)
        progressBar = findViewById(R.id.progressBar)

        btEmail.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                if( NetworkUtil.checkIfUserHasInternetConnection(context ) ) {

                    if( etEmail.text.toString() == "" ) {
                        tvBackendResponseErrorMessage.visibility = View.VISIBLE
                        tvBackendResponseErrorMessage.text = "Email can not be empty"
                    }
                    else if( CommonUtil.isEmailValid(etEmail.text.toString()) == false ) {
                        tvBackendResponseErrorMessage.visibility = View.VISIBLE
                        tvBackendResponseErrorMessage.text = "Email is in wrong format"
                    }
                    else {

                        progressBar.visibility = View.VISIBLE

                        val newPasswordRecoveryUser: PasswordRecoveryRequest = PasswordRecoveryRequest()
                        newPasswordRecoveryUser.email = etEmail.text.toString()

                        sendPasswordRecoveryToBackend(newPasswordRecoveryUser)
                    }
                }
                else {
                    tvBackendResponseErrorMessage.visibility = View.VISIBLE
                    tvBackendResponseErrorMessage.text = "You don't have internet connection"
                }
            }
        })
    }


    private fun sendPasswordRecoveryToBackend( newPasswordRecoveryUser: PasswordRecoveryRequest) {

        val retrofitService = RetrofitUserService().getRetrofit(applicationContext)

        var result3: PasswordRecoveryResponse = PasswordRecoveryResponse()

        GlobalScope.launch {

            try {

                val work2 = async { retrofitService.getPasswordRecoveryData(newPasswordRecoveryUser)  }
                //val work2 = async { retrofitService.getPosts()  }

                //val result2 =work2.await().await().body()
                result3 =work2.await().await()


                withContext(Dispatchers.Main) {

                    progressBar.visibility = View.GONE
                    if( result3?.success == true ) {
                        Log.d("RegisterActivity", "2" + result3?.message)

                        //do something with result
                        val randomIntent = Intent(this@ForgotPasswordActivity, PasswordUpdateActivity::class.java)
                        randomIntent.putExtra("backendResponseSuccess", result3?.success)
                        randomIntent.putExtra("backendResponseMessage", result3?.message)
                        randomIntent.putExtra("randomConfirmationNumber", result3?.randomConfirmationNumber)
                        randomIntent.putExtra("emailUser", result3?.email)

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

        val randomIntent = Intent (this@ForgotPasswordActivity, LoginActivity::class.java)
        finish()
        startActivity(randomIntent)
    }

}

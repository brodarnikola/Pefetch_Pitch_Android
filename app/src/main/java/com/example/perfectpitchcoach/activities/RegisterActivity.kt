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
import com.example.perfect_pitch_trainer.model.RegisterRequest
import com.example.perfect_pitch_trainer.model.RegisterResponse
import com.example.perfect_pitch_trainer.service.RetrofitUserService
import com.example.perfect_pitch_trainer.util.CommonUtil
import com.example.perfect_pitch_trainer.util.NetworkUtil
import com.example.perfectpitchcoach.R
import kotlinx.coroutines.*
import okhttp3.*
import ru.gildor.coroutines.retrofit.await
import java.io.IOException
import java.io.InputStream
import java.net.URL
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection

class RegisterActivity : AppCompatActivity() {

    lateinit var context: Context

    lateinit  var etName: EditText
    lateinit  var etSurname: EditText
    lateinit  var etEmail: EditText
    lateinit  var etUsername: EditText
    lateinit  var etPassword: EditText
    lateinit  var btRegisterUser: Button
    lateinit var tvErrorMessage: TextView
    lateinit var progressBar: ProgressBar

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_register)

        context = this
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        etName = findViewById(R.id.etName)
        etSurname = findViewById(R.id.etSurname)
        etEmail = findViewById(R.id.etEmail)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btRegisterUser = findViewById(R.id.btRegisterUser)
        tvErrorMessage = findViewById(R.id.tvErrorMessage)
        progressBar = findViewById(R.id.progressBar)

        //registerUserFirstExampleOkHttp()

        btRegisterUser.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                if( NetworkUtil.checkIfUserHasInternetConnection(context ) ) {
                    if (etName.text.toString() == "" || etSurname.text.toString() == "" || etEmail.text.toString() == ""
                        ||  etUsername.text.toString() == "" ||  etPassword.text.toString() == "" ) {

                        tvErrorMessage.visibility = View.VISIBLE
                        tvErrorMessage.text = "You did not fill all fields"
                    }
                    else if( CommonUtil.isEmailValid(etEmail.text.toString()) == false ) {
                        tvErrorMessage.visibility = View.VISIBLE
                        tvErrorMessage.text = "Email is in wrong format"
                    }
                    else if( etPassword.text.length > 24 ) {
                        tvErrorMessage.visibility = View.VISIBLE
                        tvErrorMessage.text = "Your password is to large, maximum allowed number of characters are 25. " +
                                "You have " +  etPassword.text.length + " charachters"
                    }
                    else {
                        progressBar.visibility = View.VISIBLE
                        tvErrorMessage.visibility = View.GONE

                        val newRegisteredUser: RegisterRequest = RegisterRequest()
                        newRegisteredUser.name = etName.text.toString()
                        newRegisteredUser.surname = etSurname.text.toString()
                        newRegisteredUser.email = etEmail.text.toString()
                        newRegisteredUser.username = etUsername.text.toString()
                        newRegisteredUser.password = etPassword.text.toString()

                        registerUserSecondExampleRetrofit(newRegisteredUser)
                    }
                } else {
                    tvErrorMessage.visibility = View.VISIBLE
                    tvErrorMessage.text = "You don't have internet connection"
                }

                //registerUserThirdExampleHttpsUrlConnection(newRegisteredUser)
            }
        })
    }

    private fun registerUserThirdExampleHttpsUrlConnection(newRegisteredUser: RegisterRequest) {

        GlobalScope.launch {

            try {

                /* val deferred1 = async { calculateHardThings(10, "11111") }
                val deferred2 = async { calculateHardThings(10, "22222") }

                val sum = deferred1.await() + deferred2.await() */

                val hostnameVerifier = HostnameVerifier { _, session ->
                    HttpsURLConnection.getDefaultHostnameVerifier().run {
                        verify("https://192.168.1.5:5000/", session)
                    }
                }

// Tell the URLConnection to use our HostnameVerifier
                val url = URL("https://192.168.1.5:5000/signUpUser")
                val urlConnection = url.openConnection() as HttpsURLConnection
                urlConnection.hostnameVerifier = hostnameVerifier
                val inputStream: InputStream = urlConnection.inputStream
                //copyInputStreamToOutputStream(inputStream, System.out)

                withContext(Dispatchers.Main) {

                    Log.d("Ispis https request", inputStream.toString())
                    //var hashMapAppTerms = MyApplication.instance.items

                    //do something with result
                    val randomIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    // Start the new activity.
                    startActivity(randomIntent)

                    finish()
                }

            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }


    }

    private fun registerUserFirstExampleOkHttp() {
        GlobalScope.launch {
            val beginTimestamp = System.currentTimeMillis()
            val duration = System.currentTimeMillis() - beginTimestamp
            Log.d("", "App Start length:" + duration)
            if (duration < 1000) {
                delay(1000 - duration)
            }

            val newRegisteredUser: RegisterRequest = RegisterRequest()
            newRegisteredUser.name = etName.text.toString()
            newRegisteredUser.surname = etSurname.text.toString()
            newRegisteredUser.email = etEmail.text.toString()
            newRegisteredUser.username = etUsername.text.toString()
            newRegisteredUser.password = etPassword.text.toString()

            val formBody = FormBody.Builder()
                .add("name", etName.text.toString())
                .add("surname", etSurname.text.toString())
                .add("email", etEmail.text.toString())
                .add("username", etUsername.text.toString())
                .add("password", etPassword.text.toString())
                .build()
            //val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), newRegisteredUser)


            val request = Request.Builder()
                .url("https://localhost:5000/signUpUser")
                //.post(body)
                .post(formBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) = println(response.body()?.string())
            })

            withContext(Dispatchers.Main) {
                // startApp()
                //val randomIntent = Intent (this@RegisterActivity, MainActivity::class.java)
                // Start the new activity.
                // startActivity(randomIntent)
            }
        }
    }

    private fun registerUserSecondExampleRetrofit( newRegisteredUser: RegisterRequest) {

        val retrofitService = RetrofitUserService().getRetrofit(applicationContext)

        var result3: RegisterResponse = RegisterResponse()

        GlobalScope.launch {

            try {

                /* val deferred1 = async { calculateHardThings(10, "11111") }
                val deferred2 = async { calculateHardThings(10, "22222") }

                val sum = deferred1.await() + deferred2.await() */

                val work2 = async { retrofitService.getRegisterUserData(newRegisteredUser)  }
                //val work2 = async { retrofitService.getPosts()  }

                //val result2 =work2.await().await().body()
                result3 =work2.await().await()


                withContext(Dispatchers.Main) {

                    progressBar.visibility = View.GONE
                    if( result3?.success == true ) {
                        Log.d("RegisterActivity", "2" + result3?.message)

                        //do something with result
                        val randomIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        randomIntent.putExtra("backendResponseSuccess", result3?.success)
                        randomIntent.putExtra("backendResponseMessage", result3?.message)
                        randomIntent.putExtra("username", etUsername.text.toString())
                        randomIntent.putExtra("password", etPassword.text.toString())

                        startActivity(randomIntent)

                        finish()
                    }
                    else {
                        tvErrorMessage.visibility = View.VISIBLE
                        tvErrorMessage.text = result3?.message
                    }
                }

            } catch (exception: Exception) {

                withContext(Dispatchers.Main) {

                    progressBar.visibility = View.GONE
                    tvErrorMessage.visibility = View.VISIBLE
                    tvErrorMessage.text = exception.message
                }
                exception.printStackTrace()
                exception.printStackTrace()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val randomIntent = Intent (this@RegisterActivity, LoginActivity::class.java)
        finish()
        startActivity(randomIntent)
    }


}

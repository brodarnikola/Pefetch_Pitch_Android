package  com.example.perfect_pitch_trainer.service

import android.content.Context
import com.example.brc.kotlintestapp.Settings.EndpointsLoginRegister
import com.example.perfect_pitch_trainer.service.SelfSigningClientBuilder
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitUserService {


    companion object {

        // THAT WAS EXAMPLE FOR NODE JS, WHICH WORKED, THIS WORKED
        //private const val BASE_URL = "http://perfetchpitch.nodejsapp.net/"

        // THAT WAS EXAMPLE FOR JAVA SPRING BOOT, WHICH WORKED, THIS WORKED
        //private const val BASE_URL = "http://app-b561f1b8-6e3e-4e43-8fb8-1b19f0e357b3.cleverapps.io/"

        // localhost
        private const val BASE_URL = "http://192.168.1.58:5000/"
    }


    /* fun getServiceUtil(retrofit: Retrofit): EndpointsLoginRegister = retrofit.create(EndpointsLoginRegister::class.java)

    fun getGson() = GsonBuilder().create()!! */

    val selfSigningClientBuilder: SelfSigningClientBuilder = SelfSigningClientBuilder()

    fun getRetrofit( context: Context ): EndpointsLoginRegister {

        val gson = GsonBuilder()
                .setLenient()
                .create()

        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                //.client(selfSigningClientBuilder.createClient(context))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory( GsonConverterFactory.create()  )
                .client(makeOkHttpClient())
                .build().create(EndpointsLoginRegister::class.java)
    }

    private fun makeOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(makeLoggingInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
    }

    private fun makeLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level =
                HttpLoggingInterceptor.Level.BODY
        return logging
    }



}
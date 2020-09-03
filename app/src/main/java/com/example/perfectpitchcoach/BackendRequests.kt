package com.example.perfectpitchcoach

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import com.example.perfect_pitch_trainer.model.LoginResponseJava
import com.example.perfect_pitch_trainer.service.RetrofitUserService
import com.example.perfect_pitch_trainer.settings.AppPreferences
import com.example.perfectpitchcoach.activities.MainActivity
import com.example.perfectpitchcoach.model.SolvedSubMasterClassRequest
import kotlinx.coroutines.*
import ru.gildor.coroutines.retrofit.await
import kotlin.contracts.contract

object BackendRequests {

    /*sub_master_class_unique_id = extras.getString("sub_master_class_unique_id")
    masterClassName = extras.getString("masterClassName")
    subMasterClassName = extras.getString("subMasterClassName")*/

    fun userSolvedSubMasterClass(subMasterClassName: String, masterClassName: String, fileName: String, applicationContext: Context) {

        val getMasterClassId = masterClassName.split(" ")

        val retrofitService = RetrofitUserService().getRetrofit(applicationContext)

        var solvedSubMasterClass: SolvedSubMasterClassRequest = SolvedSubMasterClassRequest()
        solvedSubMasterClass.id_masterclass = getMasterClassId[2].toLong() // subMasterClassUniqueID.toInt()
        solvedSubMasterClass.masterClassName = masterClassName
        solvedSubMasterClass.name = subMasterClassName
        // I want to send also sub_master_class_unique_id and fileName
        solvedSubMasterClass.subMasterClassUniqueId = masterClassName + "___" + subMasterClassName
        solvedSubMasterClass.fileName = fileName
        solvedSubMasterClass.solved = "YES"
        solvedSubMasterClass.id_user = AppPreferences.userId

        GlobalScope.launch {

            try {

                val work2 =  async { retrofitService.userSolvedSubMasterClass(solvedSubMasterClass) }

                val solvedSubMasterClassReponse =work2.await().await()

                withContext(Dispatchers.Main) {

                    Log.d("LoginActivity", "2" )
                    Log.d("LoginActivity", "2" )
                    Log.d("LoginActivity", "2" )
                }

            } catch (exception: Exception) {

                withContext(Dispatchers.Main) {

                    Log.d("LoginActivity", "2" )
                    Log.d("LoginActivity", "2" )
                }

                Log.d("LoginActivity", "2" )
                exception.printStackTrace()
            }
        }
    }
}
package com.example.perfectpitchcoach.database.repository

/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.support.annotation.WorkerThread
import com.example.perfectpitchcoach.database.databaseAccessObject_DAO.MasterClassDao
import com.example.perfectpitchcoach.database.model.MasterClass
import com.example.perfectpitchcoach.database.model.SubMasterClass

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */
class MasterClassRepository(private val masterClassDao: MasterClassDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allMasterClasses: LiveData<List<MasterClass>> = masterClassDao.getAllMasterClasses()
    val allSubMasterClasses: LiveData<List<SubMasterClass>> = masterClassDao.getAlLSubMasterClasses()

    // You must call this on a non-UI thread or your app will crash. So we're making this a
    // suspend function so the caller methods know this.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getAllMasterClasses() {
        masterClassDao.getAllMasterClasses()
    }


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertMasterClasses(masterClass: MasterClass) {
        masterClassDao.insertMasterClasses(masterClass)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertSubMasterClasses(masterClass: SubMasterClass) {
        masterClassDao.insertSubMasterClasses(masterClass)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun findSubMasterClassesById(name: String) : LiveData<List<SubMasterClass>> {
       return  masterClassDao.findSubMasterClassesById(name)
    }




    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateMasterClassWithOutUserId(masterClassName: String, solved: String) {
        masterClassDao.updateMasterClassWithOutUserId(masterClassName, solved)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(userId: String, solved: String) {
        masterClassDao.updateUser(userId, solved)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateSubMasterClass(masterClassName: String, nameOfSubMasterClassPractice: String, solved: String) {
        masterClassDao.updateSubMasterClass(masterClassName, nameOfSubMasterClassPractice, solved)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAllMasterClassData() {
        masterClassDao.deleteAllMasterClassData()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAllSubMasterClassData() {
        masterClassDao.deleteAllSubMasterClassData()
    }


}

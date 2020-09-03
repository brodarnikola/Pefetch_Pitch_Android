package com.example.perfectpitchcoach.database.databaseAccessObject_DAO

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
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.example.perfectpitchcoach.database.model.MasterClass
import com.example.perfectpitchcoach.database.model.SubMasterClass

/**
 * The Room Magic is in this file, where you map a Java method call to an SQL query.
 *
 * When you are using complex data types, such as Date, you have to also supply type converters.
 * To keep this example basic, no types that require type converters are used.
 * See the documentation at
 * https://developer.android.com/topic/libraries/architecture/room.html#type-converters
 */

@Dao
interface MasterClassDao {

    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @Query("SELECT * from masterClass ORDER BY name ASC")
    fun getAllMasterClasses(): LiveData<List<MasterClass>>

    @Query("SELECT * from subMasterClass")
    fun getAlLSubMasterClasses(): LiveData<List<SubMasterClass>>

    @Query("SELECT * from subMasterClass WHERE  master_class_name =  :name   ")
    fun findSubMasterClassesById(name: String): LiveData<List<SubMasterClass>>

    // We do not need a conflict strategy, because the word is our primary key, and you cannot
    // add two items with the same primary key to the database. If the table has more than one
    // column, you can use @Insert(onConflict = OnConflictStrategy.REPLACE) to updateMasterClassWithUserId a row.
    @Insert
    fun insertMasterClasses(masterClass: MasterClass)

    @Insert
    fun insertSubMasterClasses(masterClass: SubMasterClass)


    @Query("UPDATE  masterClass set solved = :solved WHERE name = :masterClassName ")
    fun updateMasterClassWithOutUserId(masterClassName: String, solved: String)

    @Query("UPDATE  masterClass set solved = :solved WHERE id = :userId ")
    fun updateUser(userId: String, solved: String)

    @Query("UPDATE  subMasterClass set solved = :solved WHERE master_class_name = :masterClassName and name = :nameOfSubMasterClassPractice ")
    fun updateSubMasterClass(masterClassName: String, nameOfSubMasterClassPractice: String, solved: String)

    @Query("DELETE FROM masterClass")
    fun deleteAllMasterClassData()

    @Query("DELETE FROM subMasterClass")
    fun deleteAllSubMasterClassData()
}

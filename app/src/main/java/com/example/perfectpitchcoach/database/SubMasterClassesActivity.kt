package com.example.perfectpitchcoach.database

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.example.perfect_pitch_trainer.service.RetrofitGithubJsonFiles
import com.example.perfectpitchcoach.App
import com.example.perfectpitchcoach.R
import com.example.perfectpitchcoach.database.model.SubMasterClass
import com.example.perfectpitchcoach.database.viewModels.MasterClassViewModel
import com.example.perfectpitchcoach.model.ParsedJsonFiles
import kotlinx.android.synthetic.main.activity_sub_master_classes.*
import kotlinx.coroutines.*

class SubMasterClassesActivity : AppCompatActivity() {

    private lateinit var masterClassViewModel: MasterClassViewModel
    private lateinit var recyclerView: RecyclerView
    private val userSolvedMasterClass = 2
    private var masterClassName: String = ""
    private var fileName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_master_classes)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerview)

        val extras = getIntent().getExtras()
        if (extras != null) {
            masterClassName = extras.getString("masterClassName")
            fileName = extras.getString("file_name")
            println("User id is: " + fileName)
        }

        // Get a new or existing ViewModel from the ViewModelProvider.
        masterClassViewModel = ViewModelProviders.of(this@SubMasterClassesActivity).get(MasterClassViewModel::class.java)

        Log.i("aa", "velicina lokalne baze je: " + masterClassViewModel.allSubMasterClasses.value)
        Log.i("aa", "velicina lokalne baze je: " + masterClassViewModel.allSubMasterClasses)
    }

    override fun onStart() {
        super.onStart()

        GlobalScope.launch {

            var didWeParseThisFile: Boolean = false
            if (App.parsedJsonSubMasterClassesFiles.size > 0) {

                for (items in App.parsedJsonSubMasterClassesFiles) {

                    if (items.nameOfFile == fileName) {
                        didWeParseThisFile = true
                        break
                    }
                }

                if (didWeParseThisFile == false) {

                    val retrofitService = RetrofitGithubJsonFiles().getRetrofit()

                    val work2 = async { retrofitService.getSubMasterClassData(fileName) }
                    val result2 = work2.await().await()

                    if (result2 != null) {

                        masterClassViewModel.allSubMasterClasses.observe( this@SubMasterClassesActivity,  Observer { subMasterClasses ->

                                if (subMasterClasses?.size == 0) {
                                    for (webJsonItems in result2.body()?.list!!) {

                                        val subMasterClassUniqueId = masterClassName + "___" + webJsonItems.name

                                        var subMasterClass: SubMasterClass = SubMasterClass("", "", "")
                                        subMasterClass.masterClassName = masterClassName
                                        subMasterClass.sub_master_class_unique_id = subMasterClassUniqueId
                                        subMasterClass.name = webJsonItems.name
                                        subMasterClass.solved = "NO"
                                        subMasterClass.file_name = webJsonItems.filename
                                        masterClassViewModel.insertSubMasterClasses(subMasterClass)
                                    }
                                } else {
                                    // Update the cached copy of the words in the adapter.
                                    for (webJsonItems in result2.body()?.list!!) {

                                        var masterClassNotExists: Boolean = false

                                        val subMasterClassUniqueId = masterClassName + "___" + webJsonItems.name

                                        var subMasterClass: SubMasterClass = SubMasterClass("", "", "")
                                        if (subMasterClasses != null) {
                                            for (roomDatabaseItems in subMasterClasses) {

                                                if (subMasterClassUniqueId == roomDatabaseItems.sub_master_class_unique_id) {

                                                    masterClassNotExists = false
                                                    break

                                                } else {
                                                    masterClassNotExists = true
                                                    subMasterClass.masterClassName = masterClassName
                                                    subMasterClass.sub_master_class_unique_id = subMasterClassUniqueId
                                                    subMasterClass.name = webJsonItems.name
                                                    subMasterClass.file_name = webJsonItems.filename
                                                    subMasterClass.solved = "NO"
                                                }
                                            }
                                        }

                                        if (masterClassNotExists == true) {
                                            masterClassViewModel.insertSubMasterClasses(subMasterClass)
                                        }
                                    }
                                }
                            })

                        withContext(Dispatchers.Main) {

                            tvBackendRequestSend.text = "READING FROM GITHUB REPOSITORY, JSON FILE"
                            val parsedJsonFiles: ParsedJsonFiles = ParsedJsonFiles()
                            parsedJsonFiles.nameOfFile = fileName
                            parsedJsonFiles.isParsed = true
                            App.parsedJsonSubMasterClassesFiles.add(parsedJsonFiles)

                            val adapter = SubMasterClassAdapter(this@SubMasterClassesActivity, userSolvedMasterClass, masterClassName)
                            recyclerView.adapter = adapter
                            recyclerView.layoutManager = LinearLayoutManager(applicationContext)

                            masterClassViewModel.findSubMasterClassesById(masterClassName)
                                .observe(this@SubMasterClassesActivity, Observer { subMasterClasses ->
                                    // Update the cached copy of the words in the adapter.
/*
                                    for( backendData in App.backendSubMasterClassData ) {
                                        if (subMasterClasses != null) {
                                            for (roomDatabaseItems in subMasterClasses) {

                                                if (backendData.name == roomDatabaseItems.name) {

                                                    masterClassViewModel.updateSubMasterClass(
                                                        backendData.masterClassName.toString(), backendData.name.toString(), "YES")
                                                    break
                                                }
                                            }
                                        }
                                    }*/
                                    subMasterClasses?.let { adapter.setWords(it) }

                                    onContentChanged()
                                })
                        }
                    }
                }
                // citamo s lokalne baze podataka
                else {
                    withContext(Dispatchers.Main) {
                        tvBackendRequestSend.text = "READING FROM LOCAL DATABASE"

                        val adapter = SubMasterClassAdapter(this@SubMasterClassesActivity, userSolvedMasterClass, masterClassName)
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(applicationContext)

                        masterClassViewModel.findSubMasterClassesById(masterClassName)
                            .observe(this@SubMasterClassesActivity, Observer { words ->
                                // Update the cached copy of the words in the adapter.
                                words?.let { adapter.setWords(it) }
                            })
                    }
                }
            } else {
                val retrofitService = RetrofitGithubJsonFiles().getRetrofit()

                val work2 = async { retrofitService.getSubMasterClassData(fileName) }

                val result2 = work2.await().await()

                if (result2 != null) {

                    masterClassViewModel.allSubMasterClasses.observe( this@SubMasterClassesActivity, Observer { subMasterClasses ->

                            if (subMasterClasses?.size == 0) {
                                for (webJsonItems in result2.body()?.list!!) {

                                    val subMasterClassUniqueId = masterClassName + "___" + webJsonItems.name

                                    var subMasterClass: SubMasterClass = SubMasterClass("", "", "")
                                    subMasterClass.masterClassName = masterClassName
                                    subMasterClass.sub_master_class_unique_id = subMasterClassUniqueId
                                    subMasterClass.name = webJsonItems.name
                                    subMasterClass.solved = "NO"
                                    subMasterClass.file_name = webJsonItems.filename
                                    masterClassViewModel.insertSubMasterClasses(subMasterClass)
                                }
                            } else {
                                // Update the cached copy of the words in the adapter.
                                for (webJsonItems in result2.body()?.list!!) {

                                    var masterClassNotExists: Boolean = false

                                    val subMasterClassUniqueId = masterClassName + "___" + webJsonItems.name

                                    var subMasterClass: SubMasterClass = SubMasterClass("", "", "")
                                    if (subMasterClasses != null) {
                                        for (roomDatabaseItems in subMasterClasses) {

                                            if ( //webJsonItems.name == roomDatabaseItems.name && webJsonItems.
                                                    subMasterClassUniqueId == roomDatabaseItems.sub_master_class_unique_id) {

                                                masterClassNotExists = false
                                                break

                                            } else {
                                                masterClassNotExists = true
                                                subMasterClass.masterClassName = masterClassName
                                                subMasterClass.sub_master_class_unique_id = subMasterClassUniqueId
                                                subMasterClass.name = webJsonItems.name
                                                subMasterClass.file_name = webJsonItems.filename
                                                subMasterClass.solved = "NO"
                                            }
                                        }
                                    }

                                    if (masterClassNotExists == true) {
                                        masterClassViewModel.insertSubMasterClasses(subMasterClass)
                                    }
                                }
                            }
                        })

                    withContext(Dispatchers.Main) {

                        tvBackendRequestSend.text = "READING FROM GITHUB REPOSITORY, JSON FILE FIRST TIME"
                        val adapter = SubMasterClassAdapter(this@SubMasterClassesActivity, userSolvedMasterClass, masterClassName)
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(applicationContext)

                        masterClassViewModel.findSubMasterClassesById(masterClassName)
                            .observe(this@SubMasterClassesActivity, Observer { subMasterClasses ->
                                // Update the cached copy of the words in the adapter.

                            /*    for( backendData in App.backendSubMasterClassData ) {
                                    if (subMasterClasses != null) {
                                        for (roomDatabaseItems in subMasterClasses) {

                                            if (backendData.name == roomDatabaseItems.name) {

                                                masterClassViewModel.updateSubMasterClass(
                                                    backendData.masterClassName.toString(), backendData.name.toString(), "YES")
                                                break
                                            }
                                        }
                                    }
                                }*/
                                subMasterClasses?.let { adapter.setWords(it) }

                                onContentChanged()
                            })

                        // Add an observer on the LiveData returned by getAllMasterClasses.
                        // The onChanged() method fires when the observed data changes and the activity is
                        // in the foreground.
                       /* masterClassViewModel.allSubMasterClasses.observe(
                            this@SubMasterClassesActivity,
                            Observer { words ->
                                // Update the cached copy of the words in the adapter.
                                words?.let { adapter.setWords(it) }
                            })*/

                        val parsedJsonFiles: ParsedJsonFiles = ParsedJsonFiles()
                        parsedJsonFiles.nameOfFile = fileName
                        parsedJsonFiles.isParsed = true
                        App.parsedJsonSubMasterClassesFiles.add(parsedJsonFiles)
                    }
                }
            }
        }
    }
}

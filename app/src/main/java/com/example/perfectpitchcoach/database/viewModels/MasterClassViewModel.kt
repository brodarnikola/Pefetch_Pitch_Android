package com.example.perfectpitchcoach.database.viewModels


import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.example.perfectpitchcoach.database.WordRoomDatabase
import com.example.perfectpitchcoach.database.model.MasterClass
import com.example.perfectpitchcoach.database.model.SubMasterClass
import com.example.perfectpitchcoach.database.repository.MasterClassRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * View Model to keep a reference to the word repository and
 * an up-to-date list of all words.
 */

class MasterClassViewModel(application: Application) : AndroidViewModel(application) {

    private var parentJob = Job()
    // By default all the coroutines launched in this scope should be using the Main dispatcher
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    val repository: MasterClassRepository
    // Using LiveData and caching what getAllMasterClasses returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only updateMasterClassWithUserId the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allMasterClasses: LiveData<List<MasterClass>>
    val allSubMasterClasses: LiveData<List<SubMasterClass>>

    init {
        val masterClassDao = WordRoomDatabase.getDatabase(
            application,
            scope
        ).masterClassDao()
        repository = MasterClassRepository(masterClassDao)
        allMasterClasses = repository.allMasterClasses
        allSubMasterClasses = repository.allSubMasterClasses
    }

    fun getAllMasterClasses() = scope.launch(Dispatchers.IO) {
        repository.getAllMasterClasses()
    }

    /**
     * Launching a new coroutine to insertMasterClasses the data in a non-blocking way
     */
    fun insertMasterClasses(masterClass: MasterClass) = scope.launch(Dispatchers.IO) {
        repository.insertMasterClasses(masterClass)
    }

    fun insertSubMasterClasses(masterClass: SubMasterClass) = scope.launch(Dispatchers.IO) {
        repository.insertSubMasterClasses(masterClass)
    }

    suspend fun findSubMasterClassesById( name: String ) : LiveData<List<SubMasterClass>>  {
        return repository.findSubMasterClassesById(name)
    }

    // novi nacin
    /*suspend fun findSubMasterClassesById(name: String) : LiveData<List<SubMasterClass>> {
       return repository.findSubMasterClassesById(name)
    }*/

   /* fun getPlayer(id: Int): LiveData<Char> {
        return cRepository.getPlayer(id)
    }*/

   /*  public LiveData<List<FormSet>> getAllFormSets(setId){
        return allFormSets = formDatabase.formSetDao().getAllFilledForms(setId);
    } */

    fun updateMasterClassWithOutUserId(masterClassName: String, solved: String) = scope.launch(Dispatchers.IO) {
        repository.updateMasterClassWithOutUserId(masterClassName, solved)
    }

    fun updateMasterClassWithUserId(userId: String, solved: String) = scope.launch(Dispatchers.IO) {
        repository.update(userId, solved)
    }

    fun updateSubMasterClass(masterClassName: String, nameOfSubMasterClassPractice: String, solved: String) = scope.launch(Dispatchers.IO) {
        repository.updateSubMasterClass(masterClassName, nameOfSubMasterClassPractice, solved)
    }

    fun deleteAllMasterClassData() = scope.launch(Dispatchers.IO) {
        repository.deleteAllMasterClassData()
    }

    fun deleteAllSubMasterClassData() = scope.launch(Dispatchers.IO) {
        repository.deleteAllSubMasterClassData()
    }


    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}

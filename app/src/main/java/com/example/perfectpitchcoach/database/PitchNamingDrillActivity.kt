package com.example.perfectpitchcoach.database


import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.perfect_pitch_trainer.service.RetrofitGithubJsonFiles
import com.example.perfectpitchcoach.App
import com.example.perfectpitchcoach.R
import com.example.perfectpitchcoach.audio.MidiPlayer
import com.example.perfectpitchcoach.audio.PitchParser
import com.example.perfectpitchcoach.database.viewModels.MasterClassViewModel
import kotlinx.android.synthetic.main.activity_pitch_naming_drill.*
import kotlinx.coroutines.*
import android.support.v4.content.ContextCompat
import android.view.Window
import android.widget.Button
import com.example.perfectpitchcoach.BackendRequests
import com.example.perfectpitchcoach.model.PratcticeBatch
import kotlin.random.Random


class PitchNamingDrillActivity : AppCompatActivity() {

    private lateinit var masterClassViewModel: MasterClassViewModel
    private val userSolvedMasterClass = 2
    private var subMasterClassName: String = ""
    private var masterClassName: String = ""
    private var jsonFilenameWithQuestions: String = ""
    private var subMasterClassUniqueId: String = ""

    private var sizeOfCorrectAnswers: List<PratcticeBatch> = listOf()
    private var currentAnswer = 1;
    private var previousUserSelectedTon = ""
    private var currentUserSelectedTon = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pitch_naming_drill)

        val extras = getIntent().getExtras()
        if (extras != null) {
            subMasterClassUniqueId = extras.getString("sub_master_class_unique_id")
            masterClassName = extras.getString("masterClassName")
            subMasterClassName = extras.getString("subMasterClassName")
            jsonFilenameWithQuestions = extras.getString("JsonFilenameWithQuestions")
            println("json file_name with questions, practice bacth: " + jsonFilenameWithQuestions)
        }

        // Get a new or existing ViewModel from the ViewModelProvider.
        masterClassViewModel =
            ViewModelProviders.of(this@PitchNamingDrillActivity).get(MasterClassViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        progressBarPracticeBatch.visibility = View.VISIBLE
        tvPleaseWaitPracticeBatch.visibility = View.VISIBLE

        GlobalScope.launch {

            if (App.parsedPracticeBatchHashMap.size > 0) {

                var isJsonFileParsed = false
                for ((key, value) in App.parsedPracticeBatchHashMap) {
                    println("$key = $value")

                    if (key == subMasterClassUniqueId) {
                        // imamo vec sparsani taj json file
                        isJsonFileParsed = true
                        break
                    }
                }

                // nismo sparsali taj json file, citamo s githuba taj json file
                if (isJsonFileParsed == false) {

                    val retrofitService = RetrofitGithubJsonFiles().getRetrofit()

                    val work2 = async { retrofitService.getPracticesQuestion(jsonFilenameWithQuestions) }
                    val result2 = work2.await().await()

                    if (result2 != null && result2.body() != null) {

                        App.practiceBatch = result2.body()!!
                        while( App.practiceBatch.practiceBatch.size < App.practiceBatch.maxHits!! ) {

                            val randomNumberOrIndex = Random.nextInt(0, App.practiceBatch.practiceBatch.size)
                            val newItem = App.practiceBatch.practiceBatch.get(randomNumberOrIndex)
                            App.practiceBatch.practiceBatch.add(newItem)
                        }

                        App.practiceBatch.practiceBatch.shuffle()

                        withContext(Dispatchers.Main) {

                            App.parsedPracticeBatchHashMap.put(subMasterClassUniqueId, App.practiceBatch)

                            progressBarPracticeBatch.visibility = View.GONE
                            tvPleaseWaitPracticeBatch.visibility = View.GONE

                            tvMasterClassName.text = masterClassName
                            tvSubMasterClassName.text = subMasterClassName
                            tvPracticeDescription.text = App.practiceBatch.description
                            tvNumberOfQuestion.text = "Number of question: 1/" + App.practiceBatch.maxHits
                            tvWrongOrCorrectAnswer.setTextColor( ContextCompat.getColor(this@PitchNamingDrillActivity.baseContext, R.color.colorAccent) )
                            tvWrongOrCorrectAnswer.text = ""
                            tvResponseData.text = "Data is received from json file, from github"
                        }
                    }
                }
                // citamo s lokalne memorije taj json file
                else {
                    if (App.parsedPracticeBatchHashMap[subMasterClassUniqueId] != null) {

                        App.practiceBatch = App.parsedPracticeBatchHashMap[subMasterClassUniqueId]!!
                        App.practiceBatch.practiceBatch.shuffle()

                        withContext(Dispatchers.Main) {

                            progressBarPracticeBatch.visibility = View.GONE
                            tvPleaseWaitPracticeBatch.visibility = View.GONE

                            tvMasterClassName.text = masterClassName
                            tvSubMasterClassName.text = subMasterClassName
                            tvPracticeDescription.text = App.practiceBatch.description
                            tvNumberOfQuestion.text = "Number of question:  1/" + App.practiceBatch.maxHits
                            tvWrongOrCorrectAnswer.setTextColor( ContextCompat.getColor(this@PitchNamingDrillActivity.baseContext, R.color.colorAccent) )
                            tvWrongOrCorrectAnswer.text = ""
                            tvResponseData.text = "Data is received from local memory"
                        }
                    }
                }
            } else {

                val retrofitService = RetrofitGithubJsonFiles().getRetrofit()

                val work2 = async { retrofitService.getPracticesQuestion(jsonFilenameWithQuestions) }
                val result2 = work2.await().await()

                if (result2 != null && result2.body() != null) {

                    App.practiceBatch = result2.body()!!
                    while(  App.practiceBatch.practiceBatch.size < App.practiceBatch.maxHits!!) {

                        val randomNumberOrIndex = Random.nextInt(0, App.practiceBatch.practiceBatch.size)
                        val newItem = App.practiceBatch.practiceBatch.get(randomNumberOrIndex)
                        App.practiceBatch.practiceBatch.add(newItem)
                    }
                    App.practiceBatch.practiceBatch.shuffle()

                    withContext(Dispatchers.Main) {

                        App.parsedPracticeBatchHashMap.put(subMasterClassUniqueId, App.practiceBatch)

                        progressBarPracticeBatch.visibility = View.GONE
                        tvPleaseWaitPracticeBatch.visibility = View.GONE

                        tvMasterClassName.text = masterClassName
                        tvSubMasterClassName.text = subMasterClassName
                        tvPracticeDescription.text = App.practiceBatch.description
                        tvNumberOfQuestion.text = "Number of question: 1/" + App.practiceBatch.maxHits
                        tvResponseData.text = "Data is received from json file, from github"
                        tvWrongOrCorrectAnswer.setTextColor( ContextCompat.getColor(this@PitchNamingDrillActivity.baseContext, R.color.colorAccent) )
                        tvWrongOrCorrectAnswer.text = ""
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        tonAccordsClickListener()

        btnPlay.setOnClickListener {

            Toast.makeText(this@PitchNamingDrillActivity, "counter je: " + currentAnswer, Toast.LENGTH_SHORT).show()

            val practiceBatch = App.practiceBatch.practiceBatch

            val listOfQuestions = practiceBatch.get(currentAnswer-1).question


            val listOfCorrectQuestions = listOfQuestions.get(0)

            var aAccordTonsToplay: MutableList<String> = mutableListOf()

            for (question in listOfCorrectQuestions) {
                aAccordTonsToplay.add(question.toString().toLowerCase())
            }

            for (question in 0..listOfQuestions.size) {
                if (question == listOfQuestions.size - 1) {
                    for (answer in listOfQuestions[question])
                        aAccordTonsToplay.add(answer)
                }
            }

            if (PitchParser.isPitchListValid(aAccordTonsToplay)) {
                MidiPlayer.playMultipleNotesMelodicly(aAccordTonsToplay)
            }
        }

        btnAnswer.setOnClickListener {

            if (currentUserSelectedTon == "fis'") {
                Toast.makeText(this@PitchNamingDrillActivity, "selektiran je fis' ton", Toast.LENGTH_SHORT).show()
            } else if (currentUserSelectedTon == "g'") {
                Toast.makeText(this@PitchNamingDrillActivity, "selektiran je g' ton", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@PitchNamingDrillActivity, "selektiran je ton:  " + currentUserSelectedTon + "   " , Toast.LENGTH_SHORT).show()
            }

            val practiceBatch = App.practiceBatch.practiceBatch

            val listOfQuestions = practiceBatch.get(currentAnswer-1).question

            val listOfCorrectQuestions = listOfQuestions.get(1)

            if (listOfCorrectQuestions[0] == currentUserSelectedTon) {

                App.practiceBatch.practiceBatch[currentAnswer - 1].answeredCorrect = "YES"
                if( currentAnswer != App.practiceBatch.maxHits ) {
                    currentAnswer++
                    tvNumberOfQuestion.text = "Number of question: " + currentAnswer +
                            "/" + App.practiceBatch.maxHits
                    tvWrongOrCorrectAnswer.text = ""
                }
                else if( currentAnswer == App.practiceBatch.maxHits ) {
                    tvWrongOrCorrectAnswer.setTextColor(
                        ContextCompat.getColor(
                            this@PitchNamingDrillActivity.baseContext,
                            R.color.colorPrimary
                        )
                    )
                    tvWrongOrCorrectAnswer.text = "Correct answer"
                }
                sizeOfCorrectAnswers = App.practiceBatch.practiceBatch.filter { it.answeredCorrect == "YES" }
                if (sizeOfCorrectAnswers.size == App.practiceBatch.maxHits) {

                    btnAllQuestionAnswered.visibility = View.VISIBLE

                    showDialog()

                    BackendRequests.userSolvedSubMasterClass(subMasterClassName, masterClassName,  jsonFilenameWithQuestions, this@PitchNamingDrillActivity.applicationContext)

                    masterClassViewModel.updateSubMasterClass(masterClassName, subMasterClassName, "YES")
                }
                // onda se correctCounterAnswer poveca za 1 i ide se na drugi viewPager screen
            }
            else {
                tvWrongOrCorrectAnswer.setTextColor( ContextCompat.getColor(this@PitchNamingDrillActivity.baseContext, R.color.colorAccent) )
                tvWrongOrCorrectAnswer.text = "Wrong answer"
                App.practiceBatch.practiceBatch[currentAnswer - 1].answeredCorrect = "NO"
            }
        }

        btnAllQuestionAnswered.setOnClickListener {

            masterClassViewModel.updateSubMasterClass(masterClassName, subMasterClassName, "YES")
        }

        btnPrevious.setOnClickListener{

            if( currentAnswer > 1 ) {
                currentAnswer--
                tvNumberOfQuestion.text = "Number of question: " + currentAnswer + "/" + App.practiceBatch.maxHits

                if(  App.practiceBatch.practiceBatch[currentAnswer - 1].answeredCorrect == "" ) {
                    tvWrongOrCorrectAnswer.text = ""
                }
                else if( App.practiceBatch.practiceBatch[currentAnswer - 1].answeredCorrect == "YES" ) {
                    tvWrongOrCorrectAnswer.setTextColor( ContextCompat.getColor(this@PitchNamingDrillActivity.baseContext, R.color.colorPrimary) )
                    tvWrongOrCorrectAnswer.text = "Correct answer"
                }
                else {
                    tvWrongOrCorrectAnswer.setTextColor( ContextCompat.getColor(this@PitchNamingDrillActivity.baseContext, R.color.colorAccent) )
                    tvWrongOrCorrectAnswer.text = "Wrong answer"
                }
            }
        }

        btnNext.setOnClickListener {
            if( currentAnswer < App.practiceBatch.maxHits!!) {
                currentAnswer++
                tvNumberOfQuestion.text = "Number of question: " + currentAnswer + "/" + App.practiceBatch.maxHits

                if(  App.practiceBatch.practiceBatch[currentAnswer - 1].answeredCorrect == "" ) {
                    tvWrongOrCorrectAnswer.text = ""
                }
                else  if( App.practiceBatch.practiceBatch[currentAnswer - 1].answeredCorrect == "YES" ) {
                    tvWrongOrCorrectAnswer.setTextColor( ContextCompat.getColor(this@PitchNamingDrillActivity.baseContext, R.color.colorPrimary) )
                    tvWrongOrCorrectAnswer.text = "Correct answer"
                }
                else {
                    tvWrongOrCorrectAnswer.setTextColor( ContextCompat.getColor(this@PitchNamingDrillActivity.baseContext, R.color.colorAccent) )
                    tvWrongOrCorrectAnswer.text = "Wrong answer"
                }
            }
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this@PitchNamingDrillActivity)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog .setCancelable(false)
        dialog .setContentView(R.layout.success_submasterclass_dialog)
        val okBtn = dialog.findViewById(R.id.btnSolved) as Button
        okBtn.setOnClickListener {
            dialog .dismiss()
        }
        dialog .show()
    }

    private fun tonAccordsClickListener() {

        llFis.setOnClickListener {

            findPreviousUserSelectedTon(it)
            previousUserSelectedTon = "fis'"

            currentUserSelectedTon = "fis'"
            llFis.setBackground(ContextCompat.getDrawable(it.context, R.drawable.red_border_black_background_layout));

            /* val border = GradientDrawable()
             border.setColor(ContextCompat.getColor(it.context, android.R.color.black)) //white background
             border.setStroke(2, ContextCompat.getColor(it.context, R.color.colorAccent)) //black border with full opacity
             if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                 llFis.setBackgroundDrawable(border)
             } else {
                 llFis.setBackground(border)
             }*/
        }

        llG.setOnClickListener {

            findPreviousUserSelectedTon(it)
            previousUserSelectedTon = "g'"

            currentUserSelectedTon = "g'"
            llG.setBackground(ContextCompat.getDrawable(it.context, R.drawable.red_border_white_background_layout));
        }

        llGis.setOnClickListener {

            findPreviousUserSelectedTon(it)
            previousUserSelectedTon = "as'"

            currentUserSelectedTon = "as'"
            llGis.setBackground(ContextCompat.getDrawable(it.context, R.drawable.red_border_black_background_layout));
        }

        llA.setOnClickListener {

            findPreviousUserSelectedTon(it)
            previousUserSelectedTon = "a'"

            currentUserSelectedTon = "a'"
            llA.setBackground(ContextCompat.getDrawable(it.context, R.drawable.red_border_white_background_layout));
        }

        llAis.setOnClickListener {

            findPreviousUserSelectedTon(it)
            previousUserSelectedTon = "b'"

            currentUserSelectedTon = "b'"
            llAis.setBackground(ContextCompat.getDrawable(it.context, R.drawable.red_border_black_background_layout));
        }

        llH.setOnClickListener {

            findPreviousUserSelectedTon(it)
            previousUserSelectedTon = "h'"

            currentUserSelectedTon = "h'"
            llH.setBackground(ContextCompat.getDrawable(it.context, R.drawable.red_border_white_background_layout));
        }

        llC.setOnClickListener {

            findPreviousUserSelectedTon(it)
            previousUserSelectedTon = "c'"

            currentUserSelectedTon = "c'"
            llC.setBackground(ContextCompat.getDrawable(it.context, R.drawable.red_border_white_background_layout));
        }

        llCis.setOnClickListener {

            findPreviousUserSelectedTon(it)
            previousUserSelectedTon = "cis'"

            currentUserSelectedTon = "cis'"
            llCis.setBackground(ContextCompat.getDrawable(it.context, R.drawable.red_border_black_background_layout));
        }

        llD.setOnClickListener {

            findPreviousUserSelectedTon(it)
            previousUserSelectedTon = "d'"

            currentUserSelectedTon = "d'"
            llD.setBackground(ContextCompat.getDrawable(it.context, R.drawable.red_border_white_background_layout));
        }

        llDis.setOnClickListener {

            findPreviousUserSelectedTon(it)
            previousUserSelectedTon = "es'"

            currentUserSelectedTon = "es'"
            llDis.setBackground(ContextCompat.getDrawable(it.context, R.drawable.red_border_black_background_layout));
        }

        llE.setOnClickListener {

            findPreviousUserSelectedTon(it)
            previousUserSelectedTon = "e'"

            currentUserSelectedTon = "e'"
            llE.setBackground(ContextCompat.getDrawable(it.context, R.drawable.red_border_white_background_layout));
        }

        llF.setOnClickListener {

            findPreviousUserSelectedTon(it)
            previousUserSelectedTon = "f'"

            currentUserSelectedTon = "f'"
            llF.setBackground(ContextCompat.getDrawable(it.context, R.drawable.red_border_white_background_layout));
        }
    }

    private fun findPreviousUserSelectedTon(it: View) {

        if (previousUserSelectedTon == "fis'")
            llFis.setBackground(ContextCompat.getDrawable(it.context, R.drawable.black_background));
        else if (previousUserSelectedTon == "g'")
            llG.setBackground(ContextCompat.getDrawable(it.context, R.drawable.white_background));
        else if (previousUserSelectedTon == "as'")
            llGis.setBackground(ContextCompat.getDrawable(it.context, R.drawable.black_background));
        else if (previousUserSelectedTon == "a'")
            llA.setBackground(ContextCompat.getDrawable(it.context, R.drawable.white_background));
        else if (previousUserSelectedTon == "b'")
            llAis.setBackground(ContextCompat.getDrawable(it.context, R.drawable.black_background));
        else if (previousUserSelectedTon == "h'")
            llH.setBackground(ContextCompat.getDrawable(it.context, R.drawable.white_background));

        else if (previousUserSelectedTon == "c'")
            llC.setBackground(ContextCompat.getDrawable(it.context, R.drawable.white_background));
        else if (previousUserSelectedTon == "cis'")
            llCis.setBackground(ContextCompat.getDrawable(it.context, R.drawable.black_background));
        else if (previousUserSelectedTon == "d'")
            llD.setBackground(ContextCompat.getDrawable(it.context, R.drawable.white_background));
        else if (previousUserSelectedTon == "es'")
            llDis.setBackground(ContextCompat.getDrawable(it.context, R.drawable.black_background));
        else if (previousUserSelectedTon == "e'")
            llE.setBackground(ContextCompat.getDrawable(it.context, R.drawable.white_background));
        else if (previousUserSelectedTon == "f'")
            llF.setBackground(ContextCompat.getDrawable(it.context, R.drawable.white_background));

    }

    override fun onStop() {
        super.onStop()
        App.practiceBatch.practiceBatch.find { it.answeredCorrect == "YES" || it.answeredCorrect == "NO"  }?.answeredCorrect = ""
        sizeOfCorrectAnswers.find { it.answeredCorrect == "YES" || it.answeredCorrect == "NO"   }?.answeredCorrect = ""
    }

    override fun onBackPressed() {
        super.onBackPressed()
        App.practiceBatch.practiceBatch.find { it.answeredCorrect == "YES" || it.answeredCorrect == "NO"  }?.answeredCorrect = ""
        sizeOfCorrectAnswers.find { it.answeredCorrect == "YES" || it.answeredCorrect == "NO"  }?.answeredCorrect = ""
    }

}

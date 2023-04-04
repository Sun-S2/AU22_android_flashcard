package com.example.au22_flashcard

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var wordView : TextView
    //private lateinit var adapter: NotesRVAdapter

    //private val wordDatabase by lazy { AppDatabase.getDatabase(this).wordDao() }
    var currentWord : Word? = null
    val wordListMain = WordListMain()
    //lateinit var db : AppDatabase

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        window.decorView.windowInsetsController!!.hide(
            android.view.WindowInsets.Type.statusBars()
        )
        setContentView(R.layout.activity_main)

        //db = AppDatabase.getInstance(this)
        //db = AppDatabase.getDatabase(this)

        wordView = findViewById(R.id.wordTextView)

        showNewWord()

        wordView.setOnClickListener {
            revealTranslation()
        }

        val startDictionaryButton = findViewById<Button>(R.id.start_activity_dic)
        startDictionaryButton.setOnClickListener {
            // Moves to the next window
            //setContentView(R.layout.activity_dictionary)

            val intent = Intent(this, Dictionary::class.java)
            startActivity(intent)
        }
        startDictionaryButton.performClick()
    }



    //Working code, paused in favour for
    // window.decorView.windowInsetsController!!.hide( ... )
    //above


   /* private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        var mainContainer = layoutInflater.inflate(R.layout.activity_main, null, false)
        WindowInsetsControllerCompat(window, mainContainer).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
    private fun showSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window,true)
        var mainContainer = layoutInflater.inflate(R.layout.activity_main, null, false)
        WindowInsetsControllerCompat(window, mainContainer).show(WindowInsetsCompat.Type.systemBars())
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }*/



    fun revealTranslation() {
        wordView.text = currentWord?.english
    }

    fun showNewWord() {

        currentWord = wordListMain.getNewWord()
        wordView.text = currentWord?.swedish
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event?.action == MotionEvent.ACTION_UP) {
            showNewWord()
        }

        return true
    }
}

//Vad ska göras:

//1. skapa en ny aktivitet där ett nytt ord får skrivas in
//2. spara det nya ordet i databasen.
//3. I main activity läs in alla ord från databasen
// (anväd coroutiner när ni läser och skriver till databasen se tidigare exempel)


//Kod
//Se Dictionary klassen och de andra som behövs som anrops därifrån.








package com.example.au22_flashcard
//Credits to https://github.com/johncodeos-blog/RoomExample used as guide for this project room databas uppgift
//Other helpful links for ideas
// https://github.com/Cleveroad/AdaptiveTableLayout
//https://github.com/android/sunflower/tree/main


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import java.util.*

class Dictionary : AppCompatActivity() {
    private lateinit var adapter: WordsRVAdapter
    private val wordDatabase by lazy { AppDatabase.getDatabase(this).wordDao() }
    private var wordId : Int  = 0


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.windowInsetsController!!.hide(
            WindowInsets.Type.statusBars()
        )

        supportActionBar?.hide()
        setContentView(R.layout.activity_dictionary)
        setRecyclerView()
        observeWords()
    }


    private fun setRecyclerView() {
        val wordsRecyclerview = findViewById<RecyclerView>(R.id.words_recyclerview)
        wordsRecyclerview.layoutManager = LinearLayoutManager(this)
        wordsRecyclerview.setHasFixedSize(true)
        adapter = WordsRVAdapter()
        Log.d("RecVw00", "0 " + adapter)
                //Log.v("0007 ", "" + adapter)
        adapter.setItemListener(object : RecyclerClickListener {

            // Tap the 'X' to delete the word.
            override fun onItemRemoveClick(position: Int) {
                val wordsList = adapter.currentList.toMutableList()
                val wordTextEng = wordsList[position].english
                val wordTextSwe = wordsList[position].swedish
                val wordDateAdded = wordsList[position].dateAdded
                val wordId = wordsList[position].id
                val removeWord = Word(wordId,wordDateAdded, wordTextEng, wordTextSwe)
                wordsList.removeAt(position)
                adapter.submitList(wordsList)
                lifecycleScope.launch {
                    wordDatabase.deleteWord(removeWord)
                }
            }

            // Tap the word to edit.
            override fun onItemClick(position: Int) {
                val intent = Intent(this@Dictionary, AddWordActivity::class.java)
                val wordsList = adapter.currentList.toMutableList()
                intent.putExtra("word_date_added", wordsList[position].dateAdded)
                intent.putExtra("word_text_eng", wordsList[position].english)
                intent.putExtra("word_text_swe", wordsList[position].swedish)
                //Unnecessary sinde wordId is capturing the focus
                intent.putExtra("word_text_id", wordsList[position].id)
                wordId = wordsList[position].id
                editWordResultLauncher.launch(intent)
            }
        })
        wordsRecyclerview.adapter = adapter
    }

    private fun observeWords() {
        lifecycleScope.launch {
            wordDatabase.getWords().collect { wordsList ->
                if (wordsList.isNotEmpty()) {
                    adapter.submitList(wordsList)
                }
            }
        }
    }

    private val newWordResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Get the new word from the Dictionary activity
                val wordDateAdded = Date()
                val wordTextEng = result.data?.getStringExtra("word_text_eng")
                val wordTextSwe = result.data?.getStringExtra("word_text_swe")
                // Add the new word at the top of the list
                val newWord = Word(0,  wordDateAdded, wordTextEng ?: "" , wordTextSwe ?: "")
                lifecycleScope.launch {
                    wordDatabase.addWord(newWord)
                }
            }
        }

    val editWordResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Get the edited word from the AddWordActivity
                val wordDateAdded = result.data?.getSerializableExtra("word_date_added") as Date
                val wordTextEng = result.data?.getStringExtra("word_text_eng")
                val wordTextSwe = result.data?.getStringExtra("word_text_swe")
                // Update the word in the list
                //wordId as temporary solution. Safest if capture from the result.data item id when exists.
                val editedWord = Word(wordId, wordDateAdded, wordTextEng ?: "", wordTextSwe?: "")
                lifecycleScope.launch {
                    wordDatabase.updateWord(editedWord)
                }
            }
        }


    @SuppressLint("ClickableViewAccessibility", "InflateParams")
    fun showPopup(v: View) {
        // inflate the layout of the popup window
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_item, null)
        // create the popup window
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        //popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0)
        popupWindow.showAtLocation(v, Gravity.TOP, 400, 150)

        // dismiss the popup window when touched
        popupView.setOnTouchListener { v, event ->
            popupWindow.dismiss()
            val intent = Intent(this, AddWordActivity::class.java)
            newWordResultLauncher.launch(intent)
            true
        }
    }
}
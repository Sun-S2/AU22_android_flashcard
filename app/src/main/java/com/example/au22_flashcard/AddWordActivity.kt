package com.example.au22_flashcard
//Credits to https://github.com/johncodeos-blog/RoomExample used as guide for this project room databas uppgift
//Other helpful links for ideas
// https://github.com/Cleveroad/AdaptiveTableLayout
//https://github.com/android/sunflower/tree/main


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
//import androidx.vectordrawable.graphics.drawable.ArgbEvaluator
import android.animation.ArgbEvaluator
import android.util.Log
import java.util.*

class AddWordActivity : AppCompatActivity() {
    private lateinit var addWordBackground: RelativeLayout
    private lateinit var addWordWindowBg: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_word)

        addWordBackground = findViewById(R.id.add_word_background)
        addWordWindowBg = findViewById(R.id.add_word_window_bg)

        setActivityStyle()

        val wordDateAdded = intent.getSerializableExtra("word_date_added") as? Date
        val wordTextToEditEng = intent.getStringExtra("word_text_eng")
        val wordTextToEditSwe = intent.getStringExtra("word_text_swe")

        val addWordTextEng = findViewById<TextView>(R.id.add_word_text_eng)
        addWordTextEng.text = wordTextToEditEng ?: ""

        val addWordTextSwe = findViewById<TextView>(R.id.add_word_text_swe)
        addWordTextSwe.text = wordTextToEditSwe ?: ""

        val addWordButton = findViewById<Button>(R.id.add_word_button)
        addWordButton.setOnClickListener {
            // Return Word text to the Dictionary
            val data = Intent()
            data.putExtra("word_date_added", wordDateAdded)
            data.putExtra("word_text_eng", addWordTextEng.text.toString())
            data.putExtra("word_text_swe", addWordTextSwe.text.toString())
            setResult(Activity.RESULT_OK, data)
            // Close current window
            onBackPressed()
        }
    }

    //@SuppressLint("RestrictedApi")
    private fun setActivityStyle() {
        // Make the background full screen, over status bar
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        this.window.statusBarColor = Color.TRANSPARENT
        val winParams = this.window.attributes
        winParams.flags =
            winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
        this.window.attributes = winParams

        // Fade animation for the background of Popup Window
        val alpha = 100 //between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), Color.TRANSPARENT, alphaColor)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            addWordBackground.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()

        addWordWindowBg.alpha = 0f
        addWordWindowBg.animate().alpha(1f).setDuration(500)
            .setInterpolator(DecelerateInterpolator()).start()

        // Close window when you tap on the dim background
        addWordBackground.setOnClickListener { onBackPressed() }
        addWordWindowBg.setOnClickListener { /* Prevent activity from closing when you tap on the popup's window background */ }
    }



    override fun onBackPressed() {
        // Fade animation for the background of Popup Window when you press the back button
        val alpha = 100 // between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), alphaColor, Color.TRANSPARENT)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            addWordBackground.setBackgroundColor(
                animator.animatedValue as Int
            )
        }

        // Fade animation for the Popup Window when you press the back button
        addWordWindowBg.animate().alpha(0f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()

        // After animation finish, close the Activity
        colorAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                finish()
                overridePendingTransition(0, 0)
            }
        })
        colorAnimation.start()
    }
}
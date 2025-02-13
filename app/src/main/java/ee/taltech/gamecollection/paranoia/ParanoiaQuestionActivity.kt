package ee.taltech.gamecollection.paranoia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ee.taltech.gamecollection.R

class ParanoiaQuestionActivity : AppCompatActivity() {
    private lateinit var question: TextView
    private lateinit var dbHelper: ParanoiaDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paranoia_question)

        question = findViewById(R.id.textViewQuestion)
        dbHelper = ParanoiaDbHelper(this)

        val isFirstLaunch = intent.getBooleanExtra("reset_questions", false)
        if (isFirstLaunch) {
            dbHelper.resetAskedQuestions()
        }
        getNewQuestion()

        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)

        val buttonCoinFlip: Button = findViewById(R.id.buttonCoinFlip)
        buttonCoinFlip.setOnClickListener {
            it.startAnimation(bounceAnimation)
            onClickCoinFlip()
        }

        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            it.startAnimation(bounceAnimation)
            val intent = Intent(this, ParanoiaActivity::class.java)
            startActivity(intent)
        }
    }

    fun getNewQuestion() {
        val newQuestion = dbHelper.getRandomUnaskedParanoiaQuestion()
        question.text = newQuestion ?: "No questions left, restart the game"
    }

    fun onClickCoinFlip() {
        intent = Intent(this, ParanoiaCoinFlipActivity::class.java)
        startActivity(intent)
    }
}
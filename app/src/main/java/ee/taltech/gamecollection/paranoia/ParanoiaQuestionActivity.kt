package ee.taltech.gamecollection.paranoia

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import ee.taltech.gamecollection.MainActivity
import ee.taltech.gamecollection.R

class ParanoiaQuestionActivity : AppCompatActivity() {

    private lateinit var question: TextView

    private lateinit var questions: MutableList<String>
    private var nextQuestionIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paranoia_question)

        question = findViewById(R.id.textViewQuestion)

        loadQuestions()
        getNewQuestion()

        val bounceAnimation =
            AnimationUtils.loadAnimation(this, R.anim.bounce)

        val buttonCoinFlip: Button =
            findViewById(R.id.buttonCoinFlip)

        buttonCoinFlip.setOnClickListener {
            it.startAnimation(bounceAnimation)
            onClickCoinFlip()
        }

        val buttonRules: Button =
            findViewById(R.id.buttonRules)

        buttonRules.setOnClickListener {
            it.startAnimation(bounceAnimation)

            val intent =
                Intent(this, ParanoiaRulesActivity::class.java)

            startActivity(intent)
        }

        val buttonBack: ImageButton =
            findViewById(R.id.buttonBack)

        buttonBack.setOnClickListener {
            val intent =
                Intent(this, MainActivity::class.java)

            startActivity(intent)
            finish()
        }
    }

    private fun loadQuestions() {
        questions = mutableListOf()

        val currentLanguage = AppCompatDelegate
            .getApplicationLocales()
            .get(0)
            ?.language

        val resourceId =
            if (currentLanguage == "et") {
                R.raw.paranoia_et
            } else {
                R.raw.paranoia_en
            }

        resources.openRawResource(resourceId)
            .bufferedReader()
            .useLines { lines ->
                for (line in lines) {
                    if (line.isNotBlank()) {
                        questions.add(line)
                    }
                }
            }

        questions.shuffle()
        nextQuestionIndex = 0
    }

    fun getNewQuestion() {
        if (questions.isEmpty()) {
            question.text = getString(R.string.no_questions_available)
            return
        }

        if (nextQuestionIndex >= questions.size) {
            questions.shuffle()
            nextQuestionIndex = 0
        }

        question.text = questions[nextQuestionIndex]
        nextQuestionIndex++
    }

    fun onClickCoinFlip() {
        val intent =
            Intent(this, ParanoiaCoinFlipActivity::class.java)

        startActivity(intent)
    }
}
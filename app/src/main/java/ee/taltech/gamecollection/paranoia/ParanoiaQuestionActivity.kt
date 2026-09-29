package ee.taltech.gamecollection.paranoia

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.edit
import androidx.core.os.ConfigurationCompat
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

        val buttonSettings: ImageButton = findViewById(R.id.buttonSettings)
        buttonSettings.setOnClickListener {
            showSettingsDialog()
        }

        val settings = loadSettings()

        loadQuestions(settings)
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

    private fun showSettingsDialog() {

        val dialog = Dialog(this)

        dialog.setContentView(R.layout.dialog_paranoia_settings)
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)

        val sexualSwitch: SwitchCompat = dialog.findViewById(R.id.sexualSwitch)
        val buttonCancel: Button = dialog.findViewById(R.id.buttonBack)

        val settings = loadSettings()

        sexualSwitch.isChecked = settings.sexual

        buttonCancel.setOnClickListener {
            saveSettings(
                ParanoiaSettings(
                    sexual = sexualSwitch.isChecked
                )
            )

            dialog.dismiss()
            recreate()
        }

        dialog.show()

        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.90).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun loadSettings(): ParanoiaSettings {
        val preferences = getSharedPreferences(
            "paranoia_settings",
            MODE_PRIVATE
        )

        return ParanoiaSettings(
            sexual = preferences.getBoolean("sexual", false)
        )
    }

    private fun saveSettings(settings: ParanoiaSettings) {
        getSharedPreferences(
            "paranoia_settings",
            MODE_PRIVATE
        )
            .edit {
                    putBoolean("sexual", settings.sexual)
            }
    }

    private fun loadQuestions(settings: ParanoiaSettings) {
        val language = ConfigurationCompat
            .getLocales(resources.configuration)
            .get(0)
            ?.language

        val questions = mutableListOf<String>()

        fun loadResource(resourceId: Int) {
            resources.openRawResource(resourceId)
                .bufferedReader()
                .useLines { lines ->
                    questions.addAll(
                        lines.filter { it.isNotBlank() }
                    )
                }
        }

        loadResource(
            if (language == "et") {
                R.raw.paranoia_et
            } else {
                R.raw.paranoia_en
            }
        )

        if (settings.sexual) {
            loadResource(
                if (language == "et") {
                    R.raw.paranoia_spicy_et
                } else {
                    R.raw.paranoia_spicy_en
                }
            )
        }

        questions.shuffle()

        this.questions = questions
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
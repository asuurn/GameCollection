package ee.taltech.gamecollection

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.edit
import androidx.core.os.LocaleListCompat
import ee.taltech.gamecollection.cardgames.CardGamesActivity
import ee.taltech.gamecollection.paranoia.ParanoiaQuestionActivity
import ee.taltech.gamecollection.scoreboard.ScoreboardActivity
import ee.taltech.gamecollection.truthOrDare.TruthOrDareActivity
import ee.taltech.gamecollection.truthOrDare.TruthOrDareSettings
import ee.taltech.gamecollection.twister.TwisterActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<ViewGroup>(R.id.main).scaleContentToFit(
            findViewById<View>(R.id.scaledContent)
        )

        val root = findViewById<android.view.View>(R.id.main)

        root.post {
            val density = resources.displayMetrics.density

            val widthDp =
                (root.width - root.paddingLeft - root.paddingRight) / density

            val heightDp =
                (root.height - root.paddingTop - root.paddingBottom) / density

            android.util.Log.d(
                "LayoutSize",
                "width=${widthDp}dp, height=${heightDp}dp"
            )
        }

        val buttonSettings: ImageButton = findViewById(R.id.buttonSettings)
        buttonSettings.setOnClickListener {
            showLanguageDialog()
        }

        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)

        val paranoiaButton: Button = findViewById(R.id.buttonToParanoia)
        paranoiaButton.setOnClickListener {
            it.startAnimation(bounceAnimation)
            val intent = Intent(this, ParanoiaQuestionActivity::class.java)
            startActivity(intent)
        }

        val buttonToBaila: Button = findViewById(R.id.buttonToScoreBoard)
        buttonToBaila.setOnClickListener {
            it.startAnimation(bounceAnimation)
            val intent = Intent(this, ScoreboardActivity::class.java)
            startActivity(intent)
        }

        val buttonToCardGames: Button = findViewById(R.id.buttonToCardGames)
        buttonToCardGames.setOnClickListener {
            it.startAnimation(bounceAnimation)
            val intent = Intent(this, CardGamesActivity::class.java)
            startActivity(intent)
        }

        val buttonToTwister: Button = findViewById(R.id.buttonToTwister)
        buttonToTwister.setOnClickListener {
            it.startAnimation(bounceAnimation)
            val intent = Intent(this, TwisterActivity::class.java)
            startActivity(intent)
        }

        val buttonToTruthOrDare: Button = findViewById(R.id.buttonToTruthOrDare)
        buttonToTruthOrDare.setOnClickListener {
            it.startAnimation(bounceAnimation)
            val intent = Intent(this, TruthOrDareActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLanguageDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_language_switch)

        dialog.window?.setBackgroundDrawableResource(
            R.drawable.dialog_background
        )

        val languageEnglish: RadioButton = dialog.findViewById(R.id.languageEnglish)
        val languageEstonian: RadioButton = dialog.findViewById(R.id.languageEstonian)
        val buttonBack: Button = dialog.findViewById(R.id.buttonBack)

        val currentLanguage = AppCompatDelegate.getApplicationLocales()
                .get(0)
                ?.language

        if (currentLanguage == "et") {
            languageEstonian.isChecked = true
        } else {
            languageEnglish.isChecked = true
        }

        buttonBack.setOnClickListener {
            val selectedLanguage =
                if (languageEstonian.isChecked) {
                    "et"
                } else {
                    "en"
                }

            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(selectedLanguage)
            )

            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.90).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }
}
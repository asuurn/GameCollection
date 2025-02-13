package ee.taltech.gamecollection.paranoia

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ee.taltech.gamecollection.R

class ParanoiaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paranoia)

        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        val buttonStart: Button = findViewById(R.id.buttonStart)
        buttonStart.setOnClickListener {
            it.startAnimation(bounceAnimation)
            intent = Intent(this, ParanoiaQuestionActivity::class.java)
            intent.putExtra("reset_questions", true)
            startActivity(intent)
        }

        val buttonRules: Button = findViewById(R.id.buttonRules)
        buttonRules.setOnClickListener {
            it.startAnimation(bounceAnimation)
            intent = Intent(this, ParanoiaRulesActivity::class.java)
            startActivity(intent)
        }

        val suggestButton = findViewById<AppCompatButton>(R.id.buttonSuggest)
        suggestButton.isEnabled = false
        suggestButton.isClickable = false
    }
}
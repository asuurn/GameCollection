package ee.taltech.gamecollection.cardgames

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ee.taltech.gamecollection.MainActivity
import ee.taltech.gamecollection.R
import ee.taltech.gamecollection.paranoia.ParanoiaQuestionActivity
import ee.taltech.gamecollection.paranoia.ParanoiaRulesActivity

class CroatianPickRules : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_croatian_pick_rules)

        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        val buttonBackToCroatianPickRules: Button = findViewById(R.id.buttonBackToCroatianPickRules)
        buttonBackToCroatianPickRules.setOnClickListener {
            it.startAnimation(bounceAnimation)
            intent = Intent(this, CroatianPick::class.java)
            startActivity(intent)
        }
    }
}
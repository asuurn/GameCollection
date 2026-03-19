package ee.taltech.gamecollection.cardgames

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ee.taltech.gamecollection.R
import ee.taltech.gamecollection.paranoia.ParanoiaQuestionActivity

class CroatianPick : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_croatian_pick)

        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        val buttonCroatianPickRules: Button = findViewById(R.id.buttonCroatianPickRules)
        buttonCroatianPickRules.setOnClickListener {
            it.startAnimation(bounceAnimation)
            intent = Intent(this, CroatianPickRules::class.java)
            startActivity(intent)
        }

        val buttonBackFromCroatianPick: Button = findViewById(R.id.buttonBackFromCroatianPick)
        buttonBackFromCroatianPick.setOnClickListener {
            it.startAnimation(bounceAnimation)
            intent = Intent(this, CardGamesActivity::class.java)
            startActivity(intent)
        }
    }
}
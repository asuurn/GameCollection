package ee.taltech.gamecollection.cardgames.CroatianPick

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ee.taltech.gamecollection.R

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
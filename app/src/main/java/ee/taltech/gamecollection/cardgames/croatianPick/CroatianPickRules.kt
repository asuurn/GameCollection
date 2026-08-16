package ee.taltech.gamecollection.cardgames.croatianPick

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ee.taltech.gamecollection.R
import ee.taltech.gamecollection.cardgames.CardGamesActivity

class CroatianPickRules : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_croatian_pick_rules)

        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        val buttonBackToCardGames: Button = findViewById(R.id.buttonBackToCardGames)
        buttonBackToCardGames.setOnClickListener {
            it.startAnimation(bounceAnimation)
            intent = Intent(this, CardGamesActivity::class.java)
            startActivity(intent)
        }
    }
}
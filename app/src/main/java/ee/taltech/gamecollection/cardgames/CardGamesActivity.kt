package ee.taltech.gamecollection.cardgames

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ee.taltech.gamecollection.MainActivity
import ee.taltech.gamecollection.R
import ee.taltech.gamecollection.cardgames.CroatianPick.CroatianPick

class CardGamesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_games)

        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        val buttonCroatianPick: Button = findViewById(R.id.buttonCroatianPick)
        buttonCroatianPick.setOnClickListener {
            it.startAnimation(bounceAnimation)
            intent = Intent(this, CroatianPick::class.java)
            startActivity(intent)
        }

        val buttonTups: Button = findViewById(R.id.buttonTups)
        buttonTups.setOnClickListener {
            it.startAnimation(bounceAnimation)
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val buttonBackFromCardGames: Button = findViewById(R.id.buttonBackFromCardGames)
        buttonBackFromCardGames.setOnClickListener {
            it.startAnimation(bounceAnimation)
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
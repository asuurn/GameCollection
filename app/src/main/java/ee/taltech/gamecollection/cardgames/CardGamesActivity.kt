package ee.taltech.gamecollection.cardgames

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import ee.taltech.gamecollection.R
import ee.taltech.gamecollection.cardgames.baila.BailaActivity
import ee.taltech.gamecollection.cardgames.croatianPick.CroatianPickRules
import ee.taltech.gamecollection.cardgames.poker.PokerActivity
import ee.taltech.gamecollection.cardgames.uno.UnoActivity

class CardGamesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_games)

        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        val buttonCroatianPick: Button = findViewById(R.id.buttonToBaila)
        buttonCroatianPick.setOnClickListener {
            it.startAnimation(bounceAnimation)
            intent = Intent(this, BailaActivity::class.java)
            startActivity(intent)
        }

        val buttonPoker: Button = findViewById(R.id.buttonToPoker)
        buttonPoker.setOnClickListener {
            it.startAnimation(bounceAnimation)
            intent = Intent(this, PokerActivity::class.java)
            startActivity(intent)
        }

        val buttonUno: Button = findViewById(R.id.buttonToTruthOrDare)
        buttonUno.setOnClickListener {
            it.startAnimation(bounceAnimation)
            intent = Intent(this, UnoActivity::class.java)
            startActivity(intent)
        }

        val buttonToCroatianPick: Button = findViewById(R.id.buttonToCroatianPick)
        buttonToCroatianPick.setOnClickListener {
            it.startAnimation(bounceAnimation)
            intent = Intent(this, CroatianPickRules::class.java)
            startActivity(intent)
        }

        val buttonBack: ImageButton = findViewById(R.id.buttonBack)
        buttonBack.setOnClickListener {
            finish()
        }
    }
}
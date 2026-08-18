package ee.taltech.gamecollection

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ee.taltech.gamecollection.cardgames.CardGamesActivity
import ee.taltech.gamecollection.paranoia.ParanoiaQuestionActivity
import ee.taltech.gamecollection.scoreboard.Scoreboard
import ee.taltech.gamecollection.truthOrDare.TruthOrDareActivity
import ee.taltech.gamecollection.twister.TwisterActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            val intent = Intent(this, Scoreboard::class.java)
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
}
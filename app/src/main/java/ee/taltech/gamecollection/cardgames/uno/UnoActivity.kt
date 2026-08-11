package ee.taltech.gamecollection.cardgames.uno

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import ee.taltech.gamecollection.R

class UnoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uno)

        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)

        val basicButton: Button = findViewById(R.id.buttonUnoBasic)
        basicButton.setOnClickListener {
            it.startAnimation(bounceAnimation)
            val intent = Intent(this, UnoBasicActivity::class.java)
            startActivity(intent)
        }

        val advancedButton: Button = findViewById(R.id.buttonUnoAdvanced)
        advancedButton.setOnClickListener {
            it.startAnimation(bounceAnimation)
            val intent = Intent(this, UnoAdvancedActivity::class.java)
            startActivity(intent)
        }

        val drunkUnoButton: Button = findViewById(R.id.buttonUnoDrunk)
        drunkUnoButton.setOnClickListener {
            it.startAnimation(bounceAnimation)
            val intent = Intent(this, UnoDrunkActivity::class.java)
            startActivity(intent)
        }

        val backButton: ImageButton = findViewById(R.id.buttonBack)
        backButton.setOnClickListener {
            finish()
        }
    }
}
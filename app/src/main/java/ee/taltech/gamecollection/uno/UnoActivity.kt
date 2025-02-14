package ee.taltech.gamecollection.uno

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ee.taltech.gamecollection.MainActivity
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

        val backButton: Button = findViewById(R.id.buttonBackFromUno)
        backButton.setOnClickListener {
            it.startAnimation(bounceAnimation)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
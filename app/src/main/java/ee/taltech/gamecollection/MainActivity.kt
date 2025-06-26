package ee.taltech.gamecollection

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ee.taltech.gamecollection.baila.BailaActivity
import ee.taltech.gamecollection.paranoia.ParanoiaActivity
import ee.taltech.gamecollection.soundboard.SoundboardActivity
import ee.taltech.gamecollection.twister.TwisterActivity
import ee.taltech.gamecollection.uno.UnoActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)

        val paranoiaButton: Button = findViewById(R.id.buttonToParanoia)
        paranoiaButton.setOnClickListener {
            it.startAnimation(bounceAnimation)
            val intent = Intent(this, ParanoiaActivity::class.java)
            startActivity(intent)
        }

        val buttonToBaila: Button = findViewById(R.id.buttonToBaila)
        buttonToBaila.setOnClickListener {
            it.startAnimation(bounceAnimation)
            val intent = Intent(this, BailaActivity::class.java)
            startActivity(intent)
        }

        val buttonToUno: Button = findViewById(R.id.buttonToUno)
        buttonToUno.setOnClickListener {
            it.startAnimation(bounceAnimation)
            val intent = Intent(this, UnoActivity::class.java)
            startActivity(intent)
        }

        val buttonToSoundboard: Button = findViewById(R.id.buttonToSoundboard)
        buttonToSoundboard.setOnClickListener {
            it.startAnimation(bounceAnimation)
            val intent = Intent(this, SoundboardActivity::class.java)
            startActivity(intent)
        }

        val buttonToTwister: Button = findViewById(R.id.buttonToTwister)
        buttonToTwister.setOnClickListener {
            it.startAnimation(bounceAnimation)
            val intent = Intent(this, TwisterActivity::class.java)
            startActivity(intent)
        }
    }
}
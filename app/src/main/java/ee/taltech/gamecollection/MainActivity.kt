package ee.taltech.gamecollection

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ee.taltech.gamecollection.paranoia.ParanoiaActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)

        val paranoiaButton: Button = findViewById(R.id.buttonToBaila)
        paranoiaButton.setOnClickListener {
            it.startAnimation(bounceAnimation)
            val intent = Intent(this, ParanoiaActivity::class.java)
            startActivity(intent)
        }

        val customButton: Button = findViewById(R.id.buttonToBaila)
        customButton.setOnClickListener {
            it.startAnimation(bounceAnimation)
            val intent = Intent(this, ParanoiaActivity::class.java)
            startActivity(intent)
        }
    }
}
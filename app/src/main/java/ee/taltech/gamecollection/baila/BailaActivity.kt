package ee.taltech.gamecollection.baila

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ee.taltech.gamecollection.MainActivity
import ee.taltech.gamecollection.R
import ee.taltech.gamecollection.paranoia.ParanoiaActivity

class BailaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_baila)

        val customButton: Button = findViewById(R.id.buttonBackFromBaila)
        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        customButton.setOnClickListener {
            it.startAnimation(bounceAnimation)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
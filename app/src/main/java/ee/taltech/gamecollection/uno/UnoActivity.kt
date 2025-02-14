package ee.taltech.gamecollection.uno

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
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
    }
}
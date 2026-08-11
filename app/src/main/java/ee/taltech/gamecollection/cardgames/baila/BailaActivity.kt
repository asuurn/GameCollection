package ee.taltech.gamecollection.cardgames.baila

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ee.taltech.gamecollection.MainActivity
import ee.taltech.gamecollection.R
import ee.taltech.gamecollection.cardgames.CardGamesActivity
import ee.taltech.gamecollection.paranoia.ParanoiaActivity

class BailaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_baila)

        val customButton: ImageButton = findViewById(R.id.buttonBack)
        customButton.setOnClickListener {
            finish()
        }
    }
}
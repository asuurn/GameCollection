package ee.taltech.gamecollection.cardgames.uno

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ee.taltech.gamecollection.R

class UnoAdvancedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uno_advanced)
        val back: ImageButton = findViewById(R.id.buttonBack)
        back.setOnClickListener {
            finish()
        }
    }
}
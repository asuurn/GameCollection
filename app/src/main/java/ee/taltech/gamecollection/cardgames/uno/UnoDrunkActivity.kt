package ee.taltech.gamecollection.cardgames.uno

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import ee.taltech.gamecollection.R

class UnoDrunkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uno_drunk)
        val back: ImageButton = findViewById(R.id.buttonBack)
        back.setOnClickListener {
            finish()
        }
    }
}
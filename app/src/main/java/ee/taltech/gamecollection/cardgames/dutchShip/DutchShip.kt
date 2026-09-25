package ee.taltech.gamecollection.cardgames.dutchShip

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import ee.taltech.gamecollection.R

class DutchShip : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dutch_ship_rules)

        val back: ImageButton = findViewById(R.id.buttonBack)
        back.setOnClickListener {
            finish()
        }
    }
}
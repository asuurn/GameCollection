package ee.taltech.gamecollection.cardgames.croatianPick

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import ee.taltech.gamecollection.R

class CroatianPickRules : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_croatian_pick_rules)

        val back: ImageButton = findViewById(R.id.buttonBack)
        back.setOnClickListener {
            finish()
        }
    }
}
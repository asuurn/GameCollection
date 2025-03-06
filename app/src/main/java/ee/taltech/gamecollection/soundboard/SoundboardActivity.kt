package ee.taltech.gamecollection.soundboard

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ee.taltech.gamecollection.R

class SoundboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soundboard)

        val buttonIds = listOf(
            R.id.buttonSB1, R.id.buttonSB2, R.id.buttonSB3,
            R.id.buttonSB4, R.id.buttonSB5, R.id.buttonSB6,
            R.id.buttonSB7, R.id.buttonSB8, R.id.buttonSB9,
            R.id.buttonSB10, R.id.buttonSB11, R.id.buttonSB12,
            R.id.buttonBackFromSoundboard, R.id.buttonRecord, R.id.buttonRename
        )

        addBounce(buttonIds)
    }

    private fun addBounce(buttonIds: List<Int>) {
        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)

        buttonIds.forEach { id ->
            findViewById<Button>(id).apply {
                setOnClickListener {
                    startAnimation(bounceAnimation)
                }
            }
        }
    }
}
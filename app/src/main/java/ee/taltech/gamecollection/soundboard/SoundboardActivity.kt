package ee.taltech.gamecollection.soundboard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import ee.taltech.gamecollection.MainActivity
import ee.taltech.gamecollection.R

class SoundboardActivity : AppCompatActivity() {
    private val requiredPermissions = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

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

        if (!hasPermissions()) {
            requestPermissionsLauncher.launch(requiredPermissions)
        }
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

    fun backFromSoundboard(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun onClickRecordSound(view: View) {
    }

    private fun hasPermissions(): Boolean {
        return requiredPermissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val deniedPermissions = permissions.filter { !it.value }.keys
            if (deniedPermissions.isNotEmpty()) {
                Toast.makeText(this, "Permissions denied: $deniedPermissions", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "All permissions granted!", Toast.LENGTH_SHORT).show()
            }
        }
}
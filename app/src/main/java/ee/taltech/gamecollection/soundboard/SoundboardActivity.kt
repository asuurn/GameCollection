import android.media.MediaPlayer
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

    private var mediaPlayer: MediaPlayer? = null

/*private val buttonSoundMap = mapOf(
     R.id.buttonSB1 to R.raw.sound1,
     R.id.buttonSB2 to R.raw.sound2,
     R.id.buttonSB3 to R.raw.sound3,
     R.id.buttonSB4 to R.raw.sound4,
     R.id.buttonSB5 to R.raw.sound5,
     R.id.buttonSB6 to R.raw.sound6,
     R.id.buttonSB7 to R.raw.sound7,
     R.id.buttonSB8 to R.raw.sound8,
     R.id.buttonSB9 to R.raw.sound9,
     R.id.buttonSB10 to R.raw.sound10,
     R.id.buttonSB11 to R.raw.sound11,
     R.id.buttonSB12 to R.raw.sound12
 )*/

 override fun onCreate(savedInstanceState: Bundle?) {
     super.onCreate(savedInstanceState)
     setContentView(R.layout.activity_soundboard)

     /*val allButtons = buttonSoundMap.keys + listOf(
         R.id.buttonBackFromSoundboard, R.id.buttonRecord, R.id.buttonRename
     )
     addBounce(allButtons)

     setSoundListeners()**/

     if (!hasPermissions()) {
         requestPermissionsLauncher.launch(requiredPermissions)
     }
 }

 /*private fun setSoundListeners() {
     for ((buttonId, soundResId) in buttonSoundMap) {
         findViewById<Button>(buttonId).setOnClickListener {
             playSound(soundResId)
         }
     }
 }
*/
 private fun playSound(soundResId: Int) {
     mediaPlayer?.release()
     mediaPlayer = MediaPlayer.create(this, soundResId)
     mediaPlayer?.start()
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

 override fun onDestroy() {
     mediaPlayer?.release()
     mediaPlayer = null
     super.onDestroy()
 }

 fun backFromSoundboard(view: View) {
     val intent = Intent(this, MainActivity::class.java)
     startActivity(intent)
 }

 fun onClickRecordSound(view: View) {}

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

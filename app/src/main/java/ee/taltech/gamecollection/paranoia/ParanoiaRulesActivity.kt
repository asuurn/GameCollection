package ee.taltech.gamecollection.paranoia

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ee.taltech.gamecollection.R

class ParanoiaRulesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paranoia_rules)
    }

    fun onClickBackToMain(view: View) {
        intent = Intent(this, ParanoiaActivity::class.java)
        startActivity(intent)
    }
}
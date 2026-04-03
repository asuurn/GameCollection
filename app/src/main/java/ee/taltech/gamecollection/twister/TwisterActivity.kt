package ee.taltech.gamecollection.twister

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ee.taltech.gamecollection.R

class TwisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_twister)

        val spinner = findViewById<SpinnerView>(R.id.spinnerView)
        val button = findViewById<AppCompatButton>(R.id.btnSpin)
        val resultText = findViewById<TextView>(R.id.textViewTwisterResult)

        spinner.setOnResultListener { result ->
            resultText.text = result
        }

        button.setOnClickListener {
            spinner.spin()
        }
    }
}
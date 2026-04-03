package ee.taltech.gamecollection.twister

import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import ee.taltech.gamecollection.R

class TwisterActivity : AppCompatActivity() {

    private lateinit var spinner: SpinnerView
    private lateinit var button: AppCompatButton

    private val handler = Handler()
    private var isAutoRunning = false
    private var intervalMs = 10000L

    private lateinit var tts: android.speech.tts.TextToSpeech

    private var ttsReady = false

    private val autoRunnable = object : Runnable {
        override fun run() {
            if (isAutoRunning) {
                spinner.spin()
                handler.postDelayed(this, intervalMs)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_twister)

        spinner = findViewById(R.id.spinnerView)
        button = findViewById(R.id.btnSpin)

        val resultText = findViewById<TextView>(R.id.textViewTwisterResult)
        val switchAuto = findViewById<Switch>(R.id.switchAuto)
        val seekBar = findViewById<SeekBar>(R.id.seekBarInterval)
        val intervalText = findViewById<TextView>(R.id.textInterval)

        tts = android.speech.tts.TextToSpeech(this) { status ->
            if (status == android.speech.tts.TextToSpeech.SUCCESS) {
                val result = tts.setLanguage(java.util.Locale.UK)

                ttsReady = result != android.speech.tts.TextToSpeech.LANG_MISSING_DATA &&
                        result != android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED
            }
        }

        spinner.setOnResultListener { result ->
            resultText.text = result

            if (ttsReady) {
                tts.speak(result, android.speech.tts.TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }

        button.setOnClickListener {
            spinner.spin()
        }

        // Interval control
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val seconds = 10 + progress   // 10..120
                intervalMs = seconds * 1000L
                intervalText.text = "Interval: ${seconds}s"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Auto mode toggle
        switchAuto.setOnCheckedChangeListener { _, isChecked ->
            isAutoRunning = isChecked

            if (isChecked) {
                handler.post(autoRunnable)
                button.isEnabled = false
            } else {
                handler.removeCallbacks(autoRunnable)
                button.isEnabled = true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(autoRunnable)

        tts.stop()
        tts.shutdown()
    }
}
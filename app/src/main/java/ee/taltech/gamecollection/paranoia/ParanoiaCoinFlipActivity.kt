package ee.taltech.gamecollection.paranoia

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ee.taltech.gamecollection.MainActivity
import ee.taltech.gamecollection.R
import kotlin.random.Random

class ParanoiaCoinFlipActivity : AppCompatActivity() {
    private lateinit var coinImage: ImageView
    private var isHeads = true
    private var hasFlipped = false
    private lateinit var resultText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paranoia_coin_flip)

        coinImage = findViewById(R.id.imageViewCoin)

        coinImage.setOnClickListener {
            if (!hasFlipped) {
                flipCoin()
                hasFlipped = true
            }
        }

        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        resultText = findViewById(R.id.textViewResult)

        val buttonNewQuestion: Button = findViewById(R.id.buttonNewQuestion)
        buttonNewQuestion.isEnabled = false
        buttonNewQuestion.setOnClickListener {
            it.startAnimation(bounceAnimation)
            onClickNewQuestion()
        }

        val buttonToMain: Button = findViewById(R.id.buttonToMain)
        buttonToMain.setOnClickListener {
            it.startAnimation(bounceAnimation)
            onClickBackToMain()
        }
    }

    private fun flipCoin() {
        val flip = ObjectAnimator.ofFloat(coinImage, "rotationY", 0f, 1800f)
        flip.duration = 1000

        flip.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                isHeads = Random.nextBoolean()
                if (isHeads) {
                    resultText.text = "Truth will come out"
                    coinImage.setImageResource(R.drawable.heads)
                } else {
                    resultText.text = "Truth will stay hidden"
                    coinImage.setImageResource(R.drawable.tails)
                }

                val buttonNewQuestion: Button = findViewById(R.id.buttonNewQuestion)
                buttonNewQuestion.isEnabled = true
            }
        })
        flip.start()
    }


    fun onClickNewQuestion() {
        hasFlipped = false
        intent = Intent(this, ParanoiaQuestionActivity::class.java)
        startActivity(intent)
    }

    fun onClickBackToMain() {
        hasFlipped = false
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
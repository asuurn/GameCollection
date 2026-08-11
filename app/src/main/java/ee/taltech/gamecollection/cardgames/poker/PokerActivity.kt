package ee.taltech.gamecollection.cardgames.poker

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ee.taltech.gamecollection.R

class PokerActivity : AppCompatActivity() {

    private val pokerHands = listOf(
        PokerHand("ROYAL FLUSH", listOf("A♦", "K♦", "Q♦", "J♦", "10♦")),
        PokerHand("STRAIGHT FLUSH", listOf("J♠", "10♠", "9♠", "8♠", "7♠")),
        PokerHand("FOUR OF A KIND", listOf("9♥", "9♣", "9♦", "9♠", " ")),
        PokerHand("FULL HOUSE", listOf("A♥", "A♣", "A♦", "3♣", "3♥")),
        PokerHand("FLUSH", listOf("K♣", "10♣", "8♣", "7♣", "5♣")),

        PokerHand("STRAIGHT", listOf("10♥", "9♣", "8♦", "7♠", "6♥")),
        PokerHand("THREE OF A KIND", listOf("7♥", "7♣", "7♠", " ", " ")),
        PokerHand("TWO PAIR", listOf("J♥", "J♣", "5♠", "5♣", " ")),
        PokerHand("PAIR", listOf("A♥", "A♣", " ", " ", " ")),
        PokerHand("HIGH CARD", listOf("K♥", " ", " ", " ", " "))
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poker)

        val customButton: ImageButton = findViewById(R.id.buttonBack)
        customButton.setOnClickListener {
            finish()
        }

        val handsContainer: LinearLayout =
            findViewById(R.id.handsContainer)

        pokerHands.forEach { hand ->

            val handView = layoutInflater.inflate(
                R.layout.item_poker_hand,
                handsContainer,
                false
            )

            val handName: TextView =
                handView.findViewById(R.id.handName)

            val cardsContainer: LinearLayout =
                handView.findViewById(R.id.cardsContainer)

            handName.text = hand.name

            hand.cards.forEach { cardText ->

                val card = TextView(this)

                card.text = cardText
                card.textSize = 16f
                card.gravity = Gravity.CENTER

                card.setTextColor(
                    if (cardText.contains("♥") ||
                        cardText.contains("♦")) {
                        Color.RED
                    } else {
                        Color.BLACK
                    }
                )

                card.setBackgroundResource(R.drawable.poker_card)

                val params = LinearLayout.LayoutParams(
                    42.dp,
                    60.dp
                )

                params.setMargins(2.dp, 0, 2.dp, 0)
                card.layoutParams = params
                cardsContainer.addView(card)
            }

            handsContainer.addView(handView)
        }
    }
}

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
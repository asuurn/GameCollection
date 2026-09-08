package ee.taltech.gamecollection.cardgames.poker

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.TextViewCompat
import ee.taltech.gamecollection.R

class PokerActivity : AppCompatActivity() {

    private lateinit var pokerHands: List<PokerHand>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poker)

        createPokerHands()

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
                val card = androidx.appcompat.widget.AppCompatTextView(this)

                card.text = cardText
                card.gravity = Gravity.CENTER
                card.maxLines = 1
                card.setPadding(0, 0, 0, 0)

                card.setTextColor(
                    if (cardText.contains("♥") || cardText.contains("♦")) {
                        Color.RED
                    } else {
                        Color.BLACK
                    }
                )

                card.setBackgroundResource(R.drawable.poker_card)

                // Each card receives an equal share of the available width.
                card.layoutParams = LinearLayout.LayoutParams(
                    0,
                    60.dp,
                    1f
                ).apply {
                    setMargins(2.dp, 0, 2.dp, 0)
                }

                TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
                    card,
                    10,
                    16,
                    1,
                    TypedValue.COMPLEX_UNIT_SP
                )

                // Keep the original 42:60 proportions as the width changes.
                card.addOnLayoutChangeListener { view, left, _, right, _, _, _, _, _ ->
                    val cardWidth = right - left

                    if (cardWidth > 0) {
                        val targetHeight =
                            (cardWidth * 60f / 42f).toInt().coerceAtLeast(1)

                        if (view.layoutParams.height != targetHeight) {
                            view.layoutParams = view.layoutParams.apply {
                                height = targetHeight
                            }
                        }
                    }
                }

                cardsContainer.addView(card)
            }

            handsContainer.addView(handView)
        }
    }

    fun createPokerHands() {
        pokerHands = listOf(
            PokerHand(getString(R.string.royal_flush), listOf("A♦", "K♦", "Q♦", "J♦", "10♦")),
            PokerHand(getString(R.string.straight_flush), listOf("J♠", "10♠", "9♠", "8♠", "7♠")),
            PokerHand(getString(R.string.four_of_a_kind), listOf("9♥", "9♣", "9♦", "9♠", " ")),
            PokerHand(getString(R.string.full_house), listOf("A♥", "A♣", "A♦", "3♣", "3♥")),
            PokerHand(getString(R.string.flush), listOf("K♣", "10♣", "8♣", "7♣", "5♣")),
            PokerHand(getString(R.string.straight), listOf("10♥", "9♣", "8♦", "7♠", "6♥")),
            PokerHand(getString(R.string.three_of_a_kind), listOf("7♥", "7♣", "7♠", " ", " ")),
            PokerHand(getString(R.string.two_pair), listOf("J♥", "J♣", "5♠", "5♣", " ")),
            PokerHand(getString(R.string.pair), listOf("A♥", "A♣", " ", " ", " ")),
            PokerHand(getString(R.string.high_card), listOf("K♥", " ", " ", " ", " "))
        )
    }
}

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
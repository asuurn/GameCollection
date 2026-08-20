package ee.taltech.gamecollection.truthOrDare

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ee.taltech.gamecollection.R
import kotlin.math.abs

class TruthOrDareActivity : AppCompatActivity() {

    private lateinit var inflater: LayoutInflater
    private lateinit var cardStack: ViewGroup

    private lateinit var topCard: View
    private lateinit var middleCard: View
    private lateinit var backCard: View

    private var nextTruthIndex = 0
    private var nextDareIndex = 0

    private var previewType: CardType? = null
    private var previewCard: CardData? = null

    private lateinit var cards: List<CardData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_truth_or_dare)

        cards =
            readFileAsLines(R.raw.truth, CardType.TRUTH) +
                    readFileAsLines(R.raw.dare, CardType.DARE)

        inflater = LayoutInflater.from(this)
        cardStack = findViewById(R.id.cardStack)

        val firstCard = getNextCard(CardType.TRUTH)
        val secondCard = getPreviewCard(CardType.TRUTH, 0)
        val thirdCard = getPreviewCard(CardType.TRUTH, 1)

        topCard = createCard(
            0F,
            0F,
            firstCard
        )

        middleCard = createCard(
            -20F,
            -20F,
            secondCard
        )

        backCard = createCard(
            -40F,
            -40F,
            thirdCard
        )

        cardStack.addView(backCard)
        cardStack.addView(middleCard)
        cardStack.addView(topCard)

        setupSwipe(topCard)
    }

    private fun getNextCard(type: CardType): CardData {
        val matchingCards = cards.filter {
            it.type == type
        }

        if (matchingCards.isEmpty()) {
            throw IllegalStateException(
                "No cards available for $type"
            )
        }

        val index = when (type) {
            CardType.TRUTH -> nextTruthIndex
            CardType.DARE -> nextDareIndex
        }

        val card = matchingCards[index]
        when (type) {
            CardType.TRUTH -> {
                nextTruthIndex =
                    (nextTruthIndex + 1) % matchingCards.size
            }
            CardType.DARE -> {
                nextDareIndex =
                    (nextDareIndex + 1) % matchingCards.size
            }
        }

        return card
    }

    private fun getPreviewCard(
        type: CardType,
        offset: Int = 0
    ): CardData {
        val matchingCards = cards.filter {
            it.type == type
        }

        val currentIndex = when (type) {
            CardType.TRUTH -> nextTruthIndex
            CardType.DARE -> nextDareIndex
        }

        val index =
            (currentIndex + offset) % matchingCards.size

        return matchingCards[index]
    }

    private fun createCard(
        x: Float,
        y: Float,
        cardData: CardData
    ): View {
        val card = inflater.inflate(
            R.layout.card_truth_or_dare,
            cardStack,
            false
        )

        updateCard(card, cardData)

        card.translationX = x
        card.translationY = y

        val params = FrameLayout.LayoutParams(
            card.layoutParams
        )
        params.gravity = Gravity.CENTER
        card.layoutParams = params

        return card
    }

    private fun updateCard(
        card: View,
        cardData: CardData
    ) {

        card.findViewById<TextView>(
            R.id.questionText
        ).text = cardData.question

        card.tag = cardData
    }


    private fun setupSwipe(card: View) {
        var startX = 0f
        var startY = 0f

        card.setOnTouchListener { view, event ->
            when (event.action) {

                MotionEvent.ACTION_DOWN -> {
                    startX = event.rawX
                    startY = event.rawY
                    previewType = null
                    previewCard = null
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val deltaX = event.rawX - startX
                    val deltaY = event.rawY - startY

                    val maxVerticalMovement = 100F

                    val limitedDeltaY = deltaY.coerceIn(
                        -maxVerticalMovement,
                        maxVerticalMovement
                    )

                    view.translationX = deltaX
                    view.translationY = limitedDeltaY
                    view.rotation = deltaX * 0.05F

                    // RIGHT = TRUTH & LEFT = DARE
                    if (abs(deltaX) > 10F) {
                        val currentType =
                            if (deltaX > 0) {
                                CardType.TRUTH
                            } else {
                                CardType.DARE
                            }

                        if (previewType != currentType) {
                            previewType = currentType
                            previewCard = getPreviewCard(currentType)
                            updateCard(
                                middleCard,
                                previewCard!!
                            )
                        }
                    }
                    true
                }

                MotionEvent.ACTION_UP -> {
                    val deltaX = event.rawX - startX
                    val deltaY = event.rawY - startY

                    val touchSlop = ViewConfiguration
                            .get(view.context)
                            .scaledTouchSlop
                    if (
                        abs(deltaX) < touchSlop &&
                        abs(deltaY) < touchSlop
                    ) {
                        view.performClick()
                        return@setOnTouchListener true
                    }

                    if (abs(deltaX) > 300F) {
                        val toRight = deltaX > 0
                        swipeAway(view, toRight)

                    } else {
                        returnCard(view)
                        restoreMiddleCard()
                        previewType = null
                        previewCard = null
                    }
                    true
                }
                else -> false
            }
        }
    }

    // Successful swipe
    private fun swipeAway(
        card: View,
        toRight: Boolean
    ) {
        // RIGHT = TRUTH & LEFT = DARE
        val selectedType =
            if (toRight) {
                CardType.TRUTH
            } else {
                CardType.DARE
            }

        val selectedCard = getNextCard(selectedType)
        updateCard(middleCard, selectedCard)

        previewType = null
        previewCard = null

        val direction = if (toRight) 1 else -1
        card.animate()
            .translationX(
                direction * 1200F
            )
            .rotation(
                direction * 30F
            )
            .setDuration(300)
            .withEndAction {

                val oldTop = topCard        // oldTop    -> back
                val oldMiddle = middleCard  // oldMiddle -> top
                val oldBack = backCard      // oldBack   -> middle

                topCard = oldMiddle
                middleCard = oldBack
                backCard = oldTop

                updateCard(
                    backCard,
                    getPreviewCard(CardType.TRUTH, 1)
                )

                backCard.translationX = -40F
                backCard.translationY = -40F
                backCard.rotation = 0F

                cardStack.removeView(backCard)

                cardStack.addView(
                    backCard,
                    0
                )

                topCard.animate()
                    .translationX(0F)
                    .translationY(0F)
                    .rotation(0F)
                    .setDuration(200)
                    .start()

                middleCard.animate()
                    .translationX(-20F)
                    .translationY(-20F)
                    .rotation(0F)
                    .setDuration(200)
                    .start()

                backCard.animate()
                    .translationX(-40F)
                    .translationY(-40F)
                    .rotation(0F)
                    .setDuration(200)
                    .start()

                setupSwipe(topCard)
            }
            .start()
    }

    // Cancel swipe
    private fun returnCard(card: View) {
        card.animate()
            .translationX(0F)
            .translationY(0F)
            .rotation(0F)
            .setDuration(200)
            .start()
    }
    private fun restoreMiddleCard() {
        updateCard(
            middleCard,
            getPreviewCard(CardType.TRUTH)
        )

        middleCard.translationX = -20F
        middleCard.translationY = -20F
        middleCard.rotation = 0F
    }

    private fun readFileAsLines(resourceId: Int, type: CardType): MutableList<CardData> {

        val cards = mutableListOf<CardData>()

        resources.openRawResource(resourceId).bufferedReader().useLines { lines ->
            for (line in lines) {
                if (line.isNotBlank()) {
                    cards.add(CardData(type, line))
                }
            }
        }

        return cards
    }
}
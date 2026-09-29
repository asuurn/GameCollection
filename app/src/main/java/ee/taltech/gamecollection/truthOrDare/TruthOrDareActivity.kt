package ee.taltech.gamecollection.truthOrDare

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import ee.taltech.gamecollection.R
import kotlin.math.abs
import androidx.core.content.edit
import androidx.core.os.ConfigurationCompat

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

    private val stackStep: Float
        get() = 8f * resources.displayMetrics.density

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_truth_or_dare)

        val buttonBack: ImageButton = findViewById(R.id.buttonBack)
        buttonBack.setOnClickListener {
            finish()
        }

        val buttonSettings: ImageButton = findViewById(R.id.buttonSettings)
        buttonSettings.setOnClickListener {
            showSettingsDialog()
        }

        val settings = loadSettings()
        val cardResources = getCardResources()
        val loadedCards = mutableListOf<CardData>()

        loadedCards += readFileAsLines(cardResources.truth, CardType.TRUTH)
        loadedCards += readFileAsLines(cardResources.dare, CardType.DARE)

        if (settings.relationship) {
            loadedCards += readFileAsLines(cardResources.relationshipTruth, CardType.TRUTH)
            loadedCards += readFileAsLines(cardResources.relationshipDare, CardType.DARE)
        }

        if (settings.sexual) {
            loadedCards += readFileAsLines(cardResources.sexualTruth, CardType.TRUTH)
            loadedCards += readFileAsLines(cardResources.sexualDare, CardType.DARE)
        }

        cards = loadedCards

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
            -2f * stackStep,
            -2f * stackStep,
            thirdCard
        )

        cardStack.addView(backCard)
        cardStack.addView(middleCard)
        cardStack.addView(topCard)

        cardStack.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            resizeCards()
        }
        cardStack.post { resizeCards() }

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

                backCard.translationX = -2f * stackStep
                backCard.translationY = -2f * stackStep
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
                    .translationX(-stackStep)
                    .translationY(-stackStep)
                    .rotation(0F)
                    .setDuration(200)
                    .start()

                backCard.animate()
                    .translationX(-2f * stackStep)
                    .translationY(-2f * stackStep)
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

        middleCard.translationX = -stackStep
        middleCard.translationY = -stackStep
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
        cards.shuffle()
        return cards
    }

    private fun showSettingsDialog() {

        val dialog = Dialog(this)

        dialog.setContentView(R.layout.dialog_truth_or_dare_settings)
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)

        val relationshipSwitch: SwitchCompat = dialog.findViewById(R.id.relationshipSwitch)
        val sexualSwitch: SwitchCompat = dialog.findViewById(R.id.sexualSwitch)
        val buttonCancel: Button = dialog.findViewById(R.id.buttonBack)

        val settings = loadSettings()

        relationshipSwitch.isChecked = settings.relationship
        sexualSwitch.isChecked = settings.sexual

        buttonCancel.setOnClickListener {
            saveSettings(
                TruthOrDareSettings(
                    relationship = relationshipSwitch.isChecked,
                    sexual = sexualSwitch.isChecked
                )
            )

            dialog.dismiss()
            recreate()
        }

        dialog.show()

        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.90).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun loadSettings(): TruthOrDareSettings {
        val preferences = getSharedPreferences(
            "truth_or_dare_settings",
            MODE_PRIVATE
        )

        return TruthOrDareSettings(
            relationship = preferences.getBoolean("relationship", false),
            sexual = preferences.getBoolean("sexual", false)
        )
    }

    private fun saveSettings(settings: TruthOrDareSettings) {
        getSharedPreferences(
            "truth_or_dare_settings",
            MODE_PRIVATE
        )
            .edit {
                putBoolean("relationship", settings.relationship)
                    .putBoolean("sexual", settings.sexual)
            }
    }

    private fun getCardResources(): CardResources {

        val language = ConfigurationCompat
            .getLocales(resources.configuration)
            .get(0)
            ?.language

        return if (language == "et") {
            CardResources(
                truth = R.raw.truth_et,
                dare = R.raw.dare_et,
                relationshipTruth = R.raw.relationship_truth_et,
                relationshipDare = R.raw.relationship_dare_et,
                sexualTruth = R.raw.sexual_truth_et,
                sexualDare = R.raw.sexual_dare_et
            )
        } else {
            CardResources(
                truth = R.raw.truth_en,
                dare = R.raw.dare_en,
                relationshipTruth = R.raw.relationship_truth_en,
                relationshipDare = R.raw.relationship_dare_en,
                sexualTruth = R.raw.sexual_truth_en,
                sexualDare = R.raw.sexual_dare_en
            )
        }
    }

    private fun resizeCards() {
        val density = resources.displayMetrics.density

        val inset = (24f * density).toInt()

        val availableWidth =
            cardStack.width - cardStack.paddingLeft -
                    cardStack.paddingRight - inset * 2

        val availableHeight =
            cardStack.height - cardStack.paddingTop -
                    cardStack.paddingBottom - inset * 2

        if (availableWidth <= 0 || availableHeight <= 0) return

        val cardWidth = minOf(
            availableWidth.toFloat(),
            availableHeight * 340f / 500f,
            340f * density
        ).toInt()

        val cardHeight = (cardWidth * 500f / 340f).toInt()

        listOf(topCard, middleCard, backCard).forEach { card ->
            if (
                card.layoutParams.width != cardWidth ||
                card.layoutParams.height != cardHeight
            ) {
                card.layoutParams = FrameLayout.LayoutParams(
                    cardWidth,
                    cardHeight,
                    Gravity.CENTER
                )
            }
        }
    }
}
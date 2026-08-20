package ee.taltech.gamecollection.truthOrDare

data class CardData(
    val type: CardType,
    val question: String
)

enum class CardType {
    TRUTH,
    DARE
}
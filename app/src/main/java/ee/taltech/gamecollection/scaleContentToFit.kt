package ee.taltech.gamecollection

import android.view.View
import android.view.ViewGroup

fun ViewGroup.scaleContentToFit(content: View) {
    val designWidth = content.layoutParams.width.toFloat()

    require(designWidth > 0) {
        "Content must have a fixed reference width in XML."
    }

    content.pivotX = 0f
    content.pivotY = 0f

    fun updateScale() {
        val availableWidth = width - paddingLeft - paddingRight
        val availableHeight = height - paddingTop - paddingBottom

        if (availableWidth <= 0 || availableHeight <= 0) return

        val scale = minOf(availableWidth / designWidth, 1f)

        val layoutHeight = (availableHeight / scale).toInt()

        if (content.layoutParams.height != layoutHeight) {
            content.layoutParams = content.layoutParams.apply {
                height = layoutHeight
            }
        }

        content.scaleX = scale
        content.scaleY = scale

        content.translationX =
            (availableWidth - designWidth * scale) / 2f
        content.translationY = 0f
    }

    addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
        updateScale()
    }

    post { updateScale() }
}
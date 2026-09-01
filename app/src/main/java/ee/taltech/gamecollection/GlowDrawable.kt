package ee.taltech.gamecollection

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt

class GlowDrawable(
    context: Context,
    @ColorInt private val glowColor: Int
) : Drawable() {

    private val density = context.resources.displayMetrics.density
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private fun withAlpha(color: Int, alpha: Int): Int {
        return Color.argb(
            alpha,
            Color.red(color),
            Color.green(color),
            Color.blue(color)
        )
    }

    override fun draw(canvas: Canvas) {

        val inset = 4f * density

        val rect = RectF(bounds).apply {
            inset(inset, inset)
        }

        paint.style = Paint.Style.FILL
        paint.color = Color.TRANSPARENT

        paint.setShadowLayer(
            24f * density,
            0f,
            0f,
            withAlpha(glowColor, 220)
        )

        canvas.drawRoundRect(
            rect,
            25f * density,
            25f * density,
            paint
        )

        paint.clearShadowLayer()

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3f * density
        paint.color = withAlpha(glowColor, 255)

        canvas.drawRoundRect(
            rect,
            25f * density,
            25f * density,
            paint
        )

        paint.strokeWidth = 2f * density
        paint.color = withAlpha(glowColor, 230)

        canvas.drawRoundRect(
            rect,
            25f * density,
            25f * density,
            paint
        )

        paint.clearShadowLayer()
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(filter: ColorFilter?) {
        paint.colorFilter = filter
    }

    override fun getOpacity() = PixelFormat.TRANSLUCENT
}
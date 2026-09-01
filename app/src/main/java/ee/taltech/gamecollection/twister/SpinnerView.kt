package ee.taltech.gamecollection.twister

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.doOnEnd
import ee.taltech.gamecollection.R
import kotlin.math.min
import kotlin.random.Random

data class SpinnerSector(
    val label: String,
    val side: String,
    val isHand: Boolean,
    val color: Int
)

class SpinnerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = minOf(
            MeasureSpec.getSize(widthMeasureSpec),
            MeasureSpec.getSize(heightMeasureSpec)
        )
        setMeasuredDimension(size, size)
    }

    private val armBitmap =
        BitmapFactory.decodeResource(context.resources, R.drawable.arm)

    private val legBitmap =
        BitmapFactory.decodeResource(context.resources, R.drawable.foot)

    private val sectors = listOf(
        SpinnerSector(
            context.getString(R.string.left_hand_red),
            "L",
            true,
            Color.RED
        ),
        SpinnerSector(
            context.getString(R.string.left_hand_blue),
            "L",
            true,
            Color.BLUE
        ),
        SpinnerSector(
            context.getString(R.string.left_hand_green),
            "L",
            true,
            Color.GREEN
        ),
        SpinnerSector(
            context.getString(R.string.left_hand_yellow),
            "L",
            true,
            Color.YELLOW
        ),

        SpinnerSector(
            context.getString(R.string.left_leg_red),
            "L",
            false,
            Color.RED
        ),
        SpinnerSector(
            context.getString(R.string.left_leg_blue),
            "L",
            false,
            Color.BLUE
        ),
        SpinnerSector(
            context.getString(R.string.left_leg_green),
            "L",
            false,
            Color.GREEN
        ),
        SpinnerSector(
            context.getString(R.string.left_leg_yellow),
            "L",
            false,
            Color.YELLOW
        ),

        SpinnerSector(
            context.getString(R.string.right_hand_red),
            "R",
            true,
            Color.RED
        ),
        SpinnerSector(
            context.getString(R.string.right_hand_blue),
            "R",
            true,
            Color.BLUE
        ),
        SpinnerSector(
            context.getString(R.string.right_hand_green),
            "R",
            true,
            Color.GREEN
        ),
        SpinnerSector(
            context.getString(R.string.right_hand_yellow),
            "R",
            true,
            Color.YELLOW
        ),

        SpinnerSector(
            context.getString(R.string.right_leg_red),
            "R",
            false,
            Color.RED
        ),
        SpinnerSector(
            context.getString(R.string.right_leg_blue),
            "R",
            false,
            Color.BLUE
        ),
        SpinnerSector(
            context.getString(R.string.right_leg_green),
            "R",
            false,
            Color.GREEN
        ),
        SpinnerSector(
            context.getString(R.string.right_leg_yellow),
            "R",
            false,
            Color.YELLOW
        )
    )

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 50f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }

    private var angle = 0f
    private var resultListener: ((String) -> Unit)? = null

    var isSpinning = false

    fun setOnResultListener(listener: (String) -> Unit) {
        resultListener = listener
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val size = min(width, height)
        val radius = size / 2f
        val rect = RectF(0f, 0f, size.toFloat(), size.toFloat())
        val sweepAngle = 360f / sectors.size
        val centerX = radius
        val centerY = radius

        canvas.save()
        canvas.rotate(angle, centerX, centerY)

        for (i in sectors.indices) {
            val sector = sectors[i]
            val startAngle = i * sweepAngle

            paint.color = sector.color
            canvas.drawArc(rect, startAngle, sweepAngle, true, paint)

            val midAngle = startAngle + sweepAngle / 2f
            val angleRad = Math.toRadians(midAngle.toDouble())

            val iconRadius = radius * 0.82f
            val textRadius = radius * 0.62f

            val iconX =
                centerX + (iconRadius * kotlin.math.cos(angleRad)).toFloat()
            val iconY =
                centerY + (iconRadius * kotlin.math.sin(angleRad)).toFloat()

            val textX =
                centerX + (textRadius * kotlin.math.cos(angleRad)).toFloat()
            val textY =
                centerY + (textRadius * kotlin.math.sin(angleRad)).toFloat()

            val bitmap = if (sector.isHand) {
                armBitmap
            } else {
                legBitmap
            }

            canvas.save()
            canvas.rotate(midAngle, iconX, iconY)

            val sizeBmp = radius * 0.22f

            val rectBmp = RectF(
                iconX - sizeBmp / 2,
                iconY - sizeBmp / 2,
                iconX + sizeBmp / 2,
                iconY + sizeBmp / 2
            )

            canvas.drawBitmap(bitmap, null, rectBmp, null)

            canvas.restore()

            canvas.save()
            canvas.rotate(midAngle, textX, textY)
            canvas.drawText(
                sector.side,
                textX,
                textY + 10f,
                textPaint
            )
            canvas.restore()
        }

        canvas.restore()

        paint.color = Color.BLACK

        val path = Path().apply {
            moveTo(width / 2f - 20, 0f)
            lineTo(width / 2f + 20, 0f)
            lineTo(width / 2f, 40f)
            close()
        }

        canvas.drawPath(path, paint)
    }

    fun spin() {
        if (isSpinning) return

        isSpinning = true

        val randomDegree = Random.nextInt(720, 1440)

        val animator = ValueAnimator.ofFloat(
            angle,
            angle + randomDegree
        ).apply {
            duration = 2000
            interpolator = android.view.animation.DecelerateInterpolator()

            addUpdateListener {
                angle = it.animatedValue as Float
                invalidate()
            }

            doOnEnd {
                isSpinning = false
                resultListener?.invoke(getResult())
            }
        }

        animator.start()
    }

    private fun getResult(): String {
        val sweep = 360f / sectors.size
        val normalized = (angle % 360 + 360) % 360
        val pointerAngle =
            (360f - normalized + 90f + 180f) % 360f

        val index = (pointerAngle / sweep).toInt()

        return sectors[index].label
    }
}
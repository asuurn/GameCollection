package ee.taltech.gamecollection

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

class GlowButtonContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var glowColor: Int = 0

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.GlowButtonContainer
        )

        glowColor = typedArray.getColor(
            R.styleable.GlowButtonContainer_glowColor,
            0
        )

        typedArray.recycle()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (glowColor != 0) {
            background = GlowDrawable(context, glowColor)
        }
    }

    override fun onMeasure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ) {
        setMeasuredDimension(
            MeasureSpec.getSize(widthMeasureSpec),
            MeasureSpec.getSize(heightMeasureSpec)
        )

        if (childCount > 0) {
            getChildAt(0).measure(
                MeasureSpec.makeMeasureSpec(
                    MeasureSpec.getSize(widthMeasureSpec),
                    MeasureSpec.EXACTLY
                ),
                MeasureSpec.makeMeasureSpec(
                    MeasureSpec.getSize(heightMeasureSpec),
                    MeasureSpec.EXACTLY
                )
            )
        }
    }

    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        if (childCount > 0) {
            getChildAt(0).layout(
                0,
                0,
                width,
                height
            )
        }
    }
}
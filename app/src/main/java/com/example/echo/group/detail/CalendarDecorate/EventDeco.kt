import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.style.LineBackgroundSpan
import androidx.fragment.app.Fragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import com.prolificinteractive.materialcalendarview.spans.DotSpan.DEFAULT_RADIUS
import java.nio.file.Files.size


class EventDeco(
    private val context: Context,
    private val stringProductColor: Array<String>,
    private val dates: CalendarDay
) : DayViewDecorator {

    private lateinit var colors: IntArray

    override fun shouldDecorate(day: CalendarDay): Boolean = dates == day

    override fun decorate(view: DayViewFacade) {


        colors = IntArray(stringProductColor.size)
        for (i in stringProductColor.indices) {
            colors[i] = Color.parseColor(stringProductColor[i])
        }

        view.addSpan(CustomMultipleDotSpan(5f, colors))

    }

    class CustomMultipleDotSpan : LineBackgroundSpan {
        private val radius: Float
        private var color = IntArray(0)

        constructor() {
            this.radius = DEFAULT_RADIUS
            this.color[0] = 0
        }

        constructor(color: Int) {
            this.radius = DEFAULT_RADIUS
            this.color[0] = 0
        }

        constructor(radius: Float) {
            this.radius = radius
            this.color[0] = 0
        }

        constructor(radius: Float, color: IntArray) {
            this.radius = radius
            this.color = color
        }


        override fun drawBackground(
            canvas: Canvas, paint: Paint,
            left: Int, right: Int, top: Int, baseline: Int, bottom: Int,
            charSequence: CharSequence,
            start: Int, end: Int, lineNum: Int
        ) {
            val total = if (color.size > 2) 3 else color.size
            var leftMost = (total - 1) * -12

            for (i in 0 until total) {
                val oldColor = paint.color
                if (color[i] != 0) {
                    paint.color = color[i]
                }
                canvas.drawCircle(
                    ((left + right) / 2 - leftMost).toFloat(),
                    bottom + radius,
                    radius,
                    paint
                )
                paint.color = oldColor
                leftMost += 24
            }
        }

    }
}
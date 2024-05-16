package viewmodel

import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.unit.IntSize
import kotlin.math.sign
import kotlin.properties.Delegates

@Stable
class GraphVM {
    var height by mutableStateOf(100)
    private var width by mutableStateOf(100)
    var mainOffset by mutableStateOf(Offset(x = 0f, y = 0f))
    val scaleFactor = mutableStateOf(1f)

    var padding by Delegates.notNull<Int>()

    var toAbsoluteOffset by mutableStateOf(
        { offset: Offset ->
            Offset(
                x = padding + offset.x * (width - 2 * padding) / 2 + (width - 2 * padding) / 2,
                y = padding + offset.y * (height - 2 * padding) / 2 + (height - 2 * padding) / 2
            )
        }
    )

    var toNotAbsoluteOffset by mutableStateOf(
        { offset: Offset ->
            Offset(
                x = (offset.x - padding - (width - 2 * padding) / 2) / (width - 2 * padding) * 2,
                y = (offset.y - padding - (height - 2 * padding) / 2) / (height - 2 * padding) * 2
            )
        }
    )

    fun onBoxSizeChanged(coordinates: IntSize) {
        height = coordinates.height
        width = coordinates.width

        toAbsoluteOffset = { offset: Offset ->
            Offset(
                x = padding + offset.x * (width - 2 * padding) / 2 + (width - 2 * padding) / 2,
                y = padding + offset.y * (height - 2 * padding) / 2 + (height - 2 * padding) / 2
            )
        }
        toNotAbsoluteOffset = { offset: Offset ->
            Offset(
                x = (offset.x - padding - (width - 2 * padding) / 2) / (width - 2 * padding) * 2,
                y = (offset.y - padding - (height - 2 * padding) / 2) / (height - 2 * padding) * 2
            )
        }
    }

    fun onMouseScroll(pointerEvent: PointerEvent) {
        val change = pointerEvent.changes.first()
        val where = toNotAbsoluteOffset(change.position)
        val delta = change.scrollDelta.y.toInt().sign
        val zoomVal = scaleFactor.value + delta * 0.1f
        if (zoomVal < 0.5f || zoomVal > 3.0f) return
        scaleFactor.value = zoomVal
    }

}
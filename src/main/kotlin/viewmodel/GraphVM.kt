package viewmodel

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.unit.IntSize
import kotlin.math.sign

@Stable
class GraphVM {
    var height by mutableStateOf(100)
    private var width by mutableStateOf(100)
    var mainOffset by mutableStateOf(Offset(x = 0f, y = 0f))
    val scaleFactor = mutableStateOf(1f)

    var padding = 100

    var toAbsoluteOffset by mutableStateOf(
        { offset: Offset ->
            Offset(
                x = padding + offset.x * scaleFactor.value * (width - 2 * padding) / 2 + (width - 2 * padding) / 2,
                y = padding + offset.y * scaleFactor.value * (height - 2 * padding) / 2 + (height - 2 * padding) / 2
            )
        }
    )

    var toNotAbsoluteOffset by mutableStateOf(
        { offset: Offset ->
            Offset(
                x = (offset.x - padding - (width - 2 * padding) / 2) / (width - 2 * padding) * 2 / scaleFactor.value,
                y = (offset.y - padding - (height - 2 * padding) / 2) / (height - 2 * padding) * 2 / scaleFactor.value
            )
        }
    )

    fun recalc() {
        toAbsoluteOffset = { offset: Offset ->
                Offset(
                    x = padding + offset.x * scaleFactor.value * (width - 2 * padding) / 2 + (width - 2 * padding) / 2,
                    y = padding + offset.y * scaleFactor.value * (height - 2 * padding) / 2 + (height - 2 * padding) / 2
                )
            }

        toNotAbsoluteOffset = { offset: Offset ->
                Offset(
                    x = (offset.x - padding - (width - 2 * padding) / 2) / (width - 2 * padding) * 2 / scaleFactor.value,
                    y = (offset.y - padding - (height - 2 * padding) / 2) / (height - 2 * padding) * 2 / scaleFactor.value
                )
            }
    }

    fun onBoxSizeChanged(coordinates: IntSize) {
        height = coordinates.height
        width = coordinates.width

        this.recalc()
    }

    fun onMouseScroll(pointerEvent: PointerEvent) {
        val change = pointerEvent.changes.first()
        val delta = change.scrollDelta.y.toInt().sign
        val zoomVal = scaleFactor.value + delta * 0.1f
        if (zoomVal < 0.001f || zoomVal > 300.0f) return
        scaleFactor.value = zoomVal

        this.recalc()
    }

}
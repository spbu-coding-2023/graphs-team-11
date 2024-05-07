package viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import kotlin.properties.Delegates

class GraphVM<D> {
    private var height by mutableStateOf(0)
    private var width by mutableStateOf(0)
    var mainOffset by mutableStateOf(Offset(x = 0f, y = 0f))

    var padding by Delegates.notNull<Int>()

    var toAbsoluteOffset = { offset: Offset ->
        Offset(
            x = padding + offset.x * (width - 2 * padding) / 2 + (width - 2 * padding) / 2,
            y = padding + offset.y * (height - 2 * padding) / 2 + (height - 2 * padding) / 2
        )
    }

    var toNotAbsoluteOffset = { offset: Offset ->
        Offset(
            x = (offset.x - padding - (width - 2 * padding) / 2) / (width - 2 * padding) * 2,
            y = (offset.y - padding - (height - 2 * padding) / 2) / (height - 2 * padding) * 2
        )
    }

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

}
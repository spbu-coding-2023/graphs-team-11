/*
 *
 *  * This file is part of BDSM Graphs.
 *  *
 *  * BDSM Graphs is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * BDSM Graphs is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with . If not, see <https://www.gnu.org/licenses/>.
 *
 */

package viewmodel

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.unit.IntSize
import java.awt.Toolkit
import kotlin.math.sign

@Stable
class GraphVM {
    var height by mutableStateOf(100)
    private var width by mutableStateOf(100)
    var mainOffset by mutableStateOf(Offset(x = 0f, y = 0f))
    val scaleFactor = mutableStateOf(1f)

    var padding = 100

    var toAbsoluteOffset by mutableStateOf({ offset: Offset ->
        Offset(
            x = padding + offset.x * scaleFactor.value * (width - 2 * padding) / 2 + (width - 2 * padding) / 2,
            y = padding + offset.y * scaleFactor.value * (height - 2 * padding) / 2 + (height - 2 * padding) / 2
        )
    })

    var toNotAbsoluteOffset by mutableStateOf({ offset: Offset ->
        Offset(
            x = (offset.x - padding - (width - 2 * padding) / 2) / (width - 2 * padding) * 2 / scaleFactor.value,
            y = (offset.y - padding - (height - 2 * padding) / 2) / (height - 2 * padding) * 2 / scaleFactor.value
        )
    })

    private fun recalc() {
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
        val oldZoom = scaleFactor.value
        val newZoom = (scaleFactor.value - delta * 0.05f).coerceIn(0.0003f, 3000.0f)
        scaleFactor.value = newZoom


        val (mouseX, mouseY) = change.position
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val (width, height) = screenSize.getWidth() to screenSize.getHeight()

        val pixelsDifferenceW = (width / oldZoom) - (width / newZoom)
        val sideRatioX = (mouseX - (width / 2)) / width
        val newMainOffsetX = mainOffset.x + pixelsDifferenceW * sideRatioX

        val pixelsDifferenceH = (height / oldZoom) - (height / newZoom)
        val sideRatioY = (mouseY - (height / 2)) / height
        val newMainOffsetY = mainOffset.y + pixelsDifferenceH * sideRatioY

        mainOffset = Offset(newMainOffsetX.toFloat(), newMainOffsetY.toFloat())

        recalc()
    }
}

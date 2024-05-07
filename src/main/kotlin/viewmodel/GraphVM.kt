package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.IntSize
import model.graph_model.GrahpViewClass
import kotlin.properties.Delegates

class GraphVM<D> {
    lateinit var gv: GrahpViewClass<D>
    private var released by mutableStateOf(true)
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

    fun onBoxHotkeys(event: KeyEvent, changedAlgo: MutableState<Boolean>): Boolean {
        if (event.type == KeyEventType.KeyDown) {
            when {
                (event.isMetaPressed && event.key == Key.Z && released) -> {
                    released = false
                    gv.comeBack()
                    changedAlgo.value = true
                    return true
                }

                event.key == Key.C && event.isShiftPressed && event.isMetaPressed -> {
                    // CTRL + C hotkey action
                    println("CTRL + SHIFT + C")
                    return true
                }

                event.key == Key.C && event.isMetaPressed -> {
                    // CTRL + C hotkey action
                    println("CTRL + C")
                    return true
                }

                event.key == Key.Z && event.isMetaPressed -> {
                    // CTRL + Z hotkey action
                    println("CTRL + Z")
                    return true
                }

                event.key == Key.N && event.isMetaPressed -> {
                    // CTRL + N hotkey action
                    println("CTRL + N")
                    return true
                }

                else -> return false
            }
        } else {
            released = true
            return false
        }
    }

}
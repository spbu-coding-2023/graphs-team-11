package ui.graph_view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity

@Composable
@Preview
fun <D> GrahpView(
    gv: GrahpViewClass<D>, showNodes: Boolean = true, changedAlgo: MutableState<Boolean>, pading: Int = 30
) {
    if (changedAlgo.value) {
        changedAlgo.value = false
    }
    var mainOffset by remember { mutableStateOf(Offset(x = 0f, y = 0f)) }

    val localDensity = LocalDensity.current
    var height by remember { mutableStateOf(844) }
    var width by remember { mutableStateOf(834) }

    var toAbsoluteOffset = { offset: Offset ->
        Offset(
            x = pading + offset.x * (width - 2 * pading) / 2 + (width - 2 * pading) / 2,
            y = pading + offset.y * (height - 2 * pading) / 2 + (height - 2 * pading) / 2
        )
    }
    var toNotAbsoluteOffset = { offset: Offset ->
        Offset(
            x = (offset.x - pading - (width - 2 * pading) / 2) / (width - 2 * pading) * 2,
            y = (offset.y - pading - (height - 2 * pading) / 2) / (height - 2 * pading) * 2
        )
    }
    // Idk why exactly this formula, but it works
    val sensivity by remember { mutableStateOf(0.2f / gv.nodesViews.size) }

    BoxWithConstraints(modifier = Modifier.fillMaxSize().onSizeChanged { coordinates ->
        height = with(localDensity) { coordinates.height }
        width = with(localDensity) { coordinates.width }

        toAbsoluteOffset = { offset: Offset ->
            Offset(
                x = pading + offset.x * (width - 2 * pading) / 2 + (width - 2 * pading) / 2,
                y = pading + offset.y * (height - 2 * pading) / 2 + (height - 2 * pading) / 2
            )
        }
        toNotAbsoluteOffset = { offset: Offset ->
            Offset(
                x = (offset.x - pading - (width - 2 * pading) / 2) / (width - 2 * pading) * 2,
                y = (offset.y - pading - (height - 2 * pading) / 2) / (height - 2 * pading) * 2
            )
        }


    }) {

        Canvas(modifier = Modifier.fillMaxSize().pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                change.consume()
                for (nodeView in gv.nodesViews) {
                    mainOffset -= dragAmount * sensivity
                }
            }
        }) {
            for (i in gv.vertViews) {

                drawLine(
                    color = i.color,
                    start = toAbsoluteOffset(i.start.offset) + Offset(
                        x = i.start.radius, y = i.start.radius
                    ) - mainOffset,
                    end = toAbsoluteOffset(i.end.offset) + Offset(x = i.end.radius, y = i.end.radius) - mainOffset,
                    alpha = i.alpha,
                )
                // println(i.start.offset)
                // println(i.end.offset)
            }
        }
        if (showNodes) {
            for (i in gv.nodesViews) {
                NodeView(
                    i.value,
                    mainOffset = mainOffset,
                    toAbsoluteOffset = toAbsoluteOffset,
                    toNotAbsoluteOffset = toNotAbsoluteOffset
                )
                // println(Pair(width, height))
            }
        }
    }
}
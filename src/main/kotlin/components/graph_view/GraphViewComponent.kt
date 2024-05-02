package components.graph_view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput

@Composable
@Preview
fun <D> GrahpViewComponent(gv: GrahpView<D>, showNodes: Boolean = true) {

    var mainOffset by remember { mutableStateOf(Offset(x = 0f, y = 0f)) }

    var gv by remember { mutableStateOf(gv) }

    // Idk why exactly this formula, but it works
    var sensivity by remember { mutableStateOf(0.2f / gv.nodesViews.size) }

    Box(modifier = Modifier.fillMaxSize()) {

        Canvas(modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
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
                    start = i.start.offset + Offset(x = i.start.radius, y = i.start.radius) - mainOffset,
                    end = i.end.offset + Offset(x = i.end.radius, y = i.start.radius) - mainOffset,
                    alpha = i.alpha
                )
                // println(i.start.offset)
                // println(i.end.offset)
            }
        }
        if (showNodes) {
            for (i in gv.nodesViews) {
                NodeViewComponent(i.value, mainOffset = mainOffset)
            }
        }
    }
}
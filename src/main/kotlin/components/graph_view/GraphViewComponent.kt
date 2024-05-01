package components.graph_view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import data.Graph
import kotlin.math.roundToInt

@Composable
@Preview
fun <D> GrahpViewComponent(graph: Graph<D>) {

    var gv = GrahpView<D>(graph)

    var selected = -1

    Box(modifier = Modifier.fillMaxSize()) {

        Canvas(modifier = Modifier.fillMaxSize()) {
            for (i in gv.vertViews) {
                drawLine(
                    color = i.color,
                    start = i.start.offset + Offset(x = i.start.radius, y = i.start.radius),
                    end = i.end.offset + Offset(x = i.end.radius, y = i.end.radius)
                )
            }
        }

        for (i in gv.nodesViews) {
            NodeViewComponent(i.value)
        }
    }
}
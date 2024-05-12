package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import model.graph_model.NodeViewClass
import kotlin.math.roundToInt

@Composable
@Preview
fun <D> NodeView(
    nodeView: NodeViewClass<D>,
    mainOffset: Offset,
    toAbsoluteOffset: (Offset) -> Offset,
    toNotAbsoluteOffset: (Offset) -> Offset,
    selected: SnapshotStateMap<D, Boolean>,
    isShifted: MutableState<Boolean>
) {


    var offset by remember { mutableStateOf(toAbsoluteOffset(nodeView.offset)) }

    Box(
        Modifier
            .offset { IntOffset((offset.x - mainOffset.x).roundToInt(), (offset.y - mainOffset.y).roundToInt()) }
            .background(
                nodeView.color,
                shape = if (selected.getOrDefault(nodeView.value, false)) RectangleShape else CircleShape
            )
            .size(nodeView.radius.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()

                    // println(offset)
                    // println(toAbsoluteOffset(nodeView.offset))

                    offset += dragAmount

                    nodeView.offset = toNotAbsoluteOffset(offset)
                    // println(toNotAbsoluteOffset(Offset(x = width / 2f, y = height / 2f)))
                    // println(nodeView.offset)
                }
            }
            .onPlaced { offset = toAbsoluteOffset(nodeView.offset) }
            .selectable(selected.getOrDefault(nodeView.value, false)) {
                if (!isShifted.value) {
                    selected.forEach { selected.remove(it.key) }
                    selected[nodeView.value] = true
                } else {
                    selected[nodeView.value] = !selected.getOrDefault(nodeView.value, false)
                }
                println(isShifted.value)
            }, contentAlignment = Alignment.Center
    ) {
        Text(
            text = nodeView.value.toString(),
            color = Color.Red,
            textAlign = TextAlign.Center,
            modifier = Modifier.wrapContentSize(unbounded = true)
        )
    }
}
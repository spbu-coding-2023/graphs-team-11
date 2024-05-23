package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import model.graph_model.NodeViewClass
import kotlin.math.roundToInt

@Composable
fun <D> NodeView(
    nodeView: NodeViewClass<D>,
    mainOffset: Offset,
    toAbsoluteOffset: (Offset) -> Offset,
    toNotAbsoluteOffset: (Offset) -> Offset,
    selected: SnapshotStateMap<D, Int>,
    isShifted: MutableState<Boolean>,
    scaleFactor: Float
) {
    var offset by remember { mutableStateOf(toAbsoluteOffset(nodeView.offset)) }

    // println(offset)

    Box(Modifier.offset {
        IntOffset(
            ((offset.x - mainOffset.x)).roundToInt(),
            ((offset.y - mainOffset.y)).roundToInt()
        )
    }.background(
        MaterialTheme.colors.background,
        shape = CircleShape,
    ).border(
        if (selected.getOrDefault(nodeView.value, -1) >= 0) 4.dp else 2.dp,
        color = nodeView.color,
        shape = CircleShape,

    ).size((nodeView.radius).dp).pointerInput(Unit) {
        detectDragGestures { change, dragAmount ->
            change.consume()
            // there is a problem with the offset calculation
            offset += dragAmount / scaleFactor
            nodeView.offset = toNotAbsoluteOffset(offset)
        }
    }.onPlaced { offset = toAbsoluteOffset(nodeView.offset) }
        .selectable(selected.getOrDefault(nodeView.value, -1) >= 0) {
            if (nodeView.value != null) {
                val value = nodeView.value!!
                if (!isShifted.value) {
                    selected.forEach { selected.remove(it.key) }
                    selected[value] = selected.size
                } else {
                    if (value in selected) {selected.remove(value)}
                    else selected[value] = selected.size
                }
            }
        }.zIndex(0f)
        , contentAlignment = Alignment.Center
    ) {
        if (nodeView.value != null) {
            Text(
                text = nodeView.value.toString(),
                color = nodeView.color,
                textAlign = TextAlign.Center,
                modifier = Modifier.wrapContentSize(unbounded = true).zIndex(1f)
            )
        } else {
            val newValue = remember { mutableStateOf("") }

            TextField(

                label = null,
                textStyle = TextStyle(fontSize=16.sp, textAlign = TextAlign.Center, color = Color.Red),
                value = newValue.value,
                onValueChange = { text ->
                    if (!text.endsWith("\n")) {
                        newValue.value = text
                    }
                    else {
                        try {
                            nodeView.value = newValue.value as D
                        } catch (classCastException: ClassCastException) {
                            try {
                                nodeView.value = newValue.value.toInt() as D
                            } catch (classCastException: ClassCastException) {
                            }
                        }
                    }
                },
                modifier = Modifier
                    .wrapContentSize(unbounded = true)
                    .fillMaxWidth()
                    .zIndex(-1f),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedLabelColor = Color.Transparent
                )
            )
        }
    }
}
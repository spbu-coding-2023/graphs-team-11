package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
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
import viewmodel.GraphVM
import viewmodel.NodeVM
import kotlin.math.roundToInt

@Composable
fun NodeView(
    nodeView: NodeViewClass,
    selected: SnapshotStateMap<String, Int>,
    isShifted: MutableState<Boolean>,
    graphVM: GraphVM,
    graphNodeKeysList: List<String>,
    changedAlgo: MutableState<Boolean>,
) {
    val viewModel = remember { NodeVM() }
    var offset by remember { mutableStateOf(graphVM.toAbsoluteOffset(nodeView.offset)) }

    Column(modifier = Modifier.offset {
        IntOffset(
            ((offset.x - graphVM.mainOffset.x)).roundToInt(), ((offset.y - graphVM.mainOffset.y)).roundToInt()
        )
    }) {

        if (viewModel.showDuplicateError.value) {
            Text(text = "Duplicate node name", color = Color.Red, modifier = Modifier.offset(x = (-70).dp, y = (-5).dp))
        }

        Box(Modifier.background(
            MaterialTheme.colors.background,
            shape = CircleShape,
        ).border(
            if (selected.getOrDefault(nodeView.value, -1) >= 0) 4.dp else 2.dp,
            color = nodeView.color,
            shape = nodeView.shape,

            ).size((nodeView.radius).dp).pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                change.consume()
                // there is a problem with the offset calculation
                offset += dragAmount / graphVM.scaleFactor.value
                nodeView.offset = graphVM.toNotAbsoluteOffset(offset)
            }
        }.onPlaced { offset = graphVM.toAbsoluteOffset(nodeView.offset) }
            .selectable(selected.getOrDefault(nodeView.value, -1) >= 0) {
                viewModel.onNodeSelected(nodeView, selected, isShifted)
            }.zIndex(0f), contentAlignment = Alignment.Center
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
                    textStyle = TextStyle(fontSize = 16.sp, textAlign = TextAlign.Center, color = Color.Red),
                    value = newValue.value,
                    modifier = Modifier.wrapContentSize(unbounded = true).fillMaxWidth().zIndex(-1f)
                        .widthIn(max = viewModel.textFieldLength.dp),
                    onValueChange = { text ->
                        viewModel.onTextFieldTextChanged(
                            text, newValue, graphNodeKeysList, viewModel.showDuplicateError, nodeView, changedAlgo
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedLabelColor = Color.Transparent
                    )
                )
            }
        }
    }
}
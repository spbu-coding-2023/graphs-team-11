package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.onClick
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import model.graph_model.GrahpViewClass
import viewmodel.GraphVM

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun <D> GrahpView(
    gv: GrahpViewClass<D>,
    changedAlgo: MutableState<Boolean>,
    selected: SnapshotStateMap<D, Boolean>,
    padding: Int = 30,
    showNodes: Boolean = true
) {
    val viewModel = remember { GraphVM() }
    viewModel.padding = padding

    if (changedAlgo.value) {
        changedAlgo.value = false
    }

    val toAbsoluteOffset = viewModel.toAbsoluteOffset
    val toNotAbsoluteOffset = viewModel.toNotAbsoluteOffset
    val sensitivity by mutableStateOf(0.2f / gv.nodesViews.size)

    val isShifted = mutableStateOf(false)

    var expanded by remember { mutableStateOf(false) }
    var offsetMenu by remember { mutableStateOf(DpOffset(x = 0.dp, y = 0.dp)) }

    DropdownMenu(
        offset = offsetMenu,
        expanded = expanded,
        onDismissRequest = { expanded = false }

    ) {
        DropdownMenuItem(
            content = {  Text("Refresh") },
            onClick = { /* Handle refresh! */ }
        )
        DropdownMenuItem(
            content = { Text("Settings") },
            onClick = { /* Handle settings! */ }
        )
        Divider()
        DropdownMenuItem(
            content = { Text("Send Feedback") },
            onClick = { /* Handle send feedback! */ }
        )
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize().onSizeChanged { coordinates ->
            viewModel.onBoxSizeChanged(coordinates)
        }.background(MaterialTheme.colors.background)
            .onKeyEvent { keyEvent ->
                isShifted.value = keyEvent.isShiftPressed
                false
            }
            .onClick(
                matcher = PointerMatcher.mouse(PointerButton.Secondary),
                onClick = {
                    expanded = true

                }
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize().pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                change.consume()
                for (nodeView in gv.nodesViews) {
                    viewModel.mainOffset -= dragAmount * sensitivity
                }
            }
        }) {
            for ((i, verts) in gv.vertViews) {
                for ((j, view) in verts) {
                    drawLine(
                        color = view.color,
                        start = toAbsoluteOffset(view.start.offset) + Offset(
                            x = view.start.radius, y = view.start.radius
                        ) - viewModel.mainOffset,
                        end = toAbsoluteOffset(view.end.offset) + Offset(
                            x = view.end.radius, y = view.end.radius
                        ) - viewModel.mainOffset,
                        alpha = view.alpha,
                    )
                    // println(i.start.offset)
                    // println(i.end.offset)
                }
            }
        }
        if (showNodes) {
            for (i in gv.nodesViews) {
                NodeView(
                    nodeView = i.value,
                    mainOffset = viewModel.mainOffset,
                    toAbsoluteOffset = toAbsoluteOffset,
                    toNotAbsoluteOffset = toNotAbsoluteOffset,
                    selected = selected,
                    isShifted = isShifted
                )
                // println(Pair(width, height))
            }
        }
    }
}
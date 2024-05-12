package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import model.graph_model.GrahpViewClass
import viewmodel.GraphVM

@Composable
@Preview
fun <D> GrahpView(
    gv: GrahpViewClass<D>, changedAlgo: MutableState<Boolean>, selected: SnapshotStateMap<D, Boolean>, padding: Int = 30, showNodes: Boolean = true
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

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize().onSizeChanged { coordinates ->
            viewModel.onBoxSizeChanged(coordinates)
        }.background(MaterialTheme.colors.background)
            .onKeyEvent { keyEvent ->
                isShifted.value = keyEvent.isShiftPressed
                false
            }
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
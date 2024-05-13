package ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import model.graph_model.GraphViewClass
import viewmodel.GraphVM

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun <D> GrahpView(
    gv: GraphViewClass<D>,
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
    val interactionSource = remember { MutableInteractionSource() }

    BoxWithConstraints(modifier = Modifier.fillMaxSize().onSizeChanged { coordinates ->
        viewModel.onBoxSizeChanged(coordinates)
    }.background(MaterialTheme.colors.background).onKeyEvent { keyEvent ->
        isShifted.value = keyEvent.isShiftPressed
        false
    }.clickable(interactionSource = interactionSource, indication = null, onClick = {
        if (selected.isNotEmpty() && !isShifted.value) {
            selected.forEach { selected.remove(it.key) }
        }
    })) {

        Canvas(modifier = Modifier.fillMaxSize().pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                change.consume()
                for (nodeView in gv.nodesViews) {
                    viewModel.mainOffset -= dragAmount * sensitivity
                }
            }

        }.onPointerEvent(PointerEventType.Scroll) {
            viewModel.onMouseScroll(it)

        }) {
            for ((i, verts) in gv.vertViews) {
                for ((j, view) in verts) {
                    drawLine(
                        color = view.color,
                        start = (toAbsoluteOffset(view.start.offset) + Offset(
                            x = view.start.radius, y = view.start.radius
                        ) - viewModel.mainOffset) * viewModel.scaleFactor.value,
                        end = (toAbsoluteOffset(view.end.offset) + Offset(
                            x = view.end.radius, y = view.end.radius
                        ) - viewModel.mainOffset) * viewModel.scaleFactor.value,
                        alpha = view.alpha,
                    )
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
                    isShifted = isShifted,
                    scaleFactor = viewModel.scaleFactor.value
                )
            }
        }
    }
}
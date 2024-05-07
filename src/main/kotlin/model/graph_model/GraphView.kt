package model.graph_model

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import viewmodel.GraphVM

@Composable
@Preview
fun <D> GrahpView(
    gv: GrahpViewClass<D>, showNodes: Boolean = true, changedAlgo: MutableState<Boolean>, padding: Int = 30
) {
    val viewModel = remember { GraphVM<D>() }
    viewModel.padding = padding

    if (changedAlgo.value) {
        changedAlgo.value = false
    }

    val toAbsoluteOffset = viewModel.toAbsoluteOffset
    val toNotAbsoluteOffset = viewModel.toNotAbsoluteOffset
    val sensitivity by mutableStateOf(0.2f / gv.nodesViews.size)

    BoxWithConstraints(modifier = Modifier.fillMaxSize().onSizeChanged { coordinates ->
        viewModel.onBoxSizeChanged(coordinates)
    }) {

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
                    i.value,
                    mainOffset = viewModel.mainOffset,
                    toAbsoluteOffset = toAbsoluteOffset,
                    toNotAbsoluteOffset = toNotAbsoluteOffset
                )
                // println(Pair(width, height))
            }
        }
    }
}
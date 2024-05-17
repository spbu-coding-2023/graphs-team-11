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
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import model.graph_model.GraphViewClass
import model.graph_model.NodeViewClass
import model.graph_model.abs
import viewmodel.GraphVM

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun <D> GrahpView(
    gv: GraphViewClass<D>,
    changedAlgo: MutableState<Boolean>,
    selected: SnapshotStateMap<D, Int>,
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
    val sensitivity by mutableStateOf(0.2f)

    val isShifted = mutableStateOf(false)
    val interactionSource = remember { MutableInteractionSource() }

    val textMeasurer = rememberTextMeasurer()

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
                    viewModel.mainOffset -= dragAmount * sensitivity
            }

        }.onPointerEvent(PointerEventType.Scroll) {
            viewModel.onMouseScroll(it)
            changedAlgo.value = true

        }.onPointerEvent(PointerEventType.Press) {
            selected.forEach { print(Pair(it.key, it.value))}
            println()
            val selectedList = mutableMapOf<Int, D>()
            for ((i, isSel) in selected) {
                selectedList[isSel] = i
            }
            if (it.button == PointerButton.Secondary) {
                if (selectedList.size == 0) {
                    gv.addNode(null, toNotAbsoluteOffset(it.changes.first.position))
                    changedAlgo.value = true
                } else if (selectedList.size == 2) {
                    gv.addVert(selectedList[0]!!, selectedList[1]!!)
                    changedAlgo.value = true
                }
            }
        }
        ) {
            for ((i, verts) in gv.vertViews) {
                for ((j, view) in verts) {
                    val arrow = Path()
                    val start = (viewModel.toAbsoluteOffset(view.start.offset) + Offset(
                        x = view.start.radius, y = view.start.radius
                    ) * 0.5f - viewModel.mainOffset)

                    val end = (viewModel.toAbsoluteOffset(view.end.offset) + Offset(
                        x = view.end.radius, y = view.end.radius
                    ) * 0.5f - viewModel.mainOffset)

                    println(Pair(start, end))


                    val d = - (end - start) / abs(end - start)

                    // Algebra Time!!!
                    arrow.moveTo((end + d * (view.end.radius / 2f + 10f)).x, (end + d * (view.end.radius / 2f + 10f)).y)
                    arrow.lineTo((end + d * (view.end.radius / 2f + 10f)).x + 10f * d.y, (end + d * (view.end.radius / 2f + 10f)).y - 10f * d.x)
                    arrow.lineTo((end + d * (view.end.radius / 2f + 10f)).x - 10f * d.x, (end + d * (view.end.radius / 2f + 10f)).y - 10f * d.y)
                    arrow.lineTo((end + d * (view.end.radius / 2f + 10f)).x - 10f * d.y , (end + d * (view.end.radius / 2f + 10f)).y + 10f * d.x)
                    arrow.close()

                    drawPath(arrow, color = view.color, alpha = view.alpha)

                    drawLine(
                        color = view.color,
                        start = start,
                        end = end,
                        alpha = view.alpha,
                    )

                    if (view.weight != 1f) {
                        drawText(
                            textMeasurer,
                            text = view.weight.toString(),
                            topLeft = (start + end) / 2f,
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = view.color
                            )
                        )
                    }
                }
            }
        }
        if (showNodes) {
            for (i in gv.nodesViews) {
                NodeView(
                    nodeView = i.value,
                    mainOffset = viewModel.mainOffset,
                    toAbsoluteOffset = viewModel.toAbsoluteOffset,
                    toNotAbsoluteOffset = viewModel.toNotAbsoluteOffset,
                    selected = selected,
                    isShifted = isShifted,
                    scaleFactor = viewModel.scaleFactor.value
                )
            }
            val toRemove = mutableListOf<NodeViewClass<D>>()
            for (i in gv.newNodes) {
                if (i.value != null) {
                    val value = i.value!!
                    gv.nodesViews[value] = i
                    gv.graph.addNode(value)
                    changedAlgo.value = true
                    toRemove.add(i)
                    continue
                }
                NodeView(
                    nodeView = i,
                    mainOffset = viewModel.mainOffset,
                    toAbsoluteOffset = viewModel.toAbsoluteOffset,
                    toNotAbsoluteOffset = viewModel.toNotAbsoluteOffset,
                    selected = selected,
                    isShifted = isShifted,
                    scaleFactor = viewModel.scaleFactor.value
                )
            }
            for (i in toRemove) {
                gv.newNodes.remove(i)
            }
        }
    }
}
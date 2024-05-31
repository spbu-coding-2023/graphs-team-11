/*
 *
 *  * This file is part of BDSM Graphs.
 *  *
 *  * BDSM Graphs is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * BDSM Graphs is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with . If not, see <https://www.gnu.org/licenses/>.
 *
 */

package ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.graph_model.GraphViewClass
import model.graph_model.NodeViewClass
import model.graph_model.UndirectedGraph
import model.graph_model.abs
import viewmodel.GraphVM

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GraphView(
    gv: GraphViewClass,
    changedAlgo: MutableState<Boolean>,
    selected: SnapshotStateMap<String, Int>,
    padding: Int = 100,
    showNodes: Boolean = true,
) {
    val viewModel = remember { GraphVM() }
    viewModel.padding = padding

    if (changedAlgo.value) {
        changedAlgo.value = false
    }

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
            val selectedList = mutableMapOf<Int, String>()
            for ((i, isSel) in selected) {
                selectedList[isSel] = i
            }
            if (it.button == PointerButton.Secondary) {
                if (selectedList.size < 2) {
                    if (!gv.newNodes.any { node -> node.value == null }) {
                        gv.addNode(null, toNotAbsoluteOffset(it.changes.first().position))
                        changedAlgo.value = true
                    }
                } else if (selectedList.size == 2) {
                    gv.addVert(selectedList[0]!!, selectedList[1]!!)
                    changedAlgo.value = true
                }
            }
        }) {
            for ((_, verts) in gv.vertViews) {
                for ((_, view) in verts) {
                    val arrow = Path()
                    val start = (viewModel.toAbsoluteOffset(view.start.offset) + Offset(
                        x = view.start.radius.dp.toPx(), y = view.start.radius.dp.toPx()
                    ) / 2f - viewModel.mainOffset)

                    val end = (viewModel.toAbsoluteOffset(view.end.offset) + Offset(
                        x = view.end.radius.dp.toPx(), y = view.end.radius.dp.toPx()
                    ) / 2f - viewModel.mainOffset)

                    if (gv.graph !is UndirectedGraph) {
                        val d = -(end - start) / abs(end - start)

                        arrow.moveTo(
                            (end + d * (view.end.radius.dp.toPx() / 2f + 10f)).x,
                            (end + d * (view.end.radius.dp.toPx() / 2f + 10f)).y
                        )
                        arrow.lineTo(
                            (end + d * (view.end.radius.dp.toPx() / 2f + 10f)).x + 10f * d.y,
                            (end + d * (view.end.radius.dp.toPx() / 2f + 10f)).y - 10f * d.x
                        )
                        arrow.lineTo(
                            (end + d * (view.end.radius.dp.toPx() / 2f + 10f)).x - 10f * d.x,
                            (end + d * (view.end.radius.dp.toPx() / 2f + 10f)).y - 10f * d.y
                        )
                        arrow.lineTo(
                            (end + d * (view.end.radius.dp.toPx() / 2f + 10f)).x - 10f * d.y,
                            (end + d * (view.end.radius.dp.toPx() / 2f + 10f)).y + 10f * d.x
                        )
                        arrow.close()

                        drawPath(arrow, color = view.color, alpha = view.alpha)
                    }

                    drawLine(
                        color = view.color,
                        start = start,
                        end = end,
                        alpha = view.alpha,
                    )

                    val textPosition = (start + end) / 2f

                    if ((view.weight != 1f) && (textPosition.x > 0) && (textPosition.y > 0) && (textPosition.x < size.width) && (textPosition.y < size.height)) {
                        drawText(
                            textMeasurer,
                            text = view.weight.toString(),
                            topLeft = textPosition,
                            softWrap = false,
                            style = TextStyle(
                                fontSize = 16.sp, color = view.color
                            )
                        )
                    }
                }
            }
        }
        if (showNodes) {
            val graphNodeKeysList = gv.nodesViews.keys.map { it }
            for (i in gv.nodesViews) {
                NodeView(
                    nodeView = i.value,
                    selected = selected,
                    isShifted = isShifted,
                    viewModel,
                    graphNodeKeysList,
                    changedAlgo
                )
            }
            val toRemove = mutableListOf<NodeViewClass>()
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
                    nodeView = i, selected = selected, isShifted = isShifted, viewModel, graphNodeKeysList, changedAlgo
                )

            }
            for (i in toRemove) {
                gv.newNodes.remove(i)
            }
        }
    }
}
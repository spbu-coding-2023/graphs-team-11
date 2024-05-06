package ui.graph_view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.processNextEventInCurrentThread

@Composable
@Preview
fun <D> GrahpView(
    gv: GrahpViewClass<D>, showNodes: Boolean = true, changedAlgo: MutableState<Boolean>, pading: Int = 30
) {
    if (changedAlgo.value) {
        changedAlgo.value = false
    }
    var mainOffset by remember { mutableStateOf(Offset(x = 0f, y = 0f)) }

    val requester = remember { FocusRequester() }
    var relesed by remember { mutableStateOf(true) }

    val localDensity = LocalDensity.current
    var height by remember { mutableStateOf(844) }
    var width by remember { mutableStateOf(834) }

    var toAbsoluteOffset = { offset: Offset ->
        Offset(
            x = pading + offset.x * (width - 2 * pading) / 2 + (width - 2 * pading) / 2,
            y = pading + offset.y * (height - 2 * pading) / 2 + (height - 2 * pading) / 2
        )
    }
    var toNotAbsoluteOffset = { offset: Offset ->
        Offset(
            x = (offset.x - pading - (width - 2 * pading) / 2) / (width - 2 * pading) * 2,
            y = (offset.y - pading - (height - 2 * pading) / 2) / (height - 2 * pading) * 2
        )
    }
    // Idk why exactly this formula, but it works
    val sensivity by remember { mutableStateOf(0.2f / gv.nodesViews.size) }

    BoxWithConstraints(modifier = Modifier
        .fillMaxSize()
        .onSizeChanged { coordinates ->
        height = with(localDensity) { coordinates.height }
        width = with(localDensity) { coordinates.width }

        toAbsoluteOffset = { offset: Offset ->
            Offset(
                x = pading + offset.x * (width - 2 * pading) / 2 + (width - 2 * pading) / 2,
                y = pading + offset.y * (height - 2 * pading) / 2 + (height - 2 * pading) / 2
            )
        }
        toNotAbsoluteOffset = { offset: Offset ->
            Offset(
                x = (offset.x - pading - (width - 2 * pading) / 2) / (width - 2 * pading) * 2,
                y = (offset.y - pading - (height - 2 * pading) / 2) / (height - 2 * pading) * 2
            )
        }
    }.onPreviewKeyEvent {

            when {
                (it.isCtrlPressed && it.key == Key.Z && relesed) -> {
                    relesed = false
                    gv.comeBack()
                    changedAlgo.value = true
                    true
                }
                (it.type == KeyEventType.KeyUp) -> {
                    relesed = true
                    true
                }
                else -> false
            }
        }
        .focusRequester(requester)
        .focusable()
        .clickable() { requester.requestFocus() }
    ) {

        Canvas(modifier = Modifier.fillMaxSize().pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                change.consume()
                for (nodeView in gv.nodesViews) {
                    mainOffset -= dragAmount * sensivity
                }
            }
        }) {
            for ((i, verts) in gv.vertViews) {
                for ((j, view) in verts) {
                    drawLine(
                        color = view.color,
                        start = toAbsoluteOffset(view.start.offset) + Offset(
                            x = view.start.radius, y = view.start.radius
                        ) - mainOffset,
                        end = toAbsoluteOffset(view.end.offset) + Offset(
                            x = view.end.radius,
                            y = view.end.radius
                        ) - mainOffset,
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
                    mainOffset = mainOffset,
                    toAbsoluteOffset = toAbsoluteOffset,
                    toNotAbsoluteOffset = toNotAbsoluteOffset
                )
                // println(Pair(width, height))
            }
        }
    }
    LaunchedEffect(Unit) {
        requester.requestFocus()
    }
}
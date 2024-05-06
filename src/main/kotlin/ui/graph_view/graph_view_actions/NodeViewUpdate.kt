package ui.graph_view.graph_view_actions

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

data class NodeViewUpdate<D>(
    val offset: Offset? = null,
    val deltaOffset: Offset = Offset(x = 0f, y = 0f),

    val radius: Float? = null,
    val color: Color? = null,
    val value: D? = null,
    val shape: Shape? = null,
    val alpha: Float? = null
) {}
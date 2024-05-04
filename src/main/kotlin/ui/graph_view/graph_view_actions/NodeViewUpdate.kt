package ui.graph_view.graph_view_actions

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

data class NodeViewUpdate<D>(
    var offset: Offset? = null,
    var deltaOffset: Offset = Offset(x = 0f, y = 0f),

    var radius: Float? = null,
    var color: Color? = null,
    var value: D? = null,
    var shape: Shape? = null,
    var alpha: Float? = null
) {}
package ui.graph_view.graph_view_actions

import ui.graph_view.VertView

data class Update<D>(
    val nodeViewUpdate: MutableMap<D, NodeViewUpdate<D>> = mutableMapOf(),
    val vertViewUpdate: MutableMap<D, MutableMap<D, VertViewUpdate<D>>> = mutableMapOf()
) {}
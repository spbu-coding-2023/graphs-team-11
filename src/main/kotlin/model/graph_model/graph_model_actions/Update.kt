package model.graph_model.graph_model_actions

data class Update<D>(
    val nodeViewUpdate: MutableMap<D, NodeViewUpdate<D>> = mutableMapOf(),
    val vertViewUpdate: MutableMap<D, MutableMap<D, VertViewUpdate<D>>> = mutableMapOf()
) {}
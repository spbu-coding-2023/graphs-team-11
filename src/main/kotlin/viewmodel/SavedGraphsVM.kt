package viewmodel

import androidx.compose.runtime.MutableState
import data.db.sqlite_exposed.deleteGraph
import model.graph_model.Graph
import ui.components.MyWindowState

class SavedGraphsVM {

    fun onGraphLoad(state: MyWindowState, graph: Graph<*>) {
        state.close()
        state.openNewWindow(graph)
    }

    fun onGraphDelete(id: Int, graphList: MutableState<List<Triple<Int, Graph<*>, String>>>) {
        deleteGraph(id)
        graphList.value = graphList.value.filter { it.first != id }
    }
}
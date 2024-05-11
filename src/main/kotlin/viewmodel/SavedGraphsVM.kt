package viewmodel

import androidx.compose.runtime.MutableState
import data.db.sqlite_exposed.deleteGraph
import model.graph_model.Graph
import ui.components.MyWindowState

class SavedGraphsVM {

    fun onUseGraphSqliteExposedPressed(state: MyWindowState, graph: Graph<Any>) {
        state.openNewWindow(graph as Graph<Int>)
    }

    fun onDeleteGraphSqliteExposedPressed(id: Int, graphList: MutableState<List<Triple<Int, Graph<Any>, String>>>) {
        deleteGraph(id)
        graphList.value = graphList.value.filter { it.first != id }
    }
}
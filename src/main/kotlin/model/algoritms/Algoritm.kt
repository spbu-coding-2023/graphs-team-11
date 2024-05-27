package model.algoritms

import androidx.compose.runtime.snapshots.SnapshotStateMap
import model.graph_model.Graph
import model.graph_model.graph_model_actions.Update


abstract class Algoritm(open val selectedSizeRequired: Int?) {
    open fun algoRun(graph: Graph, selected: SnapshotStateMap<String, Int>): Update {
        return Update()
    }
}

enum class AlgoritmType {
    DIRECTED, UNDIRECTED, BOTH
}

data class AlgorithmData(
    val name: String,
    val algo: Algoritm,
    val type: AlgoritmType
)

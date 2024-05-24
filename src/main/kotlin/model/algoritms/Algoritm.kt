package model.algoritms

import androidx.compose.runtime.snapshots.SnapshotStateMap
import model.graph_model.Graph
import model.graph_model.graph_model_actions.Update

abstract class Algoritm<D>(open val selectedSizeRequired: Int?) {
    open fun <D> algoRun(graph: Graph<D>, selected: SnapshotStateMap<D, Int>): Update<D> {
        return Update()
    }
}

enum class AlgoritmType {
    DIRECTED, UNDIRECTED, BOTH
}

data class AlgorithmData<D>(
    val name: String,
    val algo: Algoritm<D>,
    val type: AlgoritmType
)

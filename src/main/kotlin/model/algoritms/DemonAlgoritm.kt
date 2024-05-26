package model.algoritms

import androidx.compose.runtime.snapshots.SnapshotStateMap
import model.graph_model.Graph
import model.graph_model.graph_model_actions.Update

abstract class DemonAlgoritm(override val selectedSizeRequired: Int?) : Algoritm(selectedSizeRequired) {
    override fun algoRun(graph: Graph, selected: SnapshotStateMap<String, Int>): Update {
        TODO("Demon Run")
    }
}

class Floyd : DemonAlgoritm(0) {
    override fun algoRun(graph: Graph, selected: SnapshotStateMap<String, Int>): Update {
        TODO("Floyd Run")
    }
}
package model.algoritms

import androidx.compose.runtime.snapshots.SnapshotStateMap
import model.graph_model.Graph
import model.graph_model.graph_model_actions.Update

abstract class DemonAlgoritm(override val selectedSizeRequired: Int?): Algoritm(selectedSizeRequired) {
    override fun <D> algoRun(graph: Graph<D>, selected: SnapshotStateMap<D, Int>): Update<D> {
        TODO("Demon Run")
    }
}

class Floyd : DemonAlgoritm(0) {
    override fun <D> algoRun(graph: Graph<D>, selected: SnapshotStateMap<D, Int>): Update<D> {
        TODO("Floyd Run")
    }
}
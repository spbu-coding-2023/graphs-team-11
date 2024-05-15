package model.algoritms

import androidx.compose.runtime.snapshots.SnapshotStateMap
import model.graph_model.Graph
import model.graph_model.graph_model_actions.Update

interface Algoritm {
    open fun <D> alogRun(graph: Graph<D>, selected: SnapshotStateMap<D, Int>): Update<D> {
        return Update()
    }
}
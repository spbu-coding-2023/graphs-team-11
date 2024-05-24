package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import model.graph_model.NodeViewClass

class NodeVM<D> {
    val showDuplicateError = mutableStateOf(false)
    val textFieldLength = 130

    fun onNodeSelected(
        nodeView: NodeViewClass<D>, selected: SnapshotStateMap<D, Int>, isShifted: MutableState<Boolean>
    ) {
        if (nodeView.value != null) {
            val value = nodeView.value!!
            if (!isShifted.value) {
                selected.forEach { selected.remove(it.key) }
                selected[value] = selected.size
            } else {
                if (value in selected) {
                    selected.remove(value)
                } else selected[value] = selected.size
            }
        }
    }

    fun onTextFieldTextChanged(
        text: String,
        newValue: MutableState<String>,
        graphNodeKeysList: List<String>,
        showDuplicateError: MutableState<Boolean>,
        nodeView: NodeViewClass<D>,
        changedAlgo: MutableState<Boolean>
    ) {
        if (text.contains('\n')) {
            if (newValue.value.isNotBlank()) {
                try {
                    nodeView.value = newValue.value as D
                    changedAlgo.value = true
                } catch (classCastException: ClassCastException) {
                    try {
                        nodeView.value = newValue.value.toInt() as D
                        changedAlgo.value = true
                    } catch (_: ClassCastException) {
                    }
                }
            }
        } else {
            showDuplicateError.value = graphNodeKeysList.contains(text)
            newValue.value = text
        }
    }
}
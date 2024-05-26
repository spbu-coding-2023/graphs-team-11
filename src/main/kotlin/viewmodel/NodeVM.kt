package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import model.graph_model.NodeViewClass

class NodeVM {
    val showDuplicateError = mutableStateOf(false)
    val textFieldLength = 130

    fun onNodeSelected(
        nodeView: NodeViewClass, selected: SnapshotStateMap<String, Int>, isShifted: MutableState<Boolean>
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
        nodeView: NodeViewClass,
        changedAlgo: MutableState<Boolean>,
    ) {
        if (text.contains('\n')) {
            if (newValue.value.isNotBlank()) {
                try {
                    nodeView.value = newValue.value
                    changedAlgo.value = true
                } catch (classCastException: ClassCastException) {
                    println(classCastException.message)
                }
            }
        } else {
            if (text.isNotBlank()) {
                showDuplicateError.value = graphNodeKeysList.contains(text)
                newValue.value = text
            }
        }
    }
}
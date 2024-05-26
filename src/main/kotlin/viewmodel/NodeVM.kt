package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import model.graph_model.NodeViewClass
import ui.components.GraphKeyType
import ui.components.MyWindowState

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
        changedAlgo: MutableState<Boolean>,
        graphKeyType: GraphKeyType
    ) {
        if (text.contains('\n')) {
            if (newValue.value.isNotBlank()) {
                try {
                    nodeView.value = toKeyType(graphKeyType, newValue.value)
                    changedAlgo.value = true
                } catch (classCastException: ClassCastException) {
                    println(classCastException.message)
                }
            }
        } else {
            if (allowChange(text, graphKeyType) || text.isBlank()) {
                showDuplicateError.value = graphNodeKeysList.contains(text)
                newValue.value = text
            }
        }
    }

    private fun toKeyType(graphKeyType: GraphKeyType, value: String): D {
        return when (graphKeyType) {
            GraphKeyType.FLOAT -> value.toFloat() as D
            GraphKeyType.INT -> value.toInt() as D
            GraphKeyType.STRING -> value as D
        }
    }

    private fun allowChange(text: String, graphKeyType: GraphKeyType): Boolean {
        return when (graphKeyType) {
            GraphKeyType.FLOAT -> checkFloat(text)
            GraphKeyType.INT -> text.toIntOrNull() != null
            GraphKeyType.STRING -> true
        }
    }

    private fun checkFloat(text: String): Boolean {
        val dotCount = text.count { it == '.' }
        if (dotCount > 1) return false
        if (dotCount == 1) {
            val dotLessText = text.replace(".", "")
            return dotLessText.toIntOrNull() != null
        }
        return text.toIntOrNull() != null
    }
}
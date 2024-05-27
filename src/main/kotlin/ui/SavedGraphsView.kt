package ui


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import model.graph_model.Graph
import ui.components.MyWindowState
import ui.theme.BdsmAppTheme
import ui.theme.Theme
import viewmodel.SavedGraphsVM
import java.awt.Dimension

@Composable
fun SavedGraphsView(
    onClose: () -> Unit, appTheme: MutableState<Theme>, graphs: List<Triple<Int, Graph, String>>, state: MyWindowState
) {
    val viewModel = SavedGraphsVM()
    val graphList = mutableStateOf(graphs)

    Window(
        title = "Saved Graphs", onCloseRequest = onClose, alwaysOnTop = true
    ) {
        window.minimumSize = Dimension(600, 450)
        BdsmAppTheme(appTheme = appTheme.value) {
            LazyColumn(
                modifier = Modifier.background(MaterialTheme.colors.background).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                graphList.value.forEach { (id, graph, name) ->
                    item {
                        SavedGraphItem(graph, name, onUsePressed = {
                            viewModel.onGraphLoad(state, graph)
                        }, onDeletePressed = { viewModel.onGraphDelete(id, graphList) })
                    }
                }
            }
        }
    }
}

@Composable
fun SavedGraphItem(graph: Graph, name: String, onUsePressed: () -> Unit, onDeletePressed: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 5.dp).clip(RoundedCornerShape(10.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = name, modifier = Modifier.padding(10.dp), fontSize = 20.sp)
        Text(text = graph.size.toString(), modifier = Modifier.padding(10.dp), fontSize = 20.sp)

        Row(modifier = Modifier.padding(10.dp)) {
            Icon(Icons.Default.Check,
                contentDescription = "Use",
                tint = Color(0xFF21a038),
                modifier = Modifier.clickable { onUsePressed() })
            Divider(Modifier.width(20.dp))
            Icon(Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.Red,
                modifier = Modifier.clickable { onDeletePressed() })
        }
    }
}
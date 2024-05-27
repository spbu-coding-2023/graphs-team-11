package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import model.graph_model.GraphViewClass
import viewmodel.GraphVM

@Composable
@Preview
fun GraphPopup(
    enabled: MutableState<Boolean>,
    pressOffset: Offset,
    graphVM: GraphVM,
    gv: GraphViewClass
) {

    var alert by remember { mutableStateOf(AlertType.UNABLED) }
    var text by remember { mutableStateOf("") }

    DropdownMenu(
        expanded = enabled.value,
        onDismissRequest = { enabled.value = false },
        offset = DpOffset(x = pressOffset.x.dp, y = pressOffset.y.dp - graphVM.height.dp)
    ) {
        DropdownMenuItem(
            onClick = {
                alert = AlertType.CREATENODE
            }
        ) {
            Text("Create Node")
        }
        DropdownMenuItem(onClick = { alert = AlertType.CREATEVERT }) {
            Text("Create Vert")
        }
        Divider()
        DropdownMenuItem(onClick = { alert = AlertType.DELETENODE }) {
            Text("Delete Node")
        }
        DropdownMenuItem(onClick = { alert = AlertType.DELETEVERT }) {
            Text("Delete Vert")
        }
    }

    if (alert != AlertType.UNABLED) {
        AlertDialog(
            onDismissRequest = {
                alert = AlertType.UNABLED
            },
            title = {
                Text(text = "Title")
            },
            text = {
                Column {
                    TextField(
                        value = text,
                        onValueChange = { text = it }
                    )
                    Text("Custom Text")
                    Checkbox(checked = false, onCheckedChange = {})
                }
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { alert = AlertType.UNABLED }
                    ) {
                        Text("Dismiss")
                    }
                }
            }
        )
    }

}

enum class AlertType {
    UNABLED, CREATENODE, CREATEVERT, DELETENODE, DELETEVERT
}
package ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import ui.components.MyWindowState
import ui.theme.BdsmAppTheme
import ui.theme.Theme
import viewmodel.IntroWindowVM
import java.awt.Dimension

@Composable
fun IntroWindowView(
    state: MyWindowState, isSettingMenuOpen: MutableState<Boolean>, appTheme: MutableState<Theme>
) {
    val viewModel = IntroWindowVM(isSettingMenuOpen)
    val windowState = rememberWindowState(size = DpSize(800.dp, 760.dp))

    Window(onCloseRequest = state::close, title = state.title, state = windowState) {
        window.minimumSize = Dimension(700, 600)
        MenuBar {
            Menu("BDSM Graphs") {
                Item("Settings", onClick = { viewModel.onSettingsPressed() })
            }

            Menu("SQLite Exposed") {
                Item("View Graphs", onClick = { viewModel.onSQLEViewGraphsPressed() })
            }
        }

        IntroView(viewModel, state, appTheme)

        if (viewModel.isSettingMenuOpen.value) {
            SettingsView(
                onClose = { isSettingMenuOpen.value = false },
                appTheme,
            )
        }
        if (viewModel.isSavedGraphsOpen.value) {
            SavedGraphsView(
                onClose = { viewModel.isSavedGraphsOpen.value = false }, appTheme, viewModel.graphList, state
            )
        }
    }
}

@Composable
fun IntroView(viewModel: IntroWindowVM, state: MyWindowState, appTheme: MutableState<Theme>) {
    BdsmAppTheme(appTheme = appTheme.value) {
        val expanded = remember { mutableStateOf(false) }
        val selectedGraphKeyType = remember { mutableStateOf(IntroWindowVM.GraphKeyType.INT) }
        val chosenGraph = remember { mutableStateOf("Manual") }
        val graphSize = remember { mutableStateOf("") }
        val chosenGenerator = remember { mutableStateOf("Random Tree") }

        Column(
            modifier = Modifier.fillMaxSize().padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Choose Graph Type: ", fontSize = 22.sp)
                    Divider(modifier = Modifier.width(10.dp))
                    Column {
                        Button(
                            onClick = { expanded.value = true },
                            modifier = Modifier.widthIn(min = 100.dp),
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colors.surface)
                        ) {
                            Text(selectedGraphKeyType.value.name)
                        }
                        DropdownMenu(
                            expanded = expanded.value,
                            onDismissRequest = { expanded.value = false },
                        ) {
                            IntroWindowVM.GraphKeyType.entries.forEach { graphKeyType ->
                                DropdownMenuItem(onClick = {
                                    selectedGraphKeyType.value = graphKeyType
                                    expanded.value = false
                                }) {
                                    Text(graphKeyType.name)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    listOf("Manual", "Generate", "Empty").forEach { graphType ->
                        Row(Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = (chosenGraph.value == graphType),
                                onClick = { chosenGraph.value = graphType },
                                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colors.surface)
                            )
                            Text(
                                text = graphType,
                                style = MaterialTheme.typography.body1.merge(),
                                modifier = Modifier.padding(start = 5.dp),
                                fontSize = 20.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                if (chosenGraph.value != "Empty") {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp).heightIn(min = 200.dp)
                            .border(2.dp, MaterialTheme.colors.primary, MaterialTheme.shapes.medium),
                        contentAlignment = Alignment.Center
                    ) {
                        when (chosenGraph.value) {
                            "Manual" -> {
                                IntTextField(graphSize)
                            }

                            "Generate" -> {
                                GenerateGraphSettings(graphSize, chosenGenerator)
                            }

                            else -> return@Box
                        }
                    }
                }
            }

            when (chosenGraph.value) {
                "Manual" -> {
                    CreateGraphButtonWithCharCheck(selectedGraphKeyType, chosenGenerator, graphSize) {
                        val graph = viewModel.createGraphWithoutEdges(
                            selectedGraphKeyType.value, graphSize.value.toInt()
                        )
                        state.close()
                        state.openNewWindow(graph)
                    }
                }

                "Generate" -> {
                    CreateGraphButtonWithCharCheck(selectedGraphKeyType, chosenGenerator, graphSize) {
                        val graph = viewModel.generateGraph(
                            graphSize.value.toInt(), chosenGenerator.value, selectedGraphKeyType.value
                        )
                        state.close()
                        state.openNewWindow(graph)
                    }
                }

                "Empty" -> {
                    Button(
                        onClick = {
                            val graph = viewModel.createEmptyGraph(selectedGraphKeyType.value)
                            state.close()
                            state.openNewWindow(graph)
                        }, modifier = Modifier.padding(bottom = 20.dp), colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.surface,
                            disabledBackgroundColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
                        )
                    ) {
                        Text("Create Graph")
                    }
                }
            }

        }
    }
}

@Composable
fun CreateGraphButtonWithCharCheck(
    selectedGraphKeyType: MutableState<IntroWindowVM.GraphKeyType>,
    chosenGenerator: MutableState<String>,
    graphSize: MutableState<String>,
    onClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        if (chosenGenerator.value == "Flower Snark" && (selectedGraphKeyType.value == IntroWindowVM.GraphKeyType.CHAR) && (graphSize.value.isNotEmpty() && graphSize.value.toIntOrNull() != null) && (graphSize.value.toInt() > 5)) {
            Text("Char Graph size should be less than 5 for Flower Snark", color = MaterialTheme.colors.error)
            Spacer(modifier = Modifier.height(10.dp))
        } else if ((selectedGraphKeyType.value == IntroWindowVM.GraphKeyType.CHAR) && (graphSize.value.isNotEmpty() && graphSize.value.toIntOrNull() != null) && (graphSize.value.toInt() > 26)) {
            Text("Char Graph size should be less than 26", color = MaterialTheme.colors.error)
            Spacer(modifier = Modifier.height(10.dp))
        }
        Button(
            onClick = onClick,
            modifier = Modifier.padding(bottom = 20.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.surface,
                disabledBackgroundColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
            ),
            enabled =
            if (chosenGenerator.value == "Flower Snark")
                (graphSize.value.isNotEmpty() && graphSize.value.toIntOrNull() != null)
                        && !((selectedGraphKeyType.value == IntroWindowVM.GraphKeyType.CHAR)
                        && (graphSize.value.toInt() > 5))
            else (graphSize.value.isNotEmpty() && graphSize.value.toIntOrNull() != null)
                    && !((selectedGraphKeyType.value == IntroWindowVM.GraphKeyType.CHAR)
                    && (graphSize.value.toInt() > 26))

        ) {
            Text("Create Graph")
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GenerateGraphSettings(graphSize: MutableState<String>, chosenGenerator: MutableState<String>) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        IntTextField(graphSize)

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.Center
        ) {
            listOf("Random Tree", "Flower Snark", "Star Directed", "Star Undirected").forEach { graphType ->
                Row(Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = (chosenGenerator.value == graphType),
                        onClick = { chosenGenerator.value = graphType },
                        colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colors.surface)
                    )
                    Text(
                        text = graphType,
                        style = MaterialTheme.typography.body1.merge(),
                        modifier = Modifier.padding(start = 5.dp),
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
fun IntTextField(graphSize: MutableState<String>) {
    Row(
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Choose Graph Size: ", fontSize = 18.sp)
        TextField(value = graphSize.value, onValueChange = {
            if (it.toIntOrNull() == null && it.isNotEmpty()) {
                return@TextField
            }
            graphSize.value = it
        }, label = { Text("Size") })
    }
}

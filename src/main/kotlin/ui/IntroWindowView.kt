package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import data.Constants.APP_NAME
import data.Constants.SETTINGS_SHORTCUT
import kotlinx.coroutines.CoroutineScope
import ui.components.GraphFilePicker
import ui.components.GraphKeyType
import ui.components.MyWindowState
import ui.theme.BdsmAppTheme
import ui.theme.Theme
import viewmodel.IntroWindowVM
import java.awt.Dimension

@Composable
fun IntroWindowView(
    state: MyWindowState, isSettingMenuOpen: MutableState<Boolean>, appTheme: MutableState<Theme>, scope: CoroutineScope
) {
    val viewModel = IntroWindowVM(isSettingMenuOpen, scope, state.graphKeyType)
    val windowState = rememberWindowState(size = DpSize(800.dp, 760.dp))

    Window(onCloseRequest = state::close, title = state.title, state = windowState) {
        window.minimumSize = Dimension(700, 600)
        MenuBar {
            Menu(APP_NAME) {
                Item("Settings", shortcut = SETTINGS_SHORTCUT, onClick = { viewModel.onSettingsPressed() })
            }
        }

        IntroView(viewModel, state, appTheme, scope)

        if (viewModel.isSettingMenuOpen.value) {
            SettingsView(
                onClose = { isSettingMenuOpen.value = false },
                appTheme,
            )
        }
    }
}

@Composable
fun IntroView(viewModel: IntroWindowVM, state: MyWindowState, appTheme: MutableState<Theme>, scope: CoroutineScope) {
    BdsmAppTheme(appTheme = appTheme.value) {

        Column(
            modifier = Modifier.fillMaxSize().padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

                Row(
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    listOf("Saved", "Manual", "Generate", "Empty").forEach { graphType ->
                        Row(
                            Modifier.padding(vertical = 4.dp).clickable { viewModel.chosenGraph.value = graphType },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (viewModel.chosenGraph.value == graphType),
                                onClick = { viewModel.chosenGraph.value = graphType },
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

                Spacer(modifier = Modifier.height(10.dp))

                if (viewModel.chosenGraph.value != "Saved") {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Choose Graph Type: ", fontSize = 22.sp)
                        Divider(modifier = Modifier.width(10.dp))
                        Column {
                            Button(
                                onClick = { viewModel.expanded.value = true },
                                modifier = Modifier.widthIn(min = 100.dp),
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.surface)
                            ) {
                                Text(viewModel.selectedGraphKeyType.value.name)
                            }
                            DropdownMenu(
                                expanded = viewModel.expanded.value,
                                onDismissRequest = { viewModel.expanded.value = false },
                            ) {
                                GraphKeyType.entries.forEach { graphKeyType ->
                                    DropdownMenuItem(onClick = {
                                        viewModel.selectedGraphKeyType.value = graphKeyType
                                        viewModel.expanded.value = false
                                    }) {
                                        Text(graphKeyType.name)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }

                if (viewModel.chosenGraph.value != "Empty") {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp).heightIn(min = 200.dp)
                            .border(2.dp, MaterialTheme.colors.primary, MaterialTheme.shapes.medium),
                        contentAlignment = Alignment.Center
                    ) {
                        when (viewModel.chosenGraph.value) {
                            "Saved" -> {
                                SavedGraphsList(viewModel, state)
                            }

                            "Manual" -> {
                                IntTextField(viewModel.graphSize)
                            }

                            "Generate" -> {
                                GenerateGraphSettings(viewModel)
                            }

                            else -> return@Box
                        }
                    }
                }
            }

            when (viewModel.chosenGraph.value) {
                "Manual" -> {
                    CreateGraphButtonWithCheck(viewModel) {
                        val graph = viewModel.createGraphWithoutEdges()
                        state.reloadWindow(graph, scope, graphKeyType = viewModel.selectedGraphKeyType.value)
                    }
                }

                "Generate" -> {
                    CreateGraphButtonWithCheck(viewModel) {
                        val maxWeight = viewModel.weightMax.value.toIntOrNull()?.coerceIn(1, 100) ?: 1
                        val graph = viewModel.generateGraph(
                            maxWeight
                        )
                        state.reloadWindow(graph, scope, graphKeyType = viewModel.selectedGraphKeyType.value)
                    }
                }

                "Empty" -> {
                    Button(
                        onClick = {
                            val graph = viewModel.createEmptyGraph()
                            state.reloadWindow(graph, scope, true, viewModel.selectedGraphKeyType.value)
                        }, modifier = Modifier.padding(bottom = 20.dp), colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.surface,
                            disabledBackgroundColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
                        )
                    ) {
                        Text("Create Graph")
                    }
                }

                else -> return@Column
            }

        }
    }
}

@Composable
fun SavedGraphsList(
    viewModel: IntroWindowVM, state: MyWindowState
) {
    Column(
        modifier = Modifier.background(MaterialTheme.colors.background).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = {
                viewModel.isFileLoaderOpen.value = true
            }, modifier = Modifier.padding(10.dp), colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.surface,
                disabledBackgroundColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
            )
        ) {
            Text("Load Graph from file")
        }
        GraphFilePicker(viewModel.isFileLoaderOpen, viewModel.fileLoaderException, state)
        LazyColumn(
            modifier = Modifier.background(MaterialTheme.colors.background).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            viewModel.graphList.value.forEach { (id, graph, name) ->
                item {
                    SavedGraphItem(graph, name, onUsePressed = {
                        viewModel.onUseGraphSqliteExposedPressed(state, graph)
                    }, onDeletePressed = { viewModel.onDeleteGraphSqliteExposedPressed(id) })
                }
            }
        }
    }
}

@Composable
fun CreateGraphButtonWithCheck(
    viewModel: IntroWindowVM, onClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = onClick,
            modifier = Modifier.padding(bottom = 20.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.surface,
                disabledBackgroundColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
            ),
            enabled = viewModel.graphSize.value.isNotEmpty() && viewModel.graphSize.value.toIntOrNull() != null && viewModel.graphSize.value.toInt() > 0

        ) {
            Text("Create Graph")
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GenerateGraphSettings(
    viewModel: IntroWindowVM
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        IntTextField(viewModel.graphSize)

        if (viewModel.chosenGenerator.value == "Random Tree") {
            Row(
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Max edge weight: ", fontSize = 18.sp)
                TextField(value = viewModel.weightMax.value, onValueChange = {
                    if (it.toIntOrNull() == null && it.isNotEmpty()) {
                        return@TextField
                    }
                    viewModel.weightMax.value = it
                }, label = { Text("1 - 100") })
            }

        }

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.Center
        ) {
            listOf("Random Tree", "Flower Snark", "Star Directed", "Star Undirected").forEach { graphType ->
                Row(Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = (viewModel.chosenGenerator.value == graphType),
                        onClick = { viewModel.chosenGenerator.value = graphType },
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

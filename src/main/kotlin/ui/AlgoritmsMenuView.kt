package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import model.algoritms.AlgorithmData
import model.algoritms.AlgoritmType
import model.algoritms.BridgeFinding
import model.algoritms.ConnectivityСomponent
import model.algoritms.Kruskal
import model.algoritms.LeidenToRun
import model.algoritms.SampleAlgo
import model.algoritms.ShortestPathDetection
import model.algoritms.SomeThingLikeDFS
import model.graph_model.GraphViewClass
import model.graph_model.UndirectedGraph
import viewmodel.AlgorithmMenuVM

@Composable
@Preview
fun LeftMenu(
    graphViewClass: GraphViewClass, changedAlgo: MutableState<Boolean>, selected: SnapshotStateMap<String, Int>
) {
    val viewModel = remember { AlgorithmMenuVM() }
    val isMenuVisible = viewModel.isMenuVisible.value

    val density = LocalDensity.current

    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxHeight(0.5f)
    ) {
        AnimatedVisibility(visible = isMenuVisible, enter = slideInHorizontally {
            with(density) { -40.dp.roundToPx() }
        } + expandHorizontally(
            expandFrom = Alignment.Start
        ) + fadeIn(
            initialAlpha = 0.3f
        ), exit = slideOutHorizontally() + shrinkHorizontally() + fadeOut()) {
            Surface(
                modifier = Modifier.width(viewModel.menuWidth).fillMaxHeight(),
                shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
            ) {
                AlgoritmList(graphViewClass, changedAlgo, viewModel, selected)
            }
        }
        Icon(imageVector = if (isMenuVisible) Icons.AutoMirrored.Filled.KeyboardArrowLeft
        else Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Menu",
            modifier = Modifier.size(30.dp).clickable { viewModel.toggleMenu() })
    }
    if (viewModel.isException.value) {
        AlertDialog(
            title = { Text("Exception!") },
            text = { Text(viewModel.exceptionMessage.value) },
            onDismissRequest = { viewModel.isException.value = false },
            confirmButton = {
                Button(onClick = {
                    viewModel.isException.value = false
                }) {
                    Text("Confirm")
                }
            },
        )
    }
}

@Composable
@Stable
fun AlgoritmList(
    graphViewClass: GraphViewClass,
    changedAlgo: MutableState<Boolean>,
    viewModel: AlgorithmMenuVM,
    selected: SnapshotStateMap<String, Int>
) {
    val algoList = listOf(
        AlgorithmData("Detect Communities", LeidenToRun(), AlgoritmType.BOTH),
        AlgorithmData("Sample Algorithm", SampleAlgo(), AlgoritmType.BOTH),
        AlgorithmData("Something like DFS", SomeThingLikeDFS(), AlgoritmType.BOTH),
        AlgorithmData("Kosaraju", ConnectivityСomponent(), AlgoritmType.DIRECTED),
        AlgorithmData("Minimal Tree", Kruskal(), AlgoritmType.UNDIRECTED),
        AlgorithmData("Shortest Path Detection", ShortestPathDetection(), AlgoritmType.BOTH),
        AlgorithmData("Find Bridges", BridgeFinding(), AlgoritmType.UNDIRECTED),
    )

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.surface).padding(8.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(text = "Algorithms")
        Divider(color = Color.Black, modifier = Modifier.fillMaxWidth(0.3f))
        val algorithms = algoList.filter {
            it.type == AlgoritmType.BOTH ||
                    (graphViewClass.graph is UndirectedGraph && it.type == AlgoritmType.UNDIRECTED) ||
                    (graphViewClass.graph !is UndirectedGraph && it.type == AlgoritmType.DIRECTED)
        }
        for (algorithm in algorithms) {
            Text(text = algorithm.name, modifier = Modifier.clickable(onClick = {
                if (selected.size != algorithm.algo.selectedSizeRequired && algorithm.algo.selectedSizeRequired != null) {
                    viewModel.isException.value = true
                    viewModel.exceptionMessage.value =
                        "Required " + algorithm.algo.selectedSizeRequired.toString() + " selected nodes!"
                } else {
                    viewModel.runAlgorithm(algorithm.algo, graphViewClass, changedAlgo, selected)
                }
            }).offset(10.dp))
        }
    }
}

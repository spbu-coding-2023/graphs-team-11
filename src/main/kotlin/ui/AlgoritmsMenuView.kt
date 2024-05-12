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
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import model.algoritms.LeidenToRun
import model.algoritms.SampleAlgo
import model.algoritms.SomeThingLikeDFS
import model.graph_model.GrahpViewClass
import viewmodel.AlgorithmMenuVM

@Composable
@Preview
fun <D> LeftMenu(
    grahpViewClass: GrahpViewClass<D>, changedAlgo: MutableState<Boolean>, selected: SnapshotStateMap<D, Boolean>
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
                AlgoritmList(grahpViewClass, changedAlgo, viewModel)
            }
        }
        Icon(imageVector = if (isMenuVisible) Icons.AutoMirrored.Filled.KeyboardArrowLeft
        else Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Menu",
            modifier = Modifier.size(30.dp).clickable { viewModel.toggleMenu() })
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Stable
fun <D> AlgoritmList(
    grahpViewClass: GrahpViewClass<D>, changedAlgo: MutableState<Boolean>, viewModel: AlgorithmMenuVM
) {
    val algoList = mutableListOf(
        Pair("Detect Communities", LeidenToRun()),
        Pair("Sample Algorithm", SampleAlgo()),
        Pair("Something like DFS", SomeThingLikeDFS()),
    )

    val expanded = mutableStateOf(false)

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.surface).padding(8.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(text = "Algorithms")
        Divider(color = Color.Black, modifier = Modifier.fillMaxWidth(0.3f))
        for (algorithm in algoList) {
            Text(text = algorithm.first, modifier = Modifier.clickable(onClick = {
                viewModel.runAlgorithm(algorithm.second, grahpViewClass, changedAlgo)
            }).offset(10.dp))
        }
    }
}

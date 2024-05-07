package ui

import androidx.compose.animation.*
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import model.graph_model.GrahpViewClass
import viewmodel.AlgorithmMenuVM

@Composable
@Preview
fun <D> AlgorithmMenu(grahpViewClass: GrahpViewClass<D>, changedAlgo: MutableState<Boolean>) {
    val viewModel = remember { AlgorithmMenuVM() }
    val isMenuVisible = viewModel.isMenuVisible.value

    val density = LocalDensity.current

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxHeight().background(Color.White)) {
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

@Composable
@Stable
fun <D> AlgoritmList(
    grahpViewClass: GrahpViewClass<D>, changedAlgo: MutableState<Boolean>, viewModel: AlgorithmMenuVM
) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.LightGray).padding(8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
    ) {
        Text(text = "Algorithms")
        Divider(color = Color.Black, modifier = Modifier.fillMaxWidth(0.3f))
        for (algorithm in viewModel.algo.algoritms) {
            Text(text = algorithm.simpleName.toString(), modifier = Modifier.clickable(onClick = {
                viewModel.runAlgorithm(algorithm, grahpViewClass, changedAlgo)
            }).offset(10.dp))
        }
    }
}
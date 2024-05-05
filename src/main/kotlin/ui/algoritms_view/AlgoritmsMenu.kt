package ui.algoritms_view

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
import ui.graph_view.GrahpView
import ui.graph_view.graph_view_actions.NodeViewUpdate
import kotlin.reflect.full.createInstance

@Composable
@Preview
fun <D>AlgorithmMenu(grahpView: GrahpView<D>) {
    val menuWidth = 200.dp
    var menuVisible by remember { mutableStateOf(true) }
    val density = LocalDensity.current

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxHeight().background(Color.White)) {

        AnimatedVisibility(visible = menuVisible, enter = slideInHorizontally {
            with(density) { -40.dp.roundToPx() }
        } + expandHorizontally(
            expandFrom = Alignment.Start
        ) + fadeIn(
            initialAlpha = 0.3f
        ), exit = slideOutHorizontally() + shrinkHorizontally() + fadeOut()) {
            Surface(
                modifier = Modifier.width(menuWidth).fillMaxHeight(),
                shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
            ) {
                AlgoritmList(grahpView)
            }
        }
        Icon(imageVector = if (menuVisible) Icons.AutoMirrored.Filled.KeyboardArrowLeft
        else Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Menu",
            modifier = Modifier.size(30.dp).clickable {
                menuVisible = !menuVisible
            })
    }
}

@Composable
@Stable
fun <D>AlgoritmList(grahpView: GrahpView<D>) {

    val algo = AlgoritmFinder()


    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.LightGray).padding(8.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
    ) {
        Text(text = "Algorithms")
        Divider(color = Color.Black, modifier = Modifier.fillMaxWidth(0.3f))
        for (i in algo.algoritms) {
            Text(text = i.simpleName.toString(), modifier = Modifier.clickable(
                onClick = {
                    val runAlgo = i.members.single { it.name == "alogRun" }
                    val algoExpample = i.createInstance()

                    val update = runAlgo.call(algoExpample, grahpView.graph) as MutableMap<D, NodeViewUpdate<D>>

                    grahpView.applyAction(update)
                }
            ).offset(10.dp))
        }
    }
}

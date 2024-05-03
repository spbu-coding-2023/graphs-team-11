package components.algoritms_view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.reflect.full.createInstance

@Composable
@Preview
@Stable
fun AlgoritmListComponent() {

    val algo = AlgoritmFinder()

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.LightGray).padding(8.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
    ) {
        Text(text = "Algorithms")
        Divider(color = Color.Black, modifier = Modifier.fillMaxWidth(0.3f))
        for (i in algo.algoritms) {
            Text(text = i.simpleName.toString(), modifier = Modifier.clickable() {
                // i.members.forEach { println(it.name)}
                val runAlgo = i.members.single { it.name == "alogRun" }
                val algoExpample = i.createInstance()

                runAlgo.call(algoExpample)
            })

        }
    }
}
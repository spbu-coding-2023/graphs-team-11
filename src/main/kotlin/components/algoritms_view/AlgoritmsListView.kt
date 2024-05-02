package components.algoritms_view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import data.algoritms.Algoritm
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

@Composable
@Preview
@Stable
fun AlgoritmListComponent(modifier: Modifier = Modifier) {

    var algo = AlgoritmFinder()


    Column(
        modifier = modifier
            .background(Color.LightGray)
            .verticalScroll(rememberScrollState())
            .fillMaxHeight()
            .padding(24.dp)
    ) {
        Text(text = "Algorithms")
        Divider(color = Color.Black, modifier = Modifier.fillMaxWidth(0.3f))
        for (i in algo.algoritms) {
                Text(text = i.simpleName.toString(),
                    modifier = Modifier
                        .clickable() {
                            // i.members.forEach { println(it.name)}
                            var runAlgo = i.members.single {it.name == "alogRun"}
                            var algoExpample = i.createInstance()

                            runAlgo.call(algoExpample)
                        }
                )

        }
    }
}
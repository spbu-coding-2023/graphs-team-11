import androidx.compose.animation.core.updateTransition
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.Key.Companion.Tab
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.semantics.Role.Companion.Tab
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import components.algoritms_view.AlgoritmFinder
import components.algoritms_view.AlgoritmListComponent
import components.graph_view.GrahpView
import components.graph_view.GrahpViewComponent
import data.Graph
import data.algoritms.Algoritm
import data.tools.graphGenerators.flowerSnark
import data.tools.graphGenerators.starDirected
import data.tools.graphGenerators.starUndirected
import org.jetbrains.skiko.kotlinBackend
import java.awt.GraphicsEnvironment
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.stream.Collector
import javax.sound.sampled.Line
import kotlin.jvm.internal.Reflection
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.jvm.internal.impl.metadata.ProtoBuf
import kotlin.reflect.jvm.internal.impl.resolve.jvm.JvmClassName
import androidx.compose.material.Tab

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    var g = flowerSnark(100)

    var gv = GrahpView<Int>(g)

    var algo = AlgoritmFinder()

    MaterialTheme {
        Row (modifier = Modifier
            .border(border = BorderStroke(width = 1.dp, Color.LightGray))) {
            Card (modifier = Modifier.fillMaxHeight()) {
                AlgoritmListComponent()
            }
            Card {
                GrahpViewComponent(gv, showNodes = true)
            }
        }
    }

}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }

}

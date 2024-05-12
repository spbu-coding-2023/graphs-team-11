package ui.components.cosmetic

import viewmodel.CosmeticVM

import androidx.compose.animation.*
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import ui.theme.BdsmAppTheme
import viewmodel.AlgorithmMenuVM
import viewmodel.cosmetics.ColorSetter

@Composable
@Preview
fun ColorPick(cosVM: CosmeticVM) {
    val model by remember { mutableStateOf(ColorSetter()) }

    cosVM.cosmeticWidgetsViewModels.add(model)

    var red by remember { mutableStateOf(model.color.red) }
    var green by remember { mutableStateOf(model.color.green) }
    var blue by remember { mutableStateOf(model.color.blue) }


    Column {
        Row {
            Text("Node Color")
            Box(modifier = Modifier.background(model.color, shape = CircleShape).size(30.dp))
        }
        Row {
            Text("R:")
            Slider(value = red, valueRange = 0f..1f, steps = 256, onValueChange = {
                red = it
                model.color = Color(red, green, blue)
                println(model.color)
            })
        }
        Row {
            Text("G:")
            Slider(value = green, valueRange = 0f..1f, steps = 256, onValueChange = {
                green = it
                model.color = Color(red, green, blue)
                println(model.color)
            })
        }
        Row {
            Text("B:")
            Slider(value = blue, valueRange = 0f..1f, steps = 256, onValueChange = {
                blue = it
                model.color = Color(red, green, blue)
                println(model.color)
            })
        }
    }
}
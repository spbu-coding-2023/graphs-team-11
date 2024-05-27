package ui.components.cosmetic

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import viewmodel.CosmeticVM
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
            })
        }
        Row {
            Text("G:")
            Slider(value = green, valueRange = 0f..1f, steps = 256, onValueChange = {
                green = it
                model.color = Color(red, green, blue)
            })
        }
        Row {
            Text("B:")
            Slider(value = blue, valueRange = 0f..1f, steps = 256, onValueChange = {
                blue = it
                model.color = Color(red, green, blue)
            })
        }
    }
}
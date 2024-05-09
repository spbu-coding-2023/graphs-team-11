package ui.components.cosmetic

import viewmodel.CosmeticVM

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

@Composable
@Preview
fun <D> CommeticsMenu(grahpViewClass: GrahpViewClass<D>, changedAlgo: MutableState<Boolean>) {
    val viewModel = remember { CosmeticVM() }
    val isMenuVisible = viewModel.isMenuVisible.value

    val density = LocalDensity.current

    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxHeight()
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
                Column {
                    CommeticsList(viewModel)
                    Button(
                        onClick = {
                            viewModel.createUpdate(grahpViewClass)
                            changedAlgo.value = true
                        }
                    ) {
                        Text("Apply")
                    }
                }
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
fun CommeticsList(
    viewModel: CosmeticVM
) {

    Column(
        modifier = Modifier.fillMaxSize(0.8f).background(MaterialTheme.colors.surface).padding(8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
    ) {
        Text(text = "Cosmetic")
        Divider(color = Color.Black, modifier = Modifier.fillMaxWidth(0.3f))

        ColorPick(viewModel)
    }
}
package ui

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import ui.theme.Theme
import ui.theme.ThemeColors

@Composable
fun SettingsView(onClose: () -> Unit, appTheme: MutableState<Theme>) {
    val goldenMode = remember { mutableStateOf(false) }
    val backgroundColor = when (appTheme.value) {
        Theme.DARK -> ThemeColors.Dark.background
        Theme.LIGHT -> ThemeColors.Light.background
        else -> ThemeColors.Golden.background
    }
    Window(
        title = "Settings",
        onCloseRequest = onClose,
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.background(backgroundColor).fillMaxSize().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    darkModeToggle(appTheme)
                    if (goldenMode.value) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Golden Mode", fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(30.dp))
                            Checkbox(
                                checked = appTheme.value == Theme.GOLDEN, onCheckedChange = {
                                    goldenMode.value = false
                                    appTheme.value = Theme.LIGHT
                                }, modifier = Modifier.size(40.dp), colors = CheckboxDefaults.colors(
                                    checkedColor = MaterialTheme.colors.primary,
                                    uncheckedColor = MaterialTheme.colors.secondary
                                )
                            )
                        }
                    }
                }

                donateButton(goldenMode, appTheme)
            }
        }
    }
}

@Composable
fun darkModeToggle(appTheme: MutableState<Theme>) {
    val size = 40.dp
    val animationSpec: AnimationSpec<Dp> = tween(durationMillis = 300)
    val offset by animateDpAsState(
        targetValue = if (appTheme.value == Theme.DARK) 0.dp else size, animationSpec = animationSpec, label = ""
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text("Dark Mode", fontSize = 22.sp)
        Spacer(modifier = Modifier.width(30.dp))
        // Switch to toggle dark mode
        Box(
            modifier = Modifier.width(size * 2).height(size).clip(shape = CircleShape).clickable(
                enabled = appTheme.value != Theme.GOLDEN
            ) {
                if (appTheme.value == Theme.DARK) {
                    appTheme.value = Theme.LIGHT
                } else {
                    appTheme.value = Theme.DARK
                }
            }.background(
                if (appTheme.value == Theme.GOLDEN) Color.LightGray else MaterialTheme.colors.surface.copy(
                    alpha = 0.2f
                )
            )
        ) {
            Box(
                modifier = Modifier.size(size).offset(x = offset).padding(all = 5.dp).clip(shape = CircleShape)
                    .background(
                        if (appTheme.value == Theme.DARK) MaterialTheme.colors.primary
                        else MaterialTheme.colors.secondary
                    )
            ) {}
            Row(
                modifier = Modifier.border(
                    border = BorderStroke(
                        width = 1.dp, color = MaterialTheme.colors.primary
                    ), shape = CircleShape
                )
            ) {
                Box(
                    modifier = Modifier.size(size), contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(size / 2),
                        painter = painterResource("drawable/ic_moon.xml"),
                        contentDescription = "Theme Icon",
                    )
                }
                Box(
                    modifier = Modifier.size(size), contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(size / 2),
                        painter = painterResource("drawable/bulb_on_icon.xml"),
                        contentDescription = "Theme Icon",
                        tint = if (appTheme.value == Theme.DARK) MaterialTheme.colors.primary
                        else MaterialTheme.colors.secondaryVariant
                    )
                }
            }
        }
    }
}

@Composable
fun donateButton(goldenMode: MutableState<Boolean>, appTheme: MutableState<Theme>) {
    Button(
        onClick = {
            goldenMode.value = true
            appTheme.value = Theme.GOLDEN
        }, colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary, contentColor = MaterialTheme.colors.onPrimary
        )
    ) {
        Icon(
            imageVector = Icons.Default.MailOutline,
            contentDescription = "Donate",
            tint = MaterialTheme.colors.onPrimary
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text("Donate", color = MaterialTheme.colors.onPrimary)
    }
}
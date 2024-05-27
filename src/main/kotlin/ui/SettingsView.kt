package ui

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import data.db.sqlite_exposed.setTheme
import ui.theme.BdsmAppTheme
import ui.theme.Theme
import java.awt.Dimension
import java.io.File
import javax.sound.sampled.AudioSystem

@Composable
fun SettingsView(onClose: () -> Unit, appTheme: MutableState<Theme>) {
    val showSberIcon = mutableStateOf(false)
    Window(
        title = "Settings",
        onCloseRequest = onClose,
    ) {
        window.minimumSize = Dimension(600, 400)
        BdsmAppTheme(appTheme = appTheme.value) {
            Column(
                modifier = Modifier.background(MaterialTheme.colors.background).fillMaxSize().padding(20.dp).testTag("SettingsWindowTag")
                ,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                if (showSberIcon.value) {
                    Icon(
                        painter = painterResource("drawable/ic_sber.xml"),
                        contentDescription = "Sber Icon",
                        tint = Color(0xFF21a038),
                        modifier = Modifier.fillMaxSize().clickable(onClick = {
                            showSberIcon.value = false
                            appTheme.value = Theme.GOLDEN
                            setTheme(Theme.GOLDEN)
                        })
                    )
                    playSound("src/main/resources/sounds/paid.wav")
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        darkModeToggle(appTheme)
                        if (appTheme.value == Theme.GOLDEN) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Golden Mode", fontSize = 22.sp)
                                Spacer(modifier = Modifier.width(30.dp))
                                Checkbox(
                                    checked = appTheme.value == Theme.GOLDEN, onCheckedChange = {
                                        appTheme.value = Theme.LIGHT
                                        setTheme(Theme.LIGHT)
                                    }, modifier = Modifier.size(40.dp), colors = CheckboxDefaults.colors(
                                        checkedColor = MaterialTheme.colors.secondary,
                                        uncheckedColor = MaterialTheme.colors.primary
                                    )
                                )
                            }
                        }
                    }

                    if (appTheme.value != Theme.GOLDEN) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Text("Golden Theme за", fontSize = 22.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            donateButton() {
                                showSberIcon.value = true
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }
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
                    setTheme(Theme.LIGHT)
                } else {
                    appTheme.value = Theme.DARK
                    setTheme(Theme.DARK)
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
            )
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
fun donateButton(
    onClick: () -> Unit
) {
    Button(
        onClick = {
            onClick()
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

fun playSound(soundFileName: String) {
    val audioInputStream = AudioSystem.getAudioInputStream(File(soundFileName))
    val clip = AudioSystem.getClip()
    clip.open(audioInputStream)
    clip.start()
}
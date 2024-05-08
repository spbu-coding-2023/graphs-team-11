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
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window

@Composable
fun SettingsView(onClose: () -> Unit) {
    Window(
        title = "Settings",
        onCloseRequest = onClose,
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                darkModeToggle()

            }
        }
    }
}

@Composable
fun darkModeToggle() {
    val darkTheme by remember { mutableStateOf(false) }
    val size = 40.dp
    val animationSpec: AnimationSpec<Dp> = tween(durationMillis = 300)
    val offset by animateDpAsState(
        targetValue = if (darkTheme) 0.dp else size, animationSpec = animationSpec, label = ""
    )
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Text("Dark Mode", fontSize = 22.sp)
        Spacer(modifier = Modifier.width(30.dp))
        // Switch to toggle dark mode
        Box(modifier = Modifier
            .width(size * 2)
            .height(size)
            .clip(shape = CircleShape)
            .clickable {
//                onClick()
//                darkModePreferencesManager.setDarkModeEnabled(!darkTheme)
//                darkModePreferencesManager.setDarkMode()
            }
            .background(MaterialTheme.colors.surface.copy(alpha = 0.2f))) {
            Box(
                modifier = Modifier
                    .size(size)
                    .offset(x = offset)
                    .padding(all = 5.dp)
                    .clip(shape = CircleShape)
                    .background(MaterialTheme.colors.primary)
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
                        modifier = Modifier.size(size/2),
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Theme Icon",
                    )
                }
                Box(
                    modifier = Modifier.size(size), contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(size/2),
                        imageVector = Icons.Default.MailOutline,
                        contentDescription = "Theme Icon",
                        tint = if (darkTheme) MaterialTheme.colors.primary
                        else MaterialTheme.colors.secondaryVariant
                    )
                }
            }
        }
    }
}
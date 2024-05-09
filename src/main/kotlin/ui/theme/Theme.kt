package ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun darkThemeColors(): Colors {
    return MaterialTheme.colors.copy(
        background = ThemeColors.Dark.background,
        primary = ThemeColors.Dark.primary,
        onPrimary = ThemeColors.Dark.text,
        surface = ThemeColors.Dark.surface,
        secondary = ThemeColors.Dark.secondary
    )
}

@Composable
fun lightThemeColors(): Colors {
    return MaterialTheme.colors.copy(
        background = ThemeColors.Light.background,
        primary = ThemeColors.Light.primary,
        onPrimary = ThemeColors.Light.text,
        surface = ThemeColors.Light.surface,
        secondary = ThemeColors.Light.secondary

    )
}

@Composable
fun goldenThemeColors(): Colors {
    return MaterialTheme.colors.copy(
        background = ThemeColors.Golden.background,
        primary = ThemeColors.Golden.primary,
        onPrimary = ThemeColors.Golden.text,
        surface = ThemeColors.Golden.surface,
        secondary = ThemeColors.Golden.secondary
    )
}

@Composable
fun BdsmAppTheme(
    appTheme: Theme, content: @Composable () -> Unit
) {
    val colorScheme = when (appTheme) {
        Theme.DARK -> darkThemeColors()
        Theme.LIGHT -> lightThemeColors()
        else -> goldenThemeColors()
    }

    MaterialTheme(
        colors = colorScheme, content = content
    )
}

enum class Theme {
    LIGHT, DARK, GOLDEN
}
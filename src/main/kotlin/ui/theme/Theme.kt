/*
 *
 *  * This file is part of BDSM Graphs.
 *  *
 *  * BDSM Graphs is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * BDSM Graphs is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with . If not, see <https://www.gnu.org/licenses/>.
 *
 */

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
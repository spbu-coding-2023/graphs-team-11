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

import androidx.compose.ui.graphics.Color

// Light Colors
val SmoothWhite = Color(0xFFfafafa)
val SmoothGray = Color(0xFF909099)
val LightIndigo = Color(0xFFC9B1f7)
val PaleBlue = Color(0xff99c0ff)

// Night Colors
val DeepDark = Color(0xFF343434)
val SoftDark = Color(0xff6c6c6c)
val SmoothBlue = Color(0xFF93d7df)
val SmoothPurple = Color(0xFFa885ee)


sealed class ThemeColors(
    val background: Color,
    val surface: Color,
    val primary: Color,
    val text: Color,
    val secondary: Color,
) {
    data object Dark : ThemeColors(
        background = DeepDark,
        surface = SmoothBlue,
        primary = SoftDark,
        text = Color.White,
        secondary = SmoothPurple,
    )

    data object Light : ThemeColors(
        background = SmoothWhite,
        surface = PaleBlue,
        primary = SmoothGray,
        text = Color.Black,
        secondary = LightIndigo,
    )

    data object Golden : ThemeColors(
        background = Color(0xFFe9f01a),
        surface = Color(0xFFf5f5dc),
        primary = Color(0xFFe9ee45),
        text = Color.Black,
        secondary = Color(0xFFb9bd37),
    )
}
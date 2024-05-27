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

package model.graph_modelTests.graph_model_actions

import androidx.compose.ui.graphics.Color
import model.graph_model.graph_model_actions.NodeViewUpdate
import model.graph_model.graph_model_actions.Update
import model.graph_model.graph_model_actions.VertViewUpdate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UpdateTest {

    @Test
    fun `Plus that don't overwrite`() {
        val update1 = Update(
            nodeViewUpdate = mutableMapOf("1" to NodeViewUpdate(radius = -200f)),
            vertViewUpdate = mutableMapOf("1" to mutableMapOf("2" to VertViewUpdate(color = Color.Black)))
        )

        val update2 = Update(
            nodeViewUpdate = mutableMapOf("1" to NodeViewUpdate(color = Color.Red)),
            vertViewUpdate = mutableMapOf("1" to mutableMapOf("2" to VertViewUpdate(alpha = 0f)))
        )

        val expected = Update(
            nodeViewUpdate = mutableMapOf("1" to NodeViewUpdate(radius = -200f, color = Color.Red)),
            vertViewUpdate = mutableMapOf("1" to mutableMapOf("2" to VertViewUpdate(color = Color.Black, alpha = 0f)))
        )

        assertEquals((update1 + update2).nodeViewUpdate, expected.nodeViewUpdate)
        assertEquals((update1 + update2).vertViewUpdate, expected.vertViewUpdate)
    }

    @Test
    fun `Plus that overwrite`() {
        val update1 = Update(
            nodeViewUpdate = mutableMapOf("1" to NodeViewUpdate(radius = -200f)),
            vertViewUpdate = mutableMapOf("1" to mutableMapOf("2" to VertViewUpdate(color = Color.Black, alpha = 1f)))
        )

        val update2 = Update(
            nodeViewUpdate = mutableMapOf("1" to NodeViewUpdate(radius = -100f)),
            vertViewUpdate = mutableMapOf("1" to mutableMapOf("2" to VertViewUpdate(alpha = 0f)))
        )

        val expected = Update(
            nodeViewUpdate = mutableMapOf("1" to NodeViewUpdate(radius = -100f)),
            vertViewUpdate = mutableMapOf("1" to mutableMapOf("2" to VertViewUpdate(color = Color.Black, alpha = 0f)))
        )

        assertEquals((update1 + update2).nodeViewUpdate, expected.nodeViewUpdate)
        assertEquals((update1 + update2).vertViewUpdate, expected.vertViewUpdate)
    }
}
package model.graph_modelTests.graph_model_actions

import androidx.compose.ui.graphics.Color
import model.graph_model.graph_model_actions.NodeViewUpdate
import model.graph_model.graph_model_actions.Update
import model.graph_model.graph_model_actions.VertViewUpdate
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

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
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

package dataTest

import data.tools.graphGenerators.flowerSnark
import data.tools.graphGenerators.randomTree
import data.tools.graphGenerators.starDirected
import data.tools.graphGenerators.starUndirected
import model.graph_model.Graph
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GraphGeneratorsTest {
    private val nodeAmounts = listOf(10, 88, 100, 500)

    @Nested
    inner class GraphWithWeightOne {

        @TestFactory
        fun `test Random generator returns graph with right amount of nodes and weight one`(): List<DynamicTest> {
            val maxWeight = 1
            return nodeAmounts.map { nodeAmount ->
                DynamicTest.dynamicTest("Test with $nodeAmount nodes") {
                    val graph: Graph = randomTree(nodeAmount, maxWeight)
                    assertEquals(nodeAmount, graph.size)
                    graph.vertices.values.flatten().forEach { (_, weight) ->
                        assertEquals(maxWeight.toFloat(), weight)
                    }
                }
            }
        }

        @TestFactory
        fun `test Flower Snark generator returns graph with right amount of nodes, type and weight one`(): List<DynamicTest> {
            return nodeAmounts.map { nodeAmount ->
                DynamicTest.dynamicTest("Test with $nodeAmount * 4 nodes") {
                    val graph: Graph = flowerSnark(nodeAmount)
                    assertEquals(nodeAmount * 4, graph.size)

                    graph.vertices.values.flatten().forEach { (_, weight) ->
                        assertEquals(1f, weight)
                    }
                }
            }
        }

        @TestFactory
        fun `test Star Directed generator returns graph with right amount of nodes, type and weight one`(): List<DynamicTest> {
            return nodeAmounts.map { nodeAmount ->
                DynamicTest.dynamicTest("Test with $nodeAmount nodes") {
                    val graph: Graph = starDirected(nodeAmount)
                    assertEquals(nodeAmount, graph.size)

                    graph.vertices.values.flatten().forEach { (_, weight) ->
                        assertEquals(1f, weight)
                    }
                }
            }
        }

        @TestFactory
        fun `test Star Undirected generator returns graph with right amount of nodes, type and weight one`(): List<DynamicTest> {
            return nodeAmounts.map { nodeAmount ->
                DynamicTest.dynamicTest("Test with $nodeAmount nodes and type") {
                    val graph: Graph = starUndirected(nodeAmount)
                    assertEquals(nodeAmount, graph.size)

                    graph.vertices.values.flatten().forEach { (_, weight) ->
                        assertEquals(1f, weight)
                    }
                }
            }
        }
    }

    @Nested
    inner class RandomGraphWithRandomWeight {
        private val maxWeight = 100

        @TestFactory
        fun `test random generator returns graph with right amount of nodes, type and random weight`(): List<DynamicTest> {
            return nodeAmounts.map { nodeAmount ->
                DynamicTest.dynamicTest("Test with $nodeAmount nodes") {
                    val graph: Graph = randomTree(nodeAmount, maxWeight)
                    assertEquals(nodeAmount, graph.size)

                    graph.vertices.values.flatten().forEach { (_, weight) ->
                        assertTrue { weight <= maxWeight.toFloat() && weight > 0f }
                    }
                }
            }
        }
    }

    @Nested
    inner class InvariantTests {
        @TestFactory
        fun `test generators generate nodes that connected to nodes only from graph`(): List<DynamicTest> {
            val nodeAmount = 100
            val generators = listOf(::randomTree, ::flowerSnark, ::starDirected, ::starUndirected)
            return generators.map { generator ->
                DynamicTest.dynamicTest("Test ${generator.name}") {
                    val graph: Graph = if (generator.name == "randomTree") {
                        generator.call(nodeAmount, 1, true)
                    } else {
                        generator.call(nodeAmount)
                    }
                    val vertices = graph.vertices.keys.toList()
                    graph.vertices.values.flatten().forEach { (node, _) ->
                        assertTrue { vertices.contains(node) }
                    }
                }
            }
        }
    }
}
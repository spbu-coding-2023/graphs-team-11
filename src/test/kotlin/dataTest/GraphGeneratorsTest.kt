package dataTest

import data.tools.graphGenerators.flowerSnark
import data.tools.graphGenerators.randomTree
import data.tools.graphGenerators.starDirected
import data.tools.graphGenerators.starUndirected
import model.graph_model.Graph
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestFactory
import viewmodel.IntroWindowVM
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GraphGeneratorsTest {
    private val nodeAmounts = listOf(10, 88, 100, 500)
    private val types = listOf(
        IntroWindowVM.GraphKeyType.INT,
        IntroWindowVM.GraphKeyType.STRING,
        IntroWindowVM.GraphKeyType.FLOAT,
    )

    @Nested
    inner class RandomGraphWithWeightOne {

        @TestFactory
        fun `test Random generator returns graph with right amount of nodes, type and weight one`(): List<DynamicTest> {
            val maxWeight = 1
            return types.flatMap { type ->
                nodeAmounts.map { nodeAmount ->
                    DynamicTest.dynamicTest("Test with $nodeAmount nodes and type ${type.name}") {
                        val graph: Graph<*> = randomTree(nodeAmount, type, maxWeight)
                        assertEquals(nodeAmount, graph.size)
                        when (type) {
                            IntroWindowVM.GraphKeyType.INT -> assertTrue(graph.vertices.keys.first() is Int)
                            IntroWindowVM.GraphKeyType.STRING -> assertTrue(graph.vertices.keys.first() is String)
                            IntroWindowVM.GraphKeyType.FLOAT -> assertTrue(graph.vertices.keys.first() is Float)
                        }

                        graph.vertices.values.flatten().forEach { (_, weight) ->
                            assertEquals(maxWeight.toFloat(), weight)
                        }
                    }
                }
            }
        }

        @TestFactory
        fun `test Flower Snark generator returns graph with right amount of nodes, type and weight one`(): List<DynamicTest> {
            return types.flatMap { type ->
                nodeAmounts.map { nodeAmount ->
                    DynamicTest.dynamicTest("Test with $nodeAmount * 4 nodes and type ${type.name}") {
                        val graph: Graph<*> = flowerSnark(nodeAmount, type)
                        assertEquals(nodeAmount * 4, graph.size)
                        when (type) {
                            IntroWindowVM.GraphKeyType.INT -> assertTrue(graph.vertices.keys.first() is Int)
                            IntroWindowVM.GraphKeyType.STRING -> assertTrue(graph.vertices.keys.first() is String)
                            IntroWindowVM.GraphKeyType.FLOAT -> assertTrue(graph.vertices.keys.first() is Float)
                        }

                        graph.vertices.values.flatten().forEach { (_, weight) ->
                            assertEquals(1f, weight)
                        }
                    }
                }
            }
        }

        @TestFactory
        fun `test Star Directed generator returns graph with right amount of nodes, type and weight one`(): List<DynamicTest> {
            return types.flatMap { type ->
                nodeAmounts.map { nodeAmount ->
                    DynamicTest.dynamicTest("Test with $nodeAmount nodes and type ${type.name}") {
                        val graph: Graph<*> = starDirected(nodeAmount, type)
                        assertEquals(nodeAmount, graph.size)
                        when (type) {
                            IntroWindowVM.GraphKeyType.INT -> assertTrue(graph.vertices.keys.first() is Int)
                            IntroWindowVM.GraphKeyType.STRING -> assertTrue(graph.vertices.keys.first() is String)
                            IntroWindowVM.GraphKeyType.FLOAT -> assertTrue(graph.vertices.keys.first() is Float)
                        }

                        graph.vertices.values.flatten().forEach { (_, weight) ->
                            assertEquals(1f, weight)
                        }
                    }
                }
            }
        }

        @TestFactory
        fun `test Star Undirected generator returns graph with right amount of nodes, type and weight one`(): List<DynamicTest> {
            return types.flatMap { type ->
                nodeAmounts.map { nodeAmount ->
                    DynamicTest.dynamicTest("Test with $nodeAmount nodes and type ${type.name}") {
                        val graph: Graph<*> = starUndirected(nodeAmount, type)
                        assertEquals(nodeAmount, graph.size)
                        when (type) {
                            IntroWindowVM.GraphKeyType.INT -> assertTrue(graph.vertices.keys.first() is Int)
                            IntroWindowVM.GraphKeyType.STRING -> assertTrue(graph.vertices.keys.first() is String)
                            IntroWindowVM.GraphKeyType.FLOAT -> assertTrue(graph.vertices.keys.first() is Float)
                        }

                        graph.vertices.values.flatten().forEach { (_, weight) ->
                            assertEquals(1f, weight)
                        }
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
            return types.flatMap { type ->
                nodeAmounts.map { nodeAmount ->
                    DynamicTest.dynamicTest("Test with $nodeAmount nodes and type ${type.name}") {
                        val graph: Graph<*> = randomTree(nodeAmount, type, maxWeight)
                        assertEquals(nodeAmount, graph.size)
                        when (type) {
                            IntroWindowVM.GraphKeyType.INT -> assertTrue(graph.vertices.keys.first() is Int)
                            IntroWindowVM.GraphKeyType.STRING -> assertTrue(graph.vertices.keys.first() is String)
                            IntroWindowVM.GraphKeyType.FLOAT -> assertTrue(graph.vertices.keys.first() is Float)
                        }

                        graph.vertices.values.flatten().forEach { (_, weight) ->
                            assertTrue { weight <= maxWeight.toFloat() && weight > 0f }
                        }
                    }
                }
            }
        }
    }
}
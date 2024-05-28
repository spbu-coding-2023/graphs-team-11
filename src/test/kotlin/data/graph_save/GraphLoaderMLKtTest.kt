/*
 * This file is part of BDSM Graphs.
 * 
 * BDSM Graphs is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BDSM Graphs is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with . If not, see <https://www.gnu.org/licenses/>.
 */

package data.graph_save

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import model.graph_model.UndirectedGraph
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File
import kotlin.test.assertFailsWith


/**
 * Test class to check the proccesFileLoad function.
 * Accepts a graphML file and returns a Graph instance.
 */

class GraphLoaderMLKtTest {

    // Positive

    /**
     * `loadGraphML empty grahp` - test of correct loading of an empty graph.
     */
    @Test
    fun `loadGraphML empty grahp`() {
        val lines = """"""

        val graph = proccesFileLoad(lines)

        assert(graph.vertices.isEmpty())
    }

    /**
     * `load correct` - test of correct loading of a correct graph
     */
    @Test
    fun `load correct`() {
        val lines = """<?xml version="1.0" encoding="UTF-8"?>
                    <graphml xmlns="http://graphml.graphdrawing.org/xmlns"  
                        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                        xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns
                         http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">
                      <graph id="G" edgedefault="directed">
                        <node id="n0"/>
                        <node id="n1"/>
                        <node id="n2"/>
                        <node id="n3"/>
                        <edge source="n0" target="n2"/>
                        <edge source="n1" target="n2"/>
                        <edge source="n2" target="n3"/>
                      </graph>
                    </graphml>
                    """
        val graph = proccesFileLoad(lines)

        val expected = mutableMapOf(
            "n0" to mutableSetOf(Pair("n2", 1f)),
            "n1" to mutableSetOf(Pair("n2", 1f)),
            "n2" to mutableSetOf(Pair("n3", 1f)),
            "n3" to mutableSetOf()
        )

        assert(graph !is UndirectedGraph)
        assertEquals(graph.vertices, expected)
    }

    /**
     * `load correct with node names` - test of correct loading of a graph that has the node name attribute
     */
    @Test
    fun `load correct with node names`() {
        val lines = """<?xml version="1.0" encoding="UTF-8"?>
                    <graphml xmlns="http://graphml.graphdrawing.org/xmlns"  
                        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                        xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns
                         http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">
                         <key id="d0" for="node" attr.name="color" attr.type="string">
                      <graph id="G" edgedefault="directed">
                        <node id="n0">
                            <data key="d0">green</data>
                        </node>
                        <node id="n0">
                            <data key="d0">red</data>
                        </node>
                        <node id="n0">
                            <data key="d0">blue</data>
                        </node>
                      </graph>
                    </graphml>
                    """
        val graph = proccesFileLoad(lines)

        val expected = mutableMapOf<String, MutableSet<Pair<String, Float>>>(
            "green" to mutableSetOf(),
            "red" to mutableSetOf(),
            "blue" to mutableSetOf(),
        )

        assert(graph !is UndirectedGraph)
        assertEquals(graph.vertices, expected)
    }

    /**
     * `load correct with edge weight` - test of correct loading of a weighted graph
     */
    @Test
    fun `load correct with edge weight`() {
        val lines = """<?xml version="1.0" encoding="UTF-8"?>
                    <graphml xmlns="http://graphml.graphdrawing.org/xmlns"  
                        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                        xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns
                         http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">
                         <key id="d1" for="edge" attr.name="weight" attr.type="double"/>
                      <graph id="G" edgedefault="directed">
                        <node id="n4">
                        </node>
                        <node id="n5">
                        </node>
                        <edge id="e6" source="n5" target="n4">
                          <data key="d1">1.1</data>
                        </edge>
                      </graph>
                    </graphml>
                    """
        val graph = proccesFileLoad(lines)

        val expected = mutableMapOf<String, MutableSet<Pair<String, Float>>>(
            "n4" to mutableSetOf(),
            "n5" to mutableSetOf(Pair("n4", 1.1f)),
        )

        assert(graph !is UndirectedGraph)
        assertEquals(graph.vertices, expected)
    }

    // Negative

    /**
     * `No node exit at the end` - test of correct error handling in case of absence of closing tag before </graph> tag
     */
    @Test
    fun `No node exit at the end`() {
        val exception = assertFailsWith<NullPointerException> {
            val lines = """<?xml version="1.0" encoding="UTF-8"?>
                    <graphml xmlns="http://graphml.graphdrawing.org/xmlns"  
                        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                        xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns
                         http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">
                         <key id="d1" for="edge" attr.name="weight" attr.type="double"/>
                      <graph id="G" edgedefault="directed">
                        <node id="n4">
                      </graph>
                    </graphml>
                    """
            val graph = proccesFileLoad(lines)
            println(graph.vertices)
        }

        assertEquals(exception.message, "Invalid File Format: some tag isn't close")
    }

    /**
     * `No node exit` - test of correct error handling in case of absence of closing tag of node
     */
    @Test
    fun `No node exit`() {
        val exception = assertFailsWith<NullPointerException> {
            val lines = """<?xml version="1.0" encoding="UTF-8"?>
                    <graphml xmlns="http://graphml.graphdrawing.org/xmlns"  
                        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                        xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns
                         http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">
                         <key id="d1" for="edge" attr.name="weight" attr.type="double"/>
                      <graph id="G" edgedefault="directed">
                        <node id="n4">
                        <node id="n5">
                      </graph>
                    </graphml>
                    """
            val graph = proccesFileLoad(lines)
            println(graph.vertices)
        }

        assert(exception.message!!.startsWith("Invalid File Format: No node exit"))
    }

    /**
     * `No node edge` - test of correct error handling in case of absence of closing tag of edge
     */
    @Test
    fun `No edge exit`() {
        val exception = assertFailsWith<NullPointerException> {
            val lines = """<?xml version="1.0" encoding="UTF-8"?>
                    <graphml xmlns="http://graphml.graphdrawing.org/xmlns"  
                        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                        xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns
                         http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">
                         <key id="d1" for="edge" attr.name="weight" attr.type="double"/>
                      <graph id="G" edgedefault="directed">
                        <node id="n4"/>
                        <node id="n5"/>
                        <edge id="e0" source="n4" target="n5">
                        <edge id="e1" source="n5" target="n4"/>
                      </graph>
                    </graphml>
                    """
            val graph = proccesFileLoad(lines)
            println(graph.vertices)
        }
        println(exception.message)
        assert(exception.message!!.startsWith("Invalid File Format: No edge exit"))
    }

    /**
     * `No node id` - test of correct error handling in case of absence of id in node tag
     */
    @Test
    fun `No node id`() {
        val exception = assertFailsWith<NullPointerException> {
            val lines = """<?xml version="1.0" encoding="UTF-8"?>
                    <graphml xmlns="http://graphml.graphdrawing.org/xmlns"  
                        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                        xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns
                         http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">
                         <key id="d1" for="edge" attr.name="weight" attr.type="double"/>
                      <graph id="G" edgedefault="directed">
                        <node id="n4"/>
                        <node />
                      </graph>
                    </graphml>
                    """
            val graph = proccesFileLoad(lines)
        }
        assert(exception.message!!.startsWith("Invalid File Format: No needed data"))
    }

    /**
     * `No edge source` - test of correct error handling in case of absence of source in node edge
     */
    @Test
    fun `No edge source`() {
        val exception = assertFailsWith<NullPointerException> {
            val lines = """<?xml version="1.0" encoding="UTF-8"?>
                    <graphml xmlns="http://graphml.graphdrawing.org/xmlns"  
                        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                        xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns
                         http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">
                         <key id="d1" for="edge" attr.name="weight" attr.type="double"/>
                      <graph id="G" edgedefault="directed">
                        <node id="n4"/>
                        <node id="n5"/>
                        <edge id="e0" target="n4">
                      </graph>
                    </graphml>
                    """
            val graph = proccesFileLoad(lines)
        }
        assert(exception.message!!.startsWith("Invalid File Format: No edge source or target"))
    }

    /**
     * `Invalid edge source` - test of correct error handling in case of absence of node with source id
     */
    @Test
    fun `Invalid edge source`() {
        val exception = assertFailsWith<NullPointerException> {
            val lines = """<?xml version="1.0" encoding="UTF-8"?>
                    <graphml xmlns="http://graphml.graphdrawing.org/xmlns"  
                        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                        xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns
                         http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">
                         <key id="d1" for="edge" attr.name="weight" attr.type="double"/>
                      <graph id="G" edgedefault="directed">
                        <node id="n4"/>
                        <node id="n5"/>
                        <edge id="e0" source="n3" target="n4"/>
                      </graph>
                    </graphml>
                    """
            val graph = proccesFileLoad(lines)
        }
        assert(exception.message!!.startsWith("Invalid File Format: No edge source or target"))
    }
}

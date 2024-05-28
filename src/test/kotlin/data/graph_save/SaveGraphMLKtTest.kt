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

import model.graph_model.Graph
import model.graph_model.UndirectedGraph
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

/**
 * A test class for testing the functionality of the save module,
 * more specifically the graphToStringML function.
 *
 * The function takes a Graph instance and returns its representation as a graphML string.
 */
class SaveGraphMLKtTest {

    /**
     * `empty graph Save` - test of correct saving of an empty graph
     */
    @Test
    fun `empty graph Save`() {
        val graphString = graphToStringML(Graph())

        val expected = """<?xml version="1.0" encoding="UTF-8"?>
<!-- This file was written by the JAVA GraphML Library.-->
<graphml xmlns="http://graphml.graphdrawing.org/xmlns"  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">
  <key id="weight" for="edge" attr.name="weight" attr.type="double"/>
        <graph edgedefault="directed">
</graph>
</graphml>"""

        assertEquals(graphString, expected)
    }

    /**
     * `empty undirected graph Save` - test of correct saving of an empty undirected graph
     */
    @Test
    fun `empty undirected graph Save`() {
        val graphString = graphToStringML(UndirectedGraph())

        val expected = """<?xml version="1.0" encoding="UTF-8"?>
<!-- This file was written by the JAVA GraphML Library.-->
<graphml xmlns="http://graphml.graphdrawing.org/xmlns"  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">
  <key id="weight" for="edge" attr.name="weight" attr.type="double"/>
        <graph edgedefault="undirected">
</graph>
</graphml>"""

        assertEquals(graphString, expected)
    }

    /**
     * `ultimate directed graph Save` - test of correct saving of directed graph
     */
    @Test
    fun `ultimate directed graph Save`() {
        val graph = Graph()
        graph.apply {
            addNode("1")
            addNode("2")
            addNode("3")
            addNode("4")

            addVertice("1", "2")
            addVertice("2", "3")
        }

        val graphString = graphToStringML(graph)

        val expected = """<?xml version="1.0" encoding="UTF-8"?>
<!-- This file was written by the JAVA GraphML Library.-->
<graphml xmlns="http://graphml.graphdrawing.org/xmlns"  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">
  <key id="weight" for="edge" attr.name="weight" attr.type="double"/>
        <graph edgedefault="directed">
<node id="1"/>
<node id="2"/>
<node id="3"/>
<node id="4"/>
<edge id="e0" source="1" target="2"/>
<edge id="e1" source="2" target="3"/>
</graph>
</graphml>"""

        assertEquals(graphString, expected)
    }

    /**
     * `ultimate directed graph Save` - test of correct saving of directed weighted graph
     */
    @Test
    fun `ultimate directed graph with weights Save`() {
        val graph = Graph()
        graph.apply {
            addNode("1")
            addNode("2")
            addNode("3")
            addNode("4")

            addVertice("1", "2", 1.1f)
            addVertice("2", "3", 2f)
        }

        val graphString = graphToStringML(graph)

        val expected = """<?xml version="1.0" encoding="UTF-8"?>
<!-- This file was written by the JAVA GraphML Library.-->
<graphml xmlns="http://graphml.graphdrawing.org/xmlns"  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">
  <key id="weight" for="edge" attr.name="weight" attr.type="double"/>
        <graph edgedefault="directed">
<node id="1"/>
<node id="2"/>
<node id="3"/>
<node id="4"/>
<edge id="e0" source="1" target="2">
<data key="weight">1.1</data>
</edge>
<edge id="e1" source="2" target="3">
<data key="weight">2.0</data>
</edge>
</graph>
</graphml>"""

        assertEquals(graphString, expected)
    }
}

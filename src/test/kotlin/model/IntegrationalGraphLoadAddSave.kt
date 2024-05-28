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

package model

import androidx.compose.ui.geometry.Offset
import data.graph_save.graphLoadUnified
import data.graph_save.graphSaveUnified
import data.graph_save.graphToStringML
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import model.graph_model.Graph
import model.graph_model.GraphViewClass
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.io.FileNotFoundException
import kotlin.io.path.Path
import kotlin.io.path.createFile
import kotlin.test.Test
import kotlin.test.assertEquals

class IntegrationalGraphLoadAddSave  {
    companion object StaticData {
        val fileName = "IntegrationalGraphLoadAddSave.graphml"
        val fileNameSecond = "IntegrationalGraphLoadAddSave2.graphml"

        @field:TempDir
        lateinit var tempFolder: File
        lateinit var graph: Graph
        lateinit var gv: GraphViewClass
        lateinit var path: String
        private lateinit var scope: CoroutineScope
        @JvmStatic
        @BeforeAll
        fun setup(): Unit {

            path = tempFolder.toString() + '\\' + fileName
            val stringGraph = """<?xml version="1.0" encoding="UTF-8"?>
    <!-- This file was written by the JAVA GraphML Library.-->
    <graphml xmlns="http://graphml.graphdrawing.org/xmlns"  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">
      <key id="d0" for="node" attr.name="color" attr.type="string">
        <default>yellow</default>
      </key>
      <key id="d1" for="edge" attr.name="weight" attr.type="double"/>
      <graph id="G" edgedefault="undirected">
      </graph>
    </graphml>"""

            val pathFile = Path(path).createFile().toFile()
            pathFile.writeText(stringGraph)

            scope = CoroutineScope(Dispatchers.IO)
        }
    }

    @Test
    @Order(1)
    fun `Load graph`() {
        graph = graphLoadUnified(path)
        assert(graph.vertices.isEmpty())
    }

    @Test
    @Order(2)
    fun `Get graphViewModelClass`() = runTest {
        gv = GraphViewClass(graph, scope = scope, isEmpty = true, afterLayout = null)
        gv.mainJob.join()

        assert(gv.nodesViews.isEmpty())
        assert(gv.vertViews.all { it.value.isEmpty() })

        gv.apply {
            addNode("n0", Offset(0f, 0f))
            addNode("n1", Offset(0f, 0f))
            addNode("n2", Offset(0f, 0f))

            addVert("n1", "n0", 1.1f)
            addVert("n2", "n0")
            addVert("n1", "n2", 2f)
        }

        val expected = mutableMapOf(
            "n0" to mutableSetOf(Pair("n1", 1.1f), Pair("n2", 1f)),
            "n1" to mutableSetOf(Pair("n0", 1.1f), Pair("n2", 2f)),
            "n2" to mutableSetOf(Pair("n0", 1f), Pair("n1", 2f)),
        )

        assertEquals(gv.graph.vertices, expected)

        graphSaveUnified(tempFolder.toString() + "\\" + fileNameSecond, gv.graph)
    }
}
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

package data.graph_save

import data.tools.graphGenerators.randomTree
import model.graph_model.Graph
import model.graph_modelTests.assertGraphInvariant
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals


class IntegrationalGraphGenerateSaveLoad {

    val fileName = "graph.graphml"

    companion object StaticData {
        @field:TempDir
        lateinit var tempFolder: File
        lateinit var graph: Graph
    }

    @Test
    @Order(1)
    fun `Generate and save graph`() {
        graph = randomTree(10, 2)

        assertGraphInvariant(graph)

        try {
            graphSaveUnified(tempFolder.toString() + '\\' + fileName, graph)
            assert(File(tempFolder.toString() + '\\' + fileName).exists())
        } catch (e: Exception) {
            println(e.message)
            assert(false)
        }
    }

    @Test
    @Order(2)
    fun `Load graph`() {
        val newGraph = graphLoadUnified(tempFolder.toString() + '\\' + fileName)

        assertGraphInvariant(newGraph)
        assertEquals(graph.vertices, newGraph.vertices)
    }
}
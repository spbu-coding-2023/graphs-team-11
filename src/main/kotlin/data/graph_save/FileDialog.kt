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

import model.graph_model.Graph
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

fun showSaveFileDialog(): File? {
    val fileDialog = FileDialog(null as Frame?, "Save Graph", FileDialog.SAVE)
    fileDialog.setFile("*.graphml")
    fileDialog.isVisible = true
    return if (fileDialog.file != null) {
        File(fileDialog.directory, fileDialog.file)
    } else {
        null
    }
}

fun onSaveFilePressed(graph: Graph) {
    val file = showSaveFileDialog()
    if (file != null) {
        val filePath = file.absolutePath
        println(filePath)
        // Here you can write the JSON to the file
        graphSaveUnified(filePath, graph)
    }
}
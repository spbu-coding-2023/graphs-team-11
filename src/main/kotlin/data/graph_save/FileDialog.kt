package data.graph_save

import data.Constants.FILE_SAVE_FORMAT_FILTER
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

fun <D> onSaveFilePressed(graph: Graph<D>) {
    val file = showSaveFileDialog()
    if (file != null) {
        val filePath = file.absolutePath
        println(filePath)
        // Here you can write the JSON to the file
        graphSaveUnified(filePath, graph)
    }
}
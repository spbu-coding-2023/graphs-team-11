package viewmodel

import androidx.compose.runtime.mutableStateOf
import data.Constants
import data.graph_save.graphSaveUnified
import ui.components.MyWindowState
import java.io.File
import java.nio.file.InvalidPathException

class SaverMV {
    val path = mutableStateOf(Constants.RESOURCE_DIR)
    val correct = mutableStateOf(SaverStates.CORRECT)

    fun pathCheck() {
        var typeCheak = false
        for (i in Constants.FILE_SAVE_FORMAT_FILTER) {
            // println(Pair(path.value, path.value.endsWith(i)))
            typeCheak = typeCheak || path.value.endsWith(i)
        }
        try {
            var directory = ""
            if ("\\" in path.value) directory = path.value.substring(0, path.value.lastIndexOf("\\"))
            // println(path.value)
            // println(Pair(typeCheak, File(directory).isDirectory))
            if (typeCheak &&  File(directory).isDirectory) correct.value = SaverStates.CORRECT
            else if (typeCheak) correct.value = SaverStates.DIR_ERROR
            else if (File(directory).isDirectory) correct.value = SaverStates.FORMAT_ERROR
            else correct.value = SaverStates.FORMAT_DIR_ERROR
        } catch (e: InvalidPathException) {
            println("IPE")
            correct.value = SaverStates.DIR_ERROR
        } catch (e: StringIndexOutOfBoundsException) {
            correct.value = SaverStates.FORMAT_ERROR
        }
    }

    fun fileSave(states: MyWindowState) {
        graphSaveUnified(path.value, states.graph!!)
    }
}

enum class SaverStates {
    CORRECT, FORMAT_ERROR, DIR_ERROR, FORMAT_DIR_ERROR
}
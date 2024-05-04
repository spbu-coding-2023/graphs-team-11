package ui.algoritms_view

import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.reflect.KClass

class AlgoritmFinder(
    var algoritms: MutableList<KClass<*>> = mutableListOf(),
    var demonAlgoritms: MutableList<KClass<*>> = mutableListOf()
) {
    init {
        val allAlgoritms = getAll("data.algoritms")
        for (i in allAlgoritms) {
            if (i.toString() == "class data.algoritms.DemonAlgoritm") continue
            for (j in i.supertypes) {
                if (j.toString() == "data.algoritms.Algoritm") {
                    algoritms.add(i)
                    break
                }
                if (j.toString() == "data.algoritms.DemonAlgoritm") {
                    demonAlgoritms.add(i)
                    break
                }
            }
        }
    }

    fun getAll(packageName: String): MutableList<KClass<*>> {
        var classLoader = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName.replace(".", "/"))
        var reader: BufferedReader = BufferedReader(InputStreamReader(classLoader!!))

        var readerNew = reader
            .lines()
            .filter {line -> line.endsWith(".class")}
            .map { line -> getClass(line, packageName) }

        var endArray: MutableList<KClass<*>> = mutableListOf()
        for (i in readerNew) {
            endArray.add(i)
        }
        return endArray
    }

    class NoClass() {}

    fun getClass(className: String, packageName: String): KClass<*> {
        try {
            return Class.forName(packageName + "." + className.substring(0, className.lastIndexOf("."))).kotlin
        } catch (e: ClassNotFoundException) {
            println("No such class")
        }
        return NoClass()::class
    }
}
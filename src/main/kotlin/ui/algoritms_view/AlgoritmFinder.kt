package ui.algoritms_view

import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.reflect.KClass

class AlgoritmFinder(
    var algoritms: MutableList<KClass<*>> = mutableListOf(),
    var demonAlgoritms: MutableList<KClass<*>> = mutableListOf()
) {
    init {
        val allAlgoritms = getAll("algorithm.main")
        for (i in allAlgoritms) {
            i.supertypes.forEach {
                print(it)
                print(" ")
            }
            println()
            if (i.toString() == "class algorithm.main.DemonAlgoritm") continue
            for (j in i.supertypes) {
                if (j.toString() == "algorithm.main.Algoritm") {
                    algoritms.add(i)
                    break
                }
                if (j.toString() == "algorithm.main.DemonAlgoritm") {
                    demonAlgoritms.add(i)
                    break
                }
            }
        }
        println(algoritms)
    }

    private fun getAll(packageName: String): MutableList<KClass<*>> {
        val classLoader = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName.replace(".", "/"))
        val reader = BufferedReader(InputStreamReader(classLoader!!))

        val readerNew =
            reader.lines().filter { line -> line.endsWith(".class") }.map { line -> getClass(line, packageName) }
        val endArray: MutableList<KClass<*>> = mutableListOf()
        for (i in readerNew) {
            endArray.add(i)
        }
        println(endArray)
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
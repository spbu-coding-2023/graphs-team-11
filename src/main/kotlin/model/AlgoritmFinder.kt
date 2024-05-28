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

import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.reflect.KClass

class AlgoritmFinder(
    var algoritms: MutableList<KClass<*>> = mutableListOf(),
    var demonAlgoritms: MutableList<KClass<*>> = mutableListOf()
) {
    init {
        val allAlgoritms = getAll("model.algoritms")
        println(allAlgoritms)
        for (i in allAlgoritms) {
            i.supertypes.forEach {
                // print(it)
                print(" ")
            }
            println()
            if (i.toString() == "class model.algoritms.DemonAlgoritm") continue
            for (j in i.supertypes) {
                if (j.toString() == "model.algoritms.Algoritm") {
                    algoritms.add(i)
                    break
                }
                if (j.toString() == "model.algoritms.DemonAlgoritm") {
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

    class NoClass

    fun getClass(className: String, packageName: String): KClass<*> {
        try {
            return Class.forName(packageName + "." + className.substring(0, className.lastIndexOf("."))).kotlin
        } catch (e: ClassNotFoundException) {
            println("No such class")
        }
        return NoClass()::class
    }
}
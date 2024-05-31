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

package ui.components


fun generateStringNodeNames(size: Int): List<String> {
    val nodeNames = mutableListOf<String>()
    var length = 1
    while (nodeNames.size < size) {
        generateNamesOfLength(length).forEach { name ->
            if (nodeNames.size < size) {
                nodeNames.add(name)
            }
        }
        length++
    }
    return nodeNames
}

private fun generateNamesOfLength(length: Int): List<String> {
    val range = 'A'..'Z'
    if (length == 1) {
        return range.map { it.toString() }
    } else {
        val shorterNames = generateNamesOfLength(length - 1)
        return range.flatMap { char -> shorterNames.map { name -> char + name } }
    }
}
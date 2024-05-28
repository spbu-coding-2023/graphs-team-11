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

package model.graph_model

class UndirectedGraph : Graph() {
    override fun addVertice(data1: String, data2: String, weight: Float) {
        this.vertices[data1]?.add(Pair(data2, weight))
        this.vertices[data2]?.add(Pair(data1, weight))
    }

    override fun deleteVertice(data1: String, data2: String) {
        super.deleteVertice(data1, data2)
        super.deleteVertice(data2, data1)
    }
}
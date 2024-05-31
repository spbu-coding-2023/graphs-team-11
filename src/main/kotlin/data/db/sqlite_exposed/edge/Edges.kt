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

package data.db.sqlite_exposed.edge

import data.db.sqlite_exposed.graph.Graphs
import org.jetbrains.exposed.dao.id.IntIdTable

object Edges : IntIdTable() {
    val graphId = reference("graph_id", Graphs.id)
    val edgeSource = varchar("source", length = 50)
    val edgeTarget = varchar("target", length = 50)
}

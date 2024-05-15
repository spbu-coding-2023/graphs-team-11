package model.algoritms

import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.Color
import model.graph_model.Graph
import model.graph_model.graph_model_actions.NodeViewUpdate
import model.graph_model.graph_model_actions.Update
import model.graph_model.graph_model_actions.VertViewUpdate
import kotlin.random.Random

class LeidenToRun : Algoritm {
    override fun <D> alogRun(graph: Graph<D>, selected: SnapshotStateMap<D, Int>): Update<D> {
        val leidenAlgorithm = LeidenAlgorithm<D>(graph)
        val communities = leidenAlgorithm.detectCommunities()

        val communityMap = mutableMapOf<Int, MutableList<D>>()
        var communityIndex = 0
        communities.forEach { (node, community) ->
            if (communityMap.containsKey(community)) {
                communityMap[community]!!.add(node)
            } else {
                communityMap[community] = mutableListOf(node)
            }
        }
//        communityMap.forEach { (_, nodes) ->
//            println("Community $communityIndex: $nodes")
//            ++communityIndex
//        }

        val colors: MutableMap<Int, Color> = mutableMapOf()
        for ((community, _) in communityMap) {
            colors[community] = Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
        }


        val updateNode: MutableMap<D, NodeViewUpdate<D>> = mutableMapOf()
        val updateVert: MutableMap<D, MutableMap<D, VertViewUpdate<D>>> = mutableMapOf()

        for ((node, community) in communities) {
            updateNode[node] = NodeViewUpdate(color = colors[community])
            updateVert[node] = mutableMapOf()
            for ((neibour, _) in graph.vertices.getOrDefault(node, mutableSetOf())) {
                if (neibour in communityMap[community]!!) {
                    updateVert[node]!![neibour] = VertViewUpdate(color = colors[community], alpha = 1f)
                } else {
                    updateVert[node]!![neibour] = VertViewUpdate(color = Color.Gray, alpha = 0.1f)
                }
            }
        }

        return Update(nodeViewUpdate = updateNode, vertViewUpdate = updateVert)
    }
}

class LeidenAlgorithm<D>(private val graph: Graph<D>) {
    private var communities = mutableMapOf<D, Int>()
    private var reversedGraph: Graph<D>

    init {
        initializeCommunities()
        reversedGraph = graph.reverse()
    }

    private fun initializeCommunities() {
        graph.vertices.keys.forEach { node ->
            communities[node] = graph.vertices.keys.indexOf(node)
        }
    }

    fun detectCommunities(): Map<D, Int> {
        var improvement = true
        while (improvement) {
            improvement = localMovingAlgorithm()
            refinePartition()
        }
        return communities
    }

    private fun localMovingAlgorithm(): Boolean {
        var improved = false
        graph.vertices.keys.shuffled().forEach { node ->
            val bestCommunity = findBestCommunity(node)
            if (bestCommunity != communities[node]) {
                communities[node] = bestCommunity
                improved = true
            }
        }
        return improved
    }

    private fun findBestCommunity(node: D): Int {
        val neighborCommunities = mutableSetOf<Int>()

        // Add communities of outgoing neighbors
        graph.vertices[node]?.forEach { (neighbor, _) ->
            neighborCommunities.add(communities[neighbor]!!)
            graph.vertices[neighbor]?.forEach { (inNeighbor, _) ->
                neighborCommunities.add(communities[inNeighbor]!!)
            }
        }

        // Add communities of incoming neighbors
        if (graph.vertices[node]?.size == 0) {
            reversedGraph.vertices[node]?.forEach { (inNeighbor, _) ->
                neighborCommunities.add(communities[inNeighbor]!!)
            }
        }
        return neighborCommunities.maxByOrNull { community ->
            val communityConnections = (graph.vertices[node] ?: emptyList()).count { edge ->
                communities[edge.first] == community || communities.containsKey(edge.first) && communities[edge.first] == community
            }
            communityConnections
        } ?: communities[node]!!
    }

    private fun refinePartition() {
        graph.vertices.keys.forEach { node ->
            val bestCommunity = findBestCommunity(node)
            if (bestCommunity != communities[node]) {
                communities[node] = bestCommunity
            }
        }
    }
}


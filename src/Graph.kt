import java.lang.IllegalArgumentException
import java.util.*
import kotlin.collections.HashSet

inline fun <T> Graph(init: Graph<T>.() -> Unit): Graph<T> {
    return Graph<T>().apply(init)
}

class Graph<T> {

    data class Node<T>(val value: T)
    private class AdjacentNode<T>(val node: Node<T>, val weight: Int)
    class Edge<T>(val source: Node<T>, val destination: Node<T>, val weight: Int = 1)

    private val adjacencyList = hashMapOf<Node<T>, HashSet<AdjacentNode<T>>>()

    fun addEdge(edge: Edge<T>) {
        check(adjacencyList[edge.source] != null || adjacencyList[edge.destination] != null) {
            throw IllegalArgumentException("Either source or destination node is missing in the graph")
        }
        adjacencyList[edge.source]!!.add(AdjacentNode(edge.destination, edge.weight))
    }

    fun addNode(node: Node<T>) {
        check(adjacencyList[node] == null) {
            throw IllegalArgumentException("Node is already present in the graph")
        }
        adjacencyList[node] = hashSetOf()
    }

    fun addNodes(nodes: List<Node<T>>) = nodes.forEach { addNode(it) }

    fun addEdges(edges: List<Edge<T>>) = edges.forEach { addEdge(it) }

    fun nodes(vararg nodes: Node<T>) = addNodes(nodes.toList())

    fun nodes(nodes: List<Node<T>>) = addNodes(nodes)

    fun edges(vararg edges: Edge<T>) = addEdges(edges.toList())

    fun edges(edges: List<Edge<T>>) = addEdges(edges)

    fun singleSourceShortestPathDAG(source: Node<T>): Map<Node<T>, Int> {
        val distanceTable = mutableMapOf<Node<T>, Int>()
        distanceTable[source] = 0
        var distance: Int
        val sorting = topologicalSorting()
        for (i in sorting.indexOf(source) until sorting.size) {
            adjacencyList[sorting[i]]!!.forEach { adjacentNode ->
                distance = distanceTable[sorting[i]]!! + adjacentNode.weight
                if (distanceTable[adjacentNode.node] == null || distance < distanceTable[adjacentNode.node]!!) {
                    distanceTable[adjacentNode.node] = distance
                }
            }
        }
        return distanceTable
    }

    //Topological sorting
    fun topologicalSorting(): List<Node<T>> {
        val sortingList = LinkedList<Node<T>>()
        val visitedNode = mutableSetOf<T>()
        adjacencyList.entries.forEach {
            if (!visitedNode.contains(it.key.value)) {
                topologicalDFS(it.key, visitedNode, sortingList)
            }
        }
        return sortingList
    }

    private fun topologicalDFS(at: Node<T>, visitedNode: MutableSet<T>, sortingList: LinkedList<Node<T>>) {
        if (visitedNode.contains(at.value)) return
        visitedNode.add(at.value)
        if (adjacencyList[at]!!.isEmpty()) {
            sortingList.addFirst(at)
            return
        }
        adjacencyList[at]!!.forEach {
            topologicalDFS(it.node, visitedNode, sortingList)
        }
        sortingList.addFirst(at)
    }


}
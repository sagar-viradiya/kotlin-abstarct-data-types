import java.lang.IllegalArgumentException
import java.util.*
import kotlin.collections.HashSet

/**
 * Utility lnline function to initialize graph
 * @param init A lambda with receiver on Graph wrapping graph initialization.
 * @return [Graph] object
 */
inline fun <T> Graph(init: Graph<T>.() -> Unit): Graph<T> {
    return Graph<T>().apply(init)
}

/**
 * A class representing graph Data structure.
 */
class Graph<T> {

    /**
     * A data class representing graph node.
     */
    data class Node<T>(val value: T)

    /**
     * A class representing graph edge.
     */
    class Edge<T>(val source: Node<T>, val destination: Node<T>, val weight: Int = 1)

    private class AdjacentNode<T>(val node: Node<T>, val weight: Int)
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

    /*
        Breadth first search
     */
    fun BFS(source: Node<T>, node: Node<T>): Boolean {
        val visitedNode = mutableSetOf<Node<T>>()
        val queue = ArrayDeque<Node<T>>()
        queue.add(source)
        var tempNode: Node<T>
        while (queue.isNotEmpty()) {
            tempNode = queue.remove()
            if (visitedNode.contains(tempNode)) continue
            if (tempNode == node) return true
            visitedNode.add(tempNode)
            queue.addAll(adjacencyList[tempNode]!!.map { it.node })
        }
        return false
    }

    /*
        Depth first search
     */
    fun DFS(source: Node<T>, node: Node<T>, visitedNode: MutableSet<Node<T>> = mutableSetOf()): Boolean {
        if (visitedNode.contains(source)) return false
        visitedNode.add(source)
        if (source == node) return true
        adjacencyList[source]!!.forEach {
            if (DFS(it.node, node, visitedNode)) return true
        }
        return false
    }

    /*fun DFSIterative(source: Node<T>, node: Node<T>) {
        val stack = Stack<Node<T>>()
        val visitedNode = mutableSetOf<Node<T>>()
        stack.push(source)
        var tempNode: Node<T>
        while (stack.isNotEmpty()) {
            tempNode = stack.pop()
            if (visitedNode.contains(tempNode)) continue
            
        }
    }*/

    /*
        Single source shortest path in direct acyclic graph
     */
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
fun main() {

    val nodeA = Graph.Node('A')
    val nodeB = Graph.Node('B')
    val nodeC = Graph.Node('C')
    val nodeD = Graph.Node('D')
    val nodeE = Graph.Node('E')
    val nodeF = Graph.Node('F')
    val nodeG = Graph.Node('G')
    val nodeH = Graph.Node('H')

    val graph = Graph<Char> {
        nodes(
            nodeA,
            nodeB,
            nodeC,
            nodeD,
            nodeE,
            nodeF,
            nodeG,
            nodeH
        )
        edges(
            Graph.Edge(nodeC, nodeD, 8),
            Graph.Edge(nodeC, nodeG, 11),
            Graph.Edge(nodeA, nodeB, 3),
            Graph.Edge(nodeA, nodeC, 6),
            Graph.Edge(nodeB, nodeD, 4),
            Graph.Edge(nodeB, nodeC, 4),
            Graph.Edge(nodeB, nodeE, 11),
            Graph.Edge(nodeD, nodeE, -4),
            Graph.Edge(nodeD, nodeF, 5),
            Graph.Edge(nodeD, nodeG, 2),
            Graph.Edge(nodeE, nodeH, 9),
            Graph.Edge(nodeF, nodeH, 1),
            Graph.Edge(nodeG, nodeH, 2)
        )
    }

    //print(graph.topologicalSorting())

    graph.singleSourceShortestPathDAG(nodeA).forEach {
        println("${it.key.value} -> ${it.value}")
    }

}
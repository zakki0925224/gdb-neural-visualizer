package ui

import scala.collection.mutable
import java.awt.Graphics
import java.awt.Color

class Node(val label: String, val x: Int, val y: Int) {
    private val edges: mutable.ListBuffer[Edge] = mutable.ListBuffer()

    def getEdges: List[Edge] = edges.toList

    def addEdge(edge: Edge): Unit = {
        edges += edge
    }

    def render(g: Graphics): Unit = {
        g.setColor(Color.BLUE)
        g.fillOval(x - 20, y - 20, 40, 40)
        g.setColor(Color.BLACK)
        g.drawString(label, x - 15, y + 5)
    }
}

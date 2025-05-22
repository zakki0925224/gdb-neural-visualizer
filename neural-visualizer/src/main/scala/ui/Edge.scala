package ui

import java.awt.Graphics
import java.awt.Color

class Edge(val startNode: Node, val endNode: Node) {
    def render(g: Graphics): Unit = {
        g.setColor(Color.RED)
        g.drawLine(startNode.x, startNode.y, endNode.x, endNode.y)
    }
}

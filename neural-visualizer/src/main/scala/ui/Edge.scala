package ui

import java.awt.Graphics
import java.awt.Color

class Edge(val startNode: Node, val endNode: Node) {
    private val color: Color = Color.RED

    def render(g: Graphics): Unit = {
        g.setColor(color)
        g.drawLine(startNode.x, startNode.y, endNode.x, endNode.y)
    }
}

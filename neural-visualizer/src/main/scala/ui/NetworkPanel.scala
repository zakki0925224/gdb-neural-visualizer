package ui

import javax.swing.JPanel
import scala.collection.mutable
import javax.swing.OverlayLayout
import java.awt.Point
import java.awt.Graphics
import data.DebugInfo
import scala.util.Random

class NetworkPanel extends JPanel {
    private val nodes: mutable.Map[String, Node] = mutable.Map()
    private val edges: mutable.ListBuffer[Edge]  = mutable.ListBuffer()
    private val nodeMargin                       = 20

    override def paintComponent(g: Graphics): Unit = {
        super.paintComponent(g)
        edges.foreach(_.render(g))
        nodes.values.foreach(_.render(g))
    }

    def updateNetwork(debugInfo: DebugInfo): Unit = {
        val fnName = debugInfo.frame.function.getOrElse("??")
        val rand   = new Random()
        val x      = nodeMargin + rand.nextInt(this.getWidth().max(1) - nodeMargin * 2)
        val y      = nodeMargin + rand.nextInt(this.getHeight().max(1) - nodeMargin * 2)

        val node = nodes.getOrElseUpdate(fnName, new Node(fnName, x, y))
        if (nodes.size > 1) {
            val prevNode = nodes.values.toSeq(nodes.size - 2)
            edges += new Edge(prevNode, node)
        }

        repaint()
    }
}

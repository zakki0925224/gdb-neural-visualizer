package ui

import scala.collection.mutable
import java.awt.Graphics
import java.awt.Color

class Node(val label: String, val x: Int, val y: Int) {
    private val radius: Int                     = 10
    private val color: Color                    = Color.BLUE
    private val labelColor: Color               = Color.BLACK
    private val labelFontSize: Int              = 12
    private val edges: mutable.ListBuffer[Edge] = mutable.ListBuffer()

    private val maxAccessCount: Int = 50
    private val minAccessCount: Int = -50
    private var accessCount: Int    = 0

    def getEdges: List[Edge] = edges.toList

    def addEdge(edge: Edge): Unit = {
        edges += edge
    }

    def dynamicColor(baseColor: Color): Color = {
        val rawNorm = ((accessCount - minAccessCount).toDouble / (maxAccessCount - minAccessCount)).max(0.0).min(1.0)
        val norm    = Math.pow(rawNorm, 0.2)
        val baseR   = baseColor.getRed
        val baseG   = baseColor.getGreen
        val baseB   = baseColor.getBlue
        val bg      = 255 // white
        val r       = (bg + (baseR - bg) * norm).toInt.min(255).max(0)
        val g       = (bg + (baseG - bg) * norm).toInt.min(255).max(0)
        val b       = (bg + (baseB - bg) * norm).toInt.min(255).max(0)
        new Color(r, g, b)
    }

    def incrementAccessCount(): Unit = {
        accessCount = (accessCount + 1).min(maxAccessCount)
    }

    def decrementAccessCount(): Unit = {
        accessCount = (accessCount - 1).max(minAccessCount)
    }

    def render(g: Graphics): Unit = {
        g.setColor(dynamicColor(color))
        g.fillOval(x - radius, y - radius, radius * 2, radius * 2)
        g.setColor(dynamicColor(labelColor))
        g.setFont(g.getFont.deriveFont(labelFontSize.toFloat))
        g.drawString(label, x, y)
    }
}

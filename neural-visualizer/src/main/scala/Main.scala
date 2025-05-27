import com.formdev.flatlaf.FlatLightLaf
import java.awt._
import javax.swing._
import scala.concurrent.{Future, blocking}
import scala.concurrent.ExecutionContext.Implicits.global
import java.net.{DatagramPacket, DatagramSocket, InetAddress}
import play.api.libs.json.Json
import data.DebugInfo
import ui.NetworkPanel

@main def main(): Unit = {
    UIManager.setLookAndFeel(new FlatLightLaf())

    val frame = new JFrame("neural-visualizer")
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    frame.setSize(600, 400)

    val topPanel = new JPanel(new BorderLayout())

    val logArea = new JTextArea(5, 50)
    logArea.setEditable(false)
    val logScrollPane = new JScrollPane(logArea)
    topPanel.add(logScrollPane, BorderLayout.NORTH)

    val connectButton = new JButton("Connect")
    val buttonPanel   = new JPanel(new FlowLayout(FlowLayout.LEFT))
    buttonPanel.add(connectButton)
    topPanel.add(buttonPanel, BorderLayout.SOUTH)

    val networkPanel = new NetworkPanel()
    // networkPanel.setPreferredSize(new Dimension(600, 400))
    val networkScrollPane = new JScrollPane(networkPanel)
    topPanel.add(networkScrollPane, BorderLayout.CENTER)

    frame.add(topPanel)
    frame.setVisible(true)

    val udpSocket = new DatagramSocket()

    // udp receive thread
    Future {
        val buf = new Array[Byte](2048)
        try {
            while (true) {
                val packet = new DatagramPacket(buf, buf.length)
                udpSocket.receive(packet)
                val json = new String(packet.getData, 0, packet.getLength, "UTF-8")

                try {
                    val debugInfo = Json.parse(json).as[DebugInfo]
                    SwingUtilities.invokeLater(() => {
                        logArea.append(s"[RECV ] $debugInfo\n")
                        logArea.setCaretPosition(logArea.getDocument.getLength)
                        networkPanel.updateNetwork(debugInfo)
                    })
                } catch {
                    case e: Exception =>
                        SwingUtilities.invokeLater(() => {
                            logArea.append(s"[ERROR] JSON parse error: ${e.getMessage}\n")
                        })
                }
            }
        } catch {
            case e: Exception =>
                SwingUtilities.invokeLater(() => {
                    logArea.append(s"[ERROR] Receive error: ${e.getMessage}\n")
                })
        }
    }

    connectButton.addActionListener { _ =>
        connectButton.setEnabled(false)

        Future {
            try {
                val message = "abc" // test message for tell ip address to server
                val buf     = message.getBytes("UTF-8")
                val address = InetAddress.getByName("127.0.0.1")
                val packet  = new DatagramPacket(buf, buf.length, address, 6666)

                blocking {
                    udpSocket.send(packet)
                }

                SwingUtilities.invokeLater(() => {
                    logArea.append(s"[SEND ] $message\n")
                    logArea.setCaretPosition(logArea.getDocument.getLength)
                })
            } catch {
                case e: Exception =>
                    SwingUtilities.invokeLater(() => {
                        logArea.append(s"[ERROR] Send error: ${e.getMessage}\n")
                    })
            } finally {
                SwingUtilities.invokeLater(() => {
                    connectButton.setEnabled(true)
                })
            }
        }
    }

    val lock = new Object()
    frame.addWindowListener(new java.awt.event.WindowAdapter {
        override def windowClosing(e: java.awt.event.WindowEvent): Unit = {
            lock.synchronized { lock.notify() }
        }
    })
    lock.synchronized { lock.wait() }
}

package jp.co.guru.MyMine

import org.scalatest.FunSuite
import java.awt.{Component => _, _ }
import scala.swing._
import java.awt.event.PaintEvent

/**
 * がわ
 */
class F extends scala.swing.Frame {
  title = "Frame test"
    
  contents = new FlowPanel {
    contents += new C()
  }
  
  override def closeOperation() = dispose()
}

/**
 * 盤面表示
 */
class C extends Component {
  peer.setPreferredSize(new Dimension(640, 480))

  def getModel(): Model = TestModel
  
  implicit class MyGraphics2D(g: Graphics2D) {
    def drawCenteredString(s: String, x: Int, y: Int, w: Int, h: Int) = {
          val fm = g.getFontMetrics()
    	  val b = fm.getStringBounds(s, g)
    	  val l = fm.getLineMetrics(s, g)
    	  g.drawString(s, x + (w - b.getWidth().toInt) / 2, 
    	      (y + (h - b.getHeight()) / 2 + l.getAscent()).toInt)
    }   
  }
  
  override protected def paintComponent(g: Graphics2D) {
    g.setColor(Color.RED)
    g.drawLine(10, 10, 100, 100)
    val clipRect = g.getClipBounds()
    
    val model = getModel()
    val xs = model.getX
    val ys = model.getY
    val ppt = (Math.min(clipRect.width / xs, clipRect.height / ys) * .9).toInt
    
    
    for (x <- 0 to xs - 1; y <- 0 to ys) {
    	val v = model.getValue(x, y)
    	putRect(v, x * ppt, y * ppt, ppt - 1, ppt - 1)
    	
  	    def putRect(v: Status, x: Int, y: Int, w: Int, h: Int) = {
    	  val s = v.toString
    	  g.drawRect(x, y, w, h)
    	  g.drawCenteredString(s, x, y, w, h)
    	}
    }
  }
}    

/**
 * データモデルインターフェイス
 */
trait Model {
  def getX(): Int
  def getY(): Int
  def getValue(x: Int, y: Int): Status
}

object TestModel extends Model {
  def getX() = 10 
  def getY() = 10 
  def getValue(x: Int, y: Int): Status = {
    return Status(x * y % 10)
  }  
}

class SwingSuite extends FunSuite {
  test("Frame") {
	val f = new F()
	f.visible = true
  }
}

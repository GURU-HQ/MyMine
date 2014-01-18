package jp.co.guru.MyMine

import com.typesafe.scalalogging.slf4j.Logging
import java.awt.{ Component => _, Frame => _, _ }
import scala.swing._
import org.scalatest.FunSuite
import scala.swing.event.MouseClicked

/**
 * がわ
 */
class F(model: Model) extends Frame {
  title = "Frame test"

  contents = new FlowPanel {
    contents += new C(model, MyRenderer)
  }

  override def closeOperation() = dispose()
}

/**
 * 盤面表示
 */
class C(model: Model, renderer: CellRenderer) extends Component with Logging {
  peer.setPreferredSize(new Dimension(640, 480))
  val xs = model.getX
  val ys = model.getY
  //logger.info(model.toString)
  
  def calcPPT(r: Dimension): Int = {
    (Math.min(r.width / xs, r.height / ys) * .9).toInt
  }
  
  listenTo(this.mouse.clicks)
  reactions += {
     case MouseClicked(source, point, modifiers, clicks, triggersPopup) =>
       val ppt = calcPPT(this.size)
       val x = (point.x / ppt) % xs 
       val y = (point.y / ppt) % ys
       
       println(point, modifiers, clicks)
  }
  
  override protected def paintComponent(g: Graphics2D) {
    val ppt =  calcPPT(g.getClipBounds().getSize())

    g.setColor(Color.RED)
    
    for (x <- 0 to xs - 1; y <- 0 to ys - 1) {
      val v = model.getValue(x, y)
      renderer.putCell(g, v, x * ppt, y * ppt, ppt - 1, ppt - 1)
    }
  }
  
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
    val f = new F(TestModel)
    f.visible = true
  }
  
  test("Swing Bord") {
    val b = new Bord(10, 10) with BordUI with Manip
    val s = new Splite(b)
    val f = new F(s)
    (0 to 9).foreach(i => b.put(i, i))
//    b.put(5, 5)
    info("\n" + b.display)
//    b.around(5, 5).foreach(tp => s.update(tp._1, tp._2))
    s.paint(7, 1)
    info("\n" + s.display)
    f.visible = true
  }
}

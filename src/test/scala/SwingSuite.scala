package jp.co.guru.MyMine

import com.typesafe.scalalogging.slf4j.Logging
import java.awt.{ Component => _, Frame => _, _ }
import scala.swing._
import org.scalatest.FunSuite
import scala.swing.event.MouseClicked
import Util._

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
 * 盤面表示とイベント捕捉
 */
class C(val model: Model, val renderer: CellRenderer) extends Component with Logging {
  peer.setPreferredSize(new Dimension(640, 480))
  val xs = model.getX
  val ys = model.getY
  //logger.info(model.toString)
  
  def calcPPT(r: Dimension): Int = {
    (Math.min(r.width / xs, r.height / ys) * .9).toInt
  }
  
  /**
   * 指定された位置のます番号を得る
   */
  def getPos(p: Point): POS = {
       val ppt = calcPPT(this.size)
       val x = (p.x / ppt) % xs 
       val y = (p.y / ppt) % ys
       (x, y)
  }
  
  listenTo(this.mouse.clicks)
  
  reactions += {
     case MouseClicked(source, point, 256, clicks, triggersPopup) =>
       println("Right", point, 256, clicks)
       model.flag(getPos(point))
       repaint
     case MouseClicked(source, point, modifiers, clicks, triggersPopup) =>
       println("Left", point, modifiers, clicks)
       model.open(getPos(point))
       repaint
  }
  
  override protected def paintComponent(g: Graphics2D) {
    super.paintComponent(g)
    val ppt =  calcPPT(g.getClipBounds().getSize())

    
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
  
  override def open(p: POS): Boolean = false
  override def flag(p: POS): Boolean = false

}

class SwingSuite extends FunSuite {
  ignore("Frame") {
    val f = new F(TestModel)
    f.visible = true
  }
  
  test("Swing Bord") {
    val b = new Bord(20, 20) with BordUI with Manip
    b.randBomb(50)
//    (0 to 9).foreach(i => b.put(i, i))
    
    val s = new Splite(b) with DispSplite
    val f = new F(s)
    info("\n" + b.display)
    s.paint(7, 1)
    info("\n" + s.display)
    f.visible = true
  }
}

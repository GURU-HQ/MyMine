package jp.co.guru.MyMine

import com.typesafe.scalalogging.slf4j.Logging
import java.awt.{ Component => _, Frame => _, _ }
import scala.swing._
import org.scalatest.FunSuite
import scala.swing.event.MouseClicked
import Util._

trait Controller {
  self: Component with Model with View =>
    
  listenTo(this.mouse.clicks)
  
  reactions += {
     case MouseClicked(source, point, 256, clicks, triggersPopup) =>
       println("Right", point, 256, clicks)
       flag(getPos(point))
       repaint
     case MouseClicked(source, point, modifiers, clicks, triggersPopup) =>
       println("Left", point, modifiers, clicks)
       open(getPos(point))
       repaint
  }
}

trait PPT {
  self: Component =>
  val xs: Int
  val ys: Int
  
  def calcPPT(r: Dimension): Int = {
    (Math.min(r.width / xs, r.height / ys) * .9).toInt
  }
}

trait View {
  self: Component with PPT =>
  val xs: Int
  val ys: Int

  /**
   * 指定された位置のます番号を得る
   */
  def getPos(p: Point): POS = {
       val ppt = calcPPT(this.size)
       val x = (p.x / ppt) % xs 
       val y = (p.y / ppt) % ys
       (x, y)
  }
}
/*
/**
 * 盤面表示とイベント捕捉
 */
class C2(val renderer: CellRenderer) extends Component with Model with PPT with Logging {
  peer.setPreferredSize(new Dimension(640, 480))
  val xs = getX
  val ys = getY
  //logger.info(model.toString)
  
  
  override protected def paintComponent(g: Graphics2D) {
    super.paintComponent(g)
    val ppt =  calcPPT(g.getClipBounds().getSize())

    for (x <- 0 to xs - 1; y <- 0 to ys - 1) {
      val v = getValue(x, y)
      renderer.putCell(g, v, x * ppt, y * ppt, ppt - 1, ppt - 1)
    }
  }
}



abstract class F2 extends Frame with Model {
  title = "Frame test"
  contents = new FlowPanel  {
    contents += new C2(MyRenderer) with View with Model {
    }
  }
  override def closeOperation() = dispose()
}
*/
class C2Suite extends FunSuite {
  test("C2 Bord") {
    val b = new Bord(10, 10) with BordUI with Manip
    val s = new Splite(b) with DispSplite
//    val f = new F2 with TestModel with 
    (0 to 9).foreach(i => b.put(i, i))
    info("\n" + b.display)
    s.paint(7, 1)
    info("\n" + s.display)
//    f.visible = true
  }
}
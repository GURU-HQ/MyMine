/**
 * SwingによるViewの実装
 */
package jp.co.guru.MyMine

import com.typesafe.scalalogging.slf4j.Logging
import java.awt.{ Component => _, Frame => _, _ }
import scala.swing._
import scala.swing.event.MouseClicked
import Util._

/**
 * がわ
 */
class F(val model: Model, val renderer: CellRenderer = MyRenderer) extends Frame {
  
  title = "Frame test"
  contents = new FlowPanel {
    contents += new C(model, renderer)
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

  /**
   * マウスイベント処理
   * ViewからModelを直接変更している (=> イクナイ）
   */
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

  /**
   * 盤面表示本体
   */
  override protected def paintComponent(g: Graphics2D) {
    super.paintComponent(g)
    val ppt = calcPPT(g.getClipBounds().getSize())

    for (x <- 0 to xs - 1; y <- 0 to ys - 1) {
      val v = model.getValue(x, y)
      renderer.putCell(g, v, x * ppt, y * ppt, ppt - 1, ppt - 1)
    }
  }
}

object SwingMain {
  def main(args: Array[String]) {
    val b = new Board(20, 20) with Manip
    b.randBomb(50)
    val s = new Splite(b)
    val f = new F(s, GoodRenderer)
    f.visible = true
  }
}
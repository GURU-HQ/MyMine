package jp.co.guru.MyMine

import com.typesafe.scalalogging.slf4j.Logging
import java.awt.{ Component => _, Frame => _, _ }
import scala.swing._
import org.scalatest.FunSuite
import scala.swing.event.MouseClicked
import Util._
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.ActorRef


trait MineComponent extends Component {
  val xs: Int
  val ys: Int
  
  def calcPPT(r: Dimension): Int = {
    (Math.min(r.width / xs, r.height / ys) * .9).toInt
  }
  def getPos(p: Point): POS
}

case class Flag(val p: POS) {}
case class Open(val p: POS) {}
case class Modified(val p: POS) {}

class Controller extends Actor {
  def receive = {
    case Open(p) => {
//      open(p)
      println("Open", p)
    }
    case Flag(p) => {
//      flag(p)
      println("Flag", p)
    }
  }
}

trait SwingController {
  self: MineComponent with Model =>
  listenTo(mouse.clicks)

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

/**
 * 盤面表示とイベント捕捉
 */
abstract class C2(val renderer: CellRenderer, controller: ActorRef, model: Model) extends MineComponent with Logging {
  peer.setPreferredSize(new Dimension(640, 480))
  val xs = model.getX
  val ys = model.getY
  //logger.info(model.toString)

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
   * イベントをControllerにキャッチアップする 
   */
  listenTo(mouse.clicks)
  reactions += {
    case MouseClicked(source, point, modifiers, clicks, triggersPopup) =>
      if (modifiers == 256) {
        controller ! Flag(getPos(point))        
      } else {
        controller ! Open(getPos(point))        
      }
  }

  override protected def paintComponent(g: Graphics2D) {
    super.paintComponent(g)
    val ppt = calcPPT(g.getClipBounds().getSize())

    for (x <- 0 to xs - 1; y <- 0 to ys - 1) {
      val v = model.getValue(x, y)
      renderer.putCell(g, v, x * ppt, y * ppt, ppt - 1, ppt - 1)
    }
  }
}

class ActorModel(model: Model, controller: ActorRef) extends Actor {
  def receive = {
    case Open(p) => {
      model.open(p)
      controller ! Modified(p)
    }
    case Flag(p) => {
      model.flag(p)
      controller ! Modified(p)
    }
  }  
}

class C2Suite extends FunSuite {
  test("C2 Board") {
    val system = ActorSystem("sample")
    val controller = system.actorOf(Props[Controller], "hoge")
    
    val b = new Board(20, 20) with BoardUI with Manip
    val s = new Splite(b) with DispSplite
    
    val f = new Frame {
      title = "Frame test"
      contents = new FlowPanel {
        contents += new C2(MyRenderer, controller, s) with TestModel with SwingController
      }
      override def closeOperation() = dispose()
    }
    
    (0 to 9).foreach(i => b.put(i, i))
    info("\n" + b.display)
    info("\n" + s.display)
    f.visible = true
  }
}
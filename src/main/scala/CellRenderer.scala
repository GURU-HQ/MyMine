package jp.co.guru.MyMine

import java.awt.Graphics2D
import java.awt.Color
import Util._
import java.awt.Font

trait CellRenderer {
  def putCell(g: Graphics2D, v: Status, x: Int, y: Int, w: Int, h: Int)  
}

trait SwingRenderer {
  implicit class MyGraphics2D(g: Graphics2D) {
    /**
     * 指定された矩形の中央に文字列を出力する
     */
    def drawCenteredString(s: String, x: Int, y: Int, w: Int, h: Int) = {
      val fm = g.getFontMetrics()
      val b = fm.getStringBounds(s, g)
      val l = fm.getLineMetrics(s, g)
      g.drawString(s, x + (w - b.getWidth().toInt) / 2,
        (y + (h - b.getHeight()) / 2 + l.getAscent()).toInt)
    }
  }
}

/**
 * マスの描画
 */
object MyRenderer extends CellRenderer with SwingRenderer {
  override def putCell(g: Graphics2D, v: Status, x: Int, y: Int, w: Int, h: Int) = {
    val s = v.toString
    if (v == Status.UNKNOWN || v == Status.FLAG) {
      g.setColor(Color.BLACK)
      g.fillRect(x, y, w, h)
    }
    g.setColor(Color.RED)
    g.drawRect(x, y, w, h)
    g.drawCenteredString(s, x, y, w, h)
  }
}

/**
 * 良い描画
 */
object GoodRenderer extends CellRenderer with SwingRenderer {
  
  /**
   * ますの種別ごとの表示内容
   */
  private def transrate(v: Status) = {
    v match {
      case Status.UNKNOWN => ""
      case Status.FLAG => "F"
      case _ => v.toString
    }
  }
  
  /** 
   *  ます描画
   */
  override def putCell(g: Graphics2D, v: Status, x: Int, y: Int, w: Int, h: Int) = {
    if (v == Status.UNKNOWN || v == Status.FLAG) {
      g.setColor(Color.BLACK)
      g.fillRect(x, y, w, h)
    }
    
    g.setColor(Color.RED)
    g.drawRect(x, y, w, h)
    
    val s = transrate(v)
    g.drawCenteredString(s, x, y, w, h)
  }
}

/**
 * もっと良い描画
 */
object BetterRenderer extends CellRenderer with SwingRenderer {
  
  /**
   * ますの種別ごとの表示内容
   */
  private def getLabel(v: Status) = {
    v match {
      case Status.UNKNOWN | Status.EMPTY => ""
      case Status.FLAG => "F"
      case _ => v.toString
    }
  }

  /**
   * ますの種別ごとの色を返す
   */
  private def getColor(v: Status) = {
    val colorTable = Array(Color.BLUE, Color.GREEN, Color.RED, Color.BLACK, new Color(128, 0, 0), 
        new Color(128, 0, 0), new Color(128, 0, 0), new Color(128, 0, 0))
    v match {
      case Status.UNKNOWN | Status.EMPTY => Color.WHITE
      case Status.FLAG => Color.RED
      case Status(n) => colorTable(n - 1)
      case _ => { assert(false); Color.PINK }
    }
  }

  /** 
   *  ます描画
   */
  override def putCell(g: Graphics2D, v: Status, x: Int, y: Int, w: Int, h: Int) = {
    val g2 = g.create.asInstanceOf[Graphics2D] // 後々面倒なのでコピーを使う
//    g2.setFont(g2.getFont().deriveFont(Font.BOLD))
    if (v == Status.UNKNOWN || v == Status.FLAG) {
      g2.setColor(Color.BLACK)
      g2.fillRect(x, y, w, h)
    }
    
    g2.setColor(Color.RED)
    g2.drawRect(x, y, w, h)
    
    val s = getLabel(v)
    g2.setColor(getColor(v))
    g2.drawCenteredString(s, x, y, w, h)
    
  }
}

package jp.co.guru.MyMine

import java.awt.Graphics2D

trait CellRenderer {
  def putCell(g: Graphics2D, v: Status, x: Int, y: Int, w: Int, h: Int)  
}

/**
 * マスの描画
 */
object MyRenderer extends CellRenderer {
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
  
  override def putCell(g: Graphics2D, v: Status, x: Int, y: Int, w: Int, h: Int) = {
    val s = v.toString
    g.drawRect(x, y, w, h)
    g.drawCenteredString(s, x, y, w, h)
  }
}


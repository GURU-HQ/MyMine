package jp.co.guru.MyMine

import Util._

class Splite(val bord: Bord) extends Model with MyRange {
  val width = bord.width
  val height = bord.height
  
  val mask = Array.fill(width, height)(Status.UNKNOWN)
  
  def display() = {
    (0 to bord.height - 1).map(y =>
      mask(y).collect({
        case Status.UNKNOWN => '?'
        case x if x == Status(0) => ' '
        case x => x.toString.charAt(0)
      }).mkString(" ")
    ).mkString("\n")
  }

  def getX() = width
  def getY() = height
  def getValue(x: Int, y: Int) = mask(x)(y)
  
  /**
   * 指定された位置が未知の状態ならtrue
   */
  def isUnknown(p: POS): Boolean = mask(p) == Status.UNKNOWN
  
  
  private def put(p: POS, c: Int) = mask(p) = new Status(c)
 
  /**
   * 上下左右のマスの内、操作可能なものを返す
   */
  def probe(p: POS) = LRUD.map(p + _).filter(isValidPos(_))
  
  /**
   * 指定された位置のマスを開きmaskを更新
   */
  def update(p: POS) = put(p, bord.countAroundBomb(p))    
  
  /**
   * 指定された位置から連続して開く
   */
  def paint(p: POS): Boolean = {
    assert(!bord.isBomb(p), "コードから爆弾が開かれた")
    
    if (bord.countAroundBomb(p) > 0) {
      update(p)
      true
    } else {
      update(p)
      probe(p).filter(isUnknown(_)).foreach(paint(_))
      true
    }
  }
}

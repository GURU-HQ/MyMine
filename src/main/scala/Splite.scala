package jp.co.guru.MyMine

import Util._
import com.typesafe.scalalogging.slf4j.Logging

/**
 * ユーザーから見た盤面を保持する
 * （チームで共有すべき状態）
 */
class Splite(val board: Board) extends Model with MyRange with Logging {
  val width = board.width
  val height = board.height
  
  val mask = Array.fill(width, height)(Status.UNKNOWN)
  
  override def getX() = width
  override def getY() = height
  override def getValue(x: Int, y: Int): Status = mask(x)(y)
  
  /**
   * クリックされた時の処理
   * この辺りから Actorベースにしていく
   */
  override def open(p: POS): Boolean = {
    if (board.isBomb(p)) {
      logger.error("BOMB")
      false
    } else if (isUnknown(p)) {
      paint(p)
      true
    } else if (mask(p) == Status(countFlag(p))) {
      probe(p).filter(isUnknown(_)).foreach(paint(_))
      true
    } else {
      false
    }
  }
  
  /**
   * 右クリックされた時の処理
   * 旗を立てる
   */
  override def flag(p: POS): Boolean = {
    if (!board.isBomb(p)) {
      logger.error("Not BOMB")
      false
    } else {
      put(p, Status.FLAG)
      true
    }
  }

  /**
   * 指定されたますが旗であることを調べる
   */
  def isFlag(p: POS): Boolean = isValidPos(p) && mask(p) == Status.FLAG

  /**
   * 周囲のますの内、旗が立っているますの数
   */
  def countFlag(p: POS): Int = probe(p).count(isFlag(_))
  
  /**
   * 指定された位置が未知の状態ならtrue
   */
  private def isUnknown(p: POS): Boolean = mask(p) == Status.UNKNOWN
  
  /**
   * 指定された位置の状態を変更
   */
  private def put(p: POS, c: Status) = mask(p) = c
 
  /**
   * 周りのマスの内、操作可能なものを返す
   */
  private def probe(p: POS) = around(p).filter(isValidPos(_))
  
  /**
   * 指定された位置のマスを開きmaskを更新
   */
  private def update(p: POS) = put(p, new Status(board.countAroundBomb(p)))    
  
  /**
   * 指定された位置から連続して開く
   */
  def paint(p: POS): Boolean = {
    assert(!board.isBomb(p), "コードから爆弾が開かれた")
    
    if (board.countAroundBomb(p) > 0) {
      update(p)
      true
    } else {
      update(p)
      probe(p).filter(isUnknown(_)).foreach(paint(_))
      true
    }
  }
}

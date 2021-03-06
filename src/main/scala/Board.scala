package jp.co.guru.MyMine

import com.typesafe.scalalogging.slf4j.Logging
import scala.util.Random
import Util._

/**
 * 盤面情報を持つ基底クラス
 */
trait BoardBase extends MyRange {
  val board: Array[Array[Status]]
  val width: Int
  val height: Int
}

/**
 * 神用Trait 
 * (下僕の民はこれを使えない)
 */
trait Manip extends Board {
  val rnd = new Random
  
  /**
   * 1要素をランダムに取り除き 結果と取り除いた要素を返す
   */
  implicit class MyIndexedSeq[A](val s: IndexedSeq[A]) {
    def randomRemove(): (A, IndexedSeq[A]) = {
      val p = rnd.nextInt(s.length)
      val sp = s.splitAt(p)
      val h = sp._1
      val t = sp._2.tail
      (sp._2.head, h ++: t)
    }
  }
  
  /**
   *   ランダムに爆弾を置き置いた爆弾の位置を返す
   */  
  def randBomb(n: Int) = {
    val sl = spaceList
    def inner(n: Int, isl: IndexedSeq[POS], bombs: IndexedSeq[POS]): IndexedSeq[POS] = {
      if (n == 0) {
        bombs
      } else {
        val (v, remain) = isl.randomRemove
        inner(n - 1, remain, v +: bombs )
      }
    }
    val bombs = inner(n, sl, IndexedSeq.empty)
    put(bombs)
  }
}

/**
 * 下僕の民が使うクラス
 */
class Board(val width: Int, val height: Int)  extends BoardBase with Logging {
  
  val board = Array.fill(width, height)(Status.EMPTY)
  
  /**
   * ボードの全ますに関数を適用し List[List[?]]として返す
   */
  def boardAll[A](f:(Int, Int) => A): IndexedSeq[IndexedSeq[A]] = {
    (0 to height - 1).map(y => 
      (0 to width -1).map(x =>
    	f(x, y)
      )
    )
  }
  
  /**
   * ボードの全ますを List[List[?]]として返す
   */
  def boardAll(): IndexedSeq[IndexedSeq[POS]] = boardAll((x, y) => (x, y))

  /**
   * 指定された座標が爆弾であることを調べる
   */
  def isBomb(p: POS): Boolean = isValidPos(p) && board(p) == Status.BOMB
  
  /**
   * 空白のますの一覧を返す
   */
  def spaceList() = boardAll().flatten.filter(tp => !isBomb(tp))
  
  /**
   * 爆弾を置く
   */
  def put(pos: POS): Unit = {
    assert(!isBomb(pos), "ここはイカン")
    board(pos) = Status.BOMB
  }
  
  /**
   * 爆弾を置きまくる
   */
  def put(bombs: Seq[POS]): Unit = bombs.foreach(p => put(p))
  
  /**
   * 周りの爆弾の個数を数える
   */
  def countAroundBomb(p: POS): Int = around(p).count(isBomb(_))
}


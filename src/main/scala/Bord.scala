package jp.co.guru.MyMine

import org.scalatest.FunSuite
import Status._
import com.typesafe.scalalogging.slf4j.Logging
import scala.util.Random

class Status(val n: Int) extends AnyVal {
  override def toString() : String = {
    n.toString + " "
  }
}

object Status {
  def apply(n: Int) = {
    new Status(n)
  }
  
  val EMPTY = Status(0)
  val BOMB = Status(9)
  val UNKNOWN = Status(-1)
}

trait BordBase {
  protected val bord: Array[Array[Status]]
  protected val width: Int
  protected val height: Int
}

trait Test extends Bord {
  def allCells(p: POS) = for(ox <- -1 to 1; oy <- -1 to 1) yield p + (ox, oy)
  def count(p: POS) = allCells(p).count(isBomb(_)) 
}

trait Manip extends Bord {
  val rnd = new Random
  
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
   *   ランダムに爆弾を置く
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

trait BordUI extends Bord with Logging {
  def display() = {
    bordAll(bord(_)(_)).map(_.mkString).mkString("\n")
  }
}

class Bord(val width: Int, val height: Int)  extends MyRange with Logging {
  
  val bord = Array.fill(width, height)(EMPTY)
  
  /**
   * ボードの全ますに関数を適用し List[List[?]]として返す
   */
  def bordAll[A](f:(Int, Int) => A): IndexedSeq[IndexedSeq[A]] = {
    (0 to height - 1).map(y => 
      (0 to width -1).map(x =>
    	f(x, y)
      )
    )
  }
  
  /**
   * ボードの全ますに関数を適用し List[List[?]]として返す
   */
  def bordAll(): IndexedSeq[IndexedSeq[POS]] = bordAll((x, y) => (x, y))

  /**
   * 指定された座標が爆弾であることを調べる
   */
  def isBomb(p: POS): Boolean = isValidPos(p) && bord(p) == BOMB
  
  /**
   * 空白のますの一覧を返す
   */
  def spaceList() = bordAll().flatten.filter(tp => !isBomb(tp))
  
  /**
   * 爆弾を置く
   */
  def put(pos: POS): Unit = {
    assert(!isBomb(pos), "ここはイカン")
    bord(pos) = BOMB
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


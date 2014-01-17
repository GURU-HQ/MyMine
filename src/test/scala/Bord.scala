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
 
  def count(x: Int, y: Int) = {
    (for(ox <- -1 to 1; oy <- -1 to 1 if isBomb(x + ox, y + oy)) yield 1).size 
  }

  def a() = {
    bordAll((x, y) => x + y) 
  }
  
  def counts() = {
    bordAll((x, y) => count(x, y)) 
  }

  def displayCounts() = {
    
  }
}

trait Manip extends Bord {
  val rnd = new Random
  
  implicit class MyIndexedSeq[A](s: IndexedSeq[A]) {
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

class Bord(val width: Int, val height: Int) extends Logging {
  type POS = (Int, Int)
  
  val bord = Array.fill(width, height)(EMPTY)
  def around(x: Int, y: Int) = for (ox <- -1 to 1; oy <- -1 to 1 if (ox != 0 || oy != 0)) yield (x + ox, y + oy)  
  
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
  
  def bordAll(): IndexedSeq[IndexedSeq[(Int, Int)]] = {
    bordAll((x, y) => (x, y))
  }

  /**
   * 指定された座標が爆弾であることを調べる
   */
  def isBomb(x: Int, y: Int): Boolean = {
    if (x < 0 || x >= bord.length || y < 0 || y >= bord.length) false
    else bord(x)(y) == BOMB
  }

  def isBomb(tp: POS): Boolean = {
    isBomb(tp._1, tp._2)
  }

  /**
   * 空白のますの一覧を返す
   * 
   */
  def spaceList() = {
    bordAll().flatten.filter(tp => !isBomb(tp._1, tp._2))
  }
  
  /**
   * 爆弾を置く
   */
  def put(pos: POS): Unit = {
    assert(isBomb(pos), "ここはイカン")
    bord(pos._1)(pos._2) = BOMB
  }
  
  /**
   * 爆弾を置きまくる
   */
  def put(bobms: Seq[POS]): Unit = {
    bobms.foreach(p => put(p))
  }
  
  /**
   * 周りの爆弾の個数を数える
   */
  def countBomb(x: Int, y: Int) = {
    around(x, y).foldLeft(0)((x, tp) => { logger.info(tp + " " + isBomb(tp).toString); x + (if (isBomb(tp)) 1 else 0) })
  }
}


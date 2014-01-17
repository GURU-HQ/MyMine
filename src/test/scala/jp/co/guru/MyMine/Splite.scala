package jp.co.guru.MyMine

import org.scalatest.FunSuite
import Status._

trait MyRange {
  type POS = (Int, Int)
  implicit class MyTupple2(tp: POS) {
    def +(other: POS) = (tp._1 + other._1, tp._2 + other._2)
  }
  
  implicit class MyArray[A](ar: Array[Array[A]]) {
    def apply(p: POS): A = ar(p._1)(p._2)
    def update(p: POS, v: A): Unit = ar(p._1)(p._2) = v 
  }
  
  implicit class MySeq[POS](list: Seq[POS]) {
    def count(f: (POS) => Boolean): Int = list.foldLeft(0)((x, y) => x + (if (f(y)) 1 else 0))
  }

  
  val width: Int
  val height: Int
  lazy val xrange = (0 to width - 1)
  lazy val yrange = (0 to height - 1)
  val isValidPos: ((POS) => Boolean) = p => xrange.contains(p._1) && yrange.contains(p._2)
  def around(p: POS) = for (ox <- -1 to 1; oy <- -1 to 1 if (ox != 0 || oy != 0)) yield (p + (ox, oy))  
}

class Splite(val bord: Bord) extends MyRange {
  val width = bord.width
  val height = bord.height
  
  val mask = Array.fill(width, height)(UNKNOWN)
  def display() = {
    (0 to bord.height - 1).map(y =>
      mask(y).collect({
        case UNKNOWN => '?'
        case x if x == Status(0) => ' '
        case x => x.toString.charAt(0)
      }).mkString(" ")
    ).mkString("\n")
  }

//  def isUnknown(x: Int, y: Int): Boolean = mask(x)(y) == UNKNOWN
  def isUnknown(p: POS): Boolean = mask(p) == UNKNOWN
  def put(p: POS, c: Int) = mask(p) = new Status(c)
  def LRUD = List((-1, 0), (1, 0), (0, -1), (0, 1))
  def probe(p: POS) = LRUD.map(p + _).filter(isValidPos(_))

  def update(p: POS) = put(p, bord.countAroundBomb(p))    
  
  def open(p: POS) = {
    if (isUnknown(p)) {
      update(p)
    } else {
      assert(false, "Already open")
    }
  }
  
  def isSpace() {
    
  }
  
  def paint(p: POS): Boolean = {
//    assert()
    if (bord.isBomb(p)) {
      false
    } else if (bord.countAroundBomb(p) > 0) {
      update(p)
      true
    } else {
      update(p)
      probe(p).filter(isUnknown(_)).foreach(paint(_))
      true
    }
  }
}

class SpliteSuite extends FunSuite {
  test("Update Test") {
    val b = new Bord(10, 10) with BordUI with Test with Manip
    val s = new Splite(b)
    (0 to 9).foreach(i => b.put(i, i))
//    b.put(5, 5)
    info("\n" + b.display)
//    b.around(5, 5).foreach(tp => s.update(tp._1, tp._2))
    s.paint(7, 1)
    info("\n" + s.display)
  }
}
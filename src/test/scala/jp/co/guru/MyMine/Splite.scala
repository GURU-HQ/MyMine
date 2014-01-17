package jp.co.guru.MyMine

import org.scalatest.FunSuite
import Status._

class Splite(val bord: Bord) {
  val mask = Array.fill(bord.width, bord.height)(UNKNOWN)
  def display() = {
    (0 to bord.height - 1).map(y =>
      mask(y).collect({
        case UNKNOWN => '?'
        case x if x == Status(0) => ' '
        case x => x.toString.charAt(0)
      }).mkString
    ).mkString("\n")
  }
  
  def isUnknown(x: Int, y: Int) = mask(x)(y) == UNKNOWN
  def put(x: Int, y: Int, c: Int) = mask(x)(y) = new Status(c)
  def around(x: Int, y: Int) = for (ox <- -1 to 1; oy <- -1 to 1 if (x != 0 || y != 0)) yield (x + ox, y + oy)  
        
  def update(x: Int, y: Int) = {
    put(x, y, bord.countBomb(x, y))    
  }
  
  def open(x: Int, y: Int) = {
    if (isUnknown(x, y)) {
      update(x, y)
    } else {
      assert(false, "Already open")
    }
  }
}

class SpliteSuite extends FunSuite {
  test("Update Test") {
    val b = new Bord(10, 10) with BordUI with Test with Manip
    val s = new Splite(b)
    b.put(5, 5)
    info("\n" + b.display)
    s.update(5, 5)
    info("\n" + s.display)
  }
}